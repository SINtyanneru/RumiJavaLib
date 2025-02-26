package su.rumishistem.rumi_java_lib.Misskey;

import com.fasterxml.jackson.databind.JsonNode;
import su.rumishistem.rumi_java_lib.Misskey.API.*;
import su.rumishistem.rumi_java_lib.Misskey.TYPE.Note;
import su.rumishistem.rumi_java_lib.Misskey.Event.EVENT_LISTENER;
import su.rumishistem.rumi_java_lib.Misskey.RESULT.LOGIN_RESULT;
import su.rumishistem.rumi_java_lib.Misskey.TYPE.NoteVis;
import su.rumishistem.rumi_java_lib.Misskey.TYPE.User;

import javax.swing.event.EventListenerList;
import java.io.IOException;
import java.net.URISyntaxException;
import java.time.OffsetDateTime;

public class MisskeyClient {
	private String INSTANCE_DOMAIN = null;
	private String TOKEN = null;
	private WSS SAPI = null;
	private User Kai = null;
	public static EventListenerList EL_LIST = new EventListenerList();

	public MisskeyClient(String INSTANCE_DOMAIN) {
		//
		this.INSTANCE_DOMAIN = INSTANCE_DOMAIN;
	}

	public void SET_EVENT_LISTENER(EVENT_LISTENER EL) {
		EL_LIST.add(EVENT_LISTENER.class, EL);
	}

	/**
	 * ログインする
	 * @param UID ユーザーID
	 * @param PASSWORD パスワード
	 * @param TOTP TOTPの値(無いならNull)
	 * @return ログインできたか
	 * @throws IOException
	 */
	public LOGIN_RESULT LOGIN(String UID, String PASSWORD, String TOTP) {
		try {
			JsonNode USER_DATA = UserSHOW.Main(INSTANCE_DOMAIN, UID);

			//TOTPが有効か
			if (!USER_DATA.get("twoFactorEnabled").asBoolean()) {
				String RESULT = LOGIN.Main(INSTANCE_DOMAIN, UID, PASSWORD);
				if (RESULT != null) {
					TOKEN = RESULT;
					return LOGIN_RESULT.DONE;
				} else {
					return LOGIN_RESULT.FAILED;
				}
			} else {
				if (TOTP != null) {
					String RESULT = LOGIN.TOTP(INSTANCE_DOMAIN, UID, PASSWORD, TOTP);
					if (RESULT != null) {
						TOKEN = RESULT;

						//自分の情報を取得
						getI();

						//WSS接続
						CONNECT_WSS();

						return LOGIN_RESULT.DONE;
					} else {
						return LOGIN_RESULT.FAILED;
					}
				} else {
					//有効且つTOTPが空ならならTOTPと返す
					return LOGIN_RESULT.TOTP;
				}
			}
		} catch (Exception EX) {
			EX.printStackTrace();
			return LOGIN_RESULT.FAILED;
		}
	}

	public LOGIN_RESULT TOKEN_LOGIN(String TOKEN) {
		try {
			this.TOKEN = TOKEN;

			//自分の情報を取得
			getI();

			CONNECT_WSS();

			return LOGIN_RESULT.DONE;
		} catch (Exception EX) {
			EX.printStackTrace();
			return LOGIN_RESULT.FAILED;
		}
	}

	private void CONNECT_WSS() throws URISyntaxException {
		SAPI = new WSS(INSTANCE_DOMAIN, TOKEN, Kai);
	}

	/**
	 * トークンを取得
	 * @return トークン
	 */
	public String getTOKEN() {
		//
		return TOKEN;
	}

	public void PostNote(Note NOTE) throws IOException {
		CreateNote.Post(INSTANCE_DOMAIN, TOKEN, NOTE);
	}

	public void CreateReaction(Note NOTE, String ReactionText) throws IOException {
		NoteReaction.Create(NOTE, ReactionText, TOKEN, INSTANCE_DOMAIN);
	}

	public Note GetNote(String ID) throws IOException {
		JsonNode NoteData = NoteShow.Main(INSTANCE_DOMAIN, TOKEN, ID);
		NoteVis VIS = null;
		String RN_ID = null;
		Note REPLY_NOTE = null;
		String CW = null;
		boolean KaiMention = false;

		//公開範囲
		switch (NoteData.get("visibility").asText()) {
			case "public": {
				VIS = NoteVis.PUBLIC;
				break;
			}

			case "home": {
				VIS = NoteVis.HOME;
				break;
			}

			case "followers": {
				VIS = NoteVis.FOLLOWER;
				break;
			}

			case "specified": {
				VIS = NoteVis.DM;
				break;
			}
		}

		//CW
		if (NoteData.get("cw") != null) {
			CW = NoteData.get("cw").asText();
		}

		//自分がメンションされているか
		if (NoteData.get("mentions") != null) {
			for (int I = 0; I < NoteData.get("mentions").size(); I++) {
				if (NoteData.get("mentions").get(I).asText().equals(Kai.getID())) {
					KaiMention = true;
					break;
				}
			}
		}

		return new Note(
			false,
			new User(
				NoteData.get("user").get("id").asText(),
				NoteData.get("user").get("username").asText(),
				NoteData.get("user").get("name").asText(),
				NoteData.get("user").get("avatarUrl").asText(),
				NoteData.get("user").get("host").asText(),
				null
			),
			NoteData.get("id").asText(),
			NoteData.get("text").asText(),
			OffsetDateTime.parse(NoteData.get("createdAt").asText()),
			VIS,
			RN_ID,
			REPLY_NOTE,
			CW,
			KaiMention
		);
	}

	private void getI() throws IOException {
		JsonNode RESULT = I.Main(TOKEN, INSTANCE_DOMAIN);
		Kai = new User(
			RESULT.get("id").asText(),
			RESULT.get("username").asText(),
			RESULT.get("name").asText(),
			RESULT.get("avatarUrl").asText(),
			INSTANCE_DOMAIN,
			TOKEN
		);
	}
}

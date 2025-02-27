package su.rumishistem.rumi_java_lib.Misskey;

import com.fasterxml.jackson.databind.JsonNode;
import su.rumishistem.rumi_java_lib.Misskey.API.*;
import su.rumishistem.rumi_java_lib.Misskey.MODULE.ConvertType;
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
			User USER_DATA = UserSHOW.GetUID(INSTANCE_DOMAIN, UID);

			//TOTPが有効か
			if (!USER_DATA.isTwoFactorEnabled()) {
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

	private void getI() throws IOException {
		JsonNode RESULT = I.Main(TOKEN, INSTANCE_DOMAIN);
		Kai = new User(
			RESULT.get("id").asText(),
			RESULT.get("username").asText(),
			RESULT.get("name").asText(),
			RESULT.get("avatarUrl").asText(),
			INSTANCE_DOMAIN,
			TOKEN,
			RESULT.get("twoFactorEnabled").asBoolean(),
			false,
			false
		);
	}

	/**
	 * トークンを取得
	 * @return トークン
	 */
	public String getTOKEN() {
		return TOKEN;
	}

	public User GetSelfUser() {
		return Kai;
	}

	public void PostNote(Note NOTE) throws IOException {
		CreateNote.Post(INSTANCE_DOMAIN, TOKEN, Kai, NOTE);
	}

	public void CreateReaction(Note NOTE, String ReactionText) throws IOException {
		NoteReaction.Create(NOTE, ReactionText, TOKEN, INSTANCE_DOMAIN);
	}

	public Note GetNote(String ID) throws IOException {
		JsonNode NoteData = NoteShow.Main(INSTANCE_DOMAIN, TOKEN, ID);
		return ConvertType.ConvertNote(NoteData, Kai, INSTANCE_DOMAIN, TOKEN);
	}

	public User GetUserID(String ID) throws IOException {
		return UserSHOW.GetID(INSTANCE_DOMAIN, ID);
	}

	public User GetUserUID(String UID) throws IOException {
		return UserSHOW.GetUID(INSTANCE_DOMAIN, UID);
	}
}

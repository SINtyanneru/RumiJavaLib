package com.rumisystem.rumi_java_lib.Misskey;

import com.fasterxml.jackson.databind.JsonNode;
import com.rumisystem.rumi_java_lib.Misskey.API.*;
import com.rumisystem.rumi_java_lib.Misskey.TYPE.Note;
import com.rumisystem.rumi_java_lib.Misskey.Event.EVENT_LISTENER;
import com.rumisystem.rumi_java_lib.Misskey.RESULT.LOGIN_RESULT;
import com.rumisystem.rumi_java_lib.Misskey.TYPE.User;

import javax.swing.event.EventListenerList;
import java.io.IOException;
import java.net.URISyntaxException;

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
			JsonNode USER_DATA = SHOW.Main(INSTANCE_DOMAIN, UID);

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

package com.rumisystem.rumi_java_lib.Misskey;

import com.fasterxml.jackson.databind.JsonNode;
import com.rumisystem.rumi_java_lib.Misskey.API.CreateNote;
import com.rumisystem.rumi_java_lib.Misskey.API.LOGIN;
import com.rumisystem.rumi_java_lib.Misskey.API.SHOW;
import com.rumisystem.rumi_java_lib.Misskey.TYPE.Note;
import com.rumisystem.rumi_java_lib.Misskey.Event.EVENT_LISTENER;
import com.rumisystem.rumi_java_lib.Misskey.RESULT.LOGIN_RESULT;

import javax.swing.event.EventListenerList;
import java.io.IOException;
import java.net.URISyntaxException;

public class MisskeyClient {
	private String INSTANCE_DOMAIN = null;
	private String TOKEN = null;
	private WSS SAPI = null;
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
			CONNECT_WSS();

			return LOGIN_RESULT.DONE;
		} catch (Exception EX) {
			EX.printStackTrace();
			return LOGIN_RESULT.FAILED;
		}
	}

	private void CONNECT_WSS() throws URISyntaxException {
		SAPI = new WSS(INSTANCE_DOMAIN, TOKEN);
	}

	/**
	 * トークンを取得
	 * @return トークン
	 */
	public String getTOKEN() {
		return TOKEN;
	}

	public void PostNote(Note NOTE) throws IOException {
		CreateNote.Post(INSTANCE_DOMAIN, TOKEN, NOTE);
	}
}

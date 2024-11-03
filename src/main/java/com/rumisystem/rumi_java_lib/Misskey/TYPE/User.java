package com.rumisystem.rumi_java_lib.Misskey.TYPE;

import com.rumisystem.rumi_java_lib.Misskey.API.Follow;

public class User {
	private String ID;
	private String UID;
	private String NAME;
	private String ICON_URL;

	private String DOMAIN;
	private String TOKEN;

	public User(String ID, String UID, String NAME, String ICON_URL, String DOMAIN, String TOKEN) {
		this.ID = ID;
		this.UID = UID;
		this.NAME = NAME;
		this.ICON_URL = ICON_URL;

		this.DOMAIN = DOMAIN;
		this.TOKEN = TOKEN;
	}

	public String getID() {
		return ID;
	}

	public String getUID() {
		return UID;
	}

	public String getNAME() {
		return NAME;
	}

	public String getICON_URL() {
		return ICON_URL;
	}

	public boolean Follow() {
		try {
			Follow.Create(ID, TOKEN, DOMAIN);
			return true;
		} catch (Exception EX) {
			EX.printStackTrace();
			return false;
		}
	}
}

package com.rumisystem.rumi_java_lib.Misskey.TYPE;

public class User {
	private String ID;
	private String UID;
	private String NAME;
	private String ICON_URL;

	public User(String  ID, String UID, String NAME, String ICON_URL) {
		this.ID = ID;
		this.UID = UID;
		this.NAME = NAME;
		this.ICON_URL = ICON_URL;
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
}

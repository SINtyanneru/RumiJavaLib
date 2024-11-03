package com.rumisystem.rumi_java_lib.Misskey.TYPE;

import java.time.OffsetDateTime;

public class Note {
	private User USER;
	private String ID;
	private String TEXT;
	private OffsetDateTime DATE;
	private NoteVis VIS;
	private String RENOTE_ID;
	private String REPLY_ID;
	private boolean CW = false;
	private String CW_TEXT;

	private boolean BUILD = false;

	public Note(boolean BUILD, User USER, String  ID, String TEXT, OffsetDateTime DATE, NoteVis VIS, String RENOTE_ID, String REPLY_ID, String CW_TEXT) {
		this.BUILD = BUILD;
		this.USER = USER;
		this.ID = ID;
		this.TEXT = TEXT;
		this.DATE = DATE;
		this.VIS = VIS;
		this.RENOTE_ID = RENOTE_ID;
		this.REPLY_ID = REPLY_ID;

		//CW
		if (CW_TEXT != null) {
			CW = true;
			this.CW_TEXT = CW_TEXT;
		}
	}

	public User getUSER() {
		return USER;
	}

	public String getID() {
		return ID;
	}

	public String getTEXT() {
		return TEXT;
	}

	public boolean isRN() {
		if (RENOTE_ID != null) {
			return true;
		} else {
			return false;
		}
	}

	public boolean isREPLY() {
		if (REPLY_ID != null) {
			return true;
		} else {
			return false;
		}
	}

	public boolean isCW() {
		return CW;
	}

	public String getCW() {
		return CW_TEXT;
	}

	public OffsetDateTime getDATE() {
		return DATE;
	}
}

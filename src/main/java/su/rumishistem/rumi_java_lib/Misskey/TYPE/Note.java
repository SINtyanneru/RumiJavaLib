package su.rumishistem.rumi_java_lib.Misskey.TYPE;

import su.rumishistem.rumi_java_lib.Misskey.API.NoteReaction;

import java.time.OffsetDateTime;

public class Note {
	private User USER;
	private String ID;
	private String TEXT;
	private OffsetDateTime DATE;
	private NoteVis VIS;
	private String RENOTE_ID;
	private Note REPLY_NOTE;
	private boolean CW = false;
	private String CW_TEXT;
	private boolean KaiMention = false;

	private boolean BUILD = false;

	public Note(boolean BUILD, User USER, String  ID, String TEXT, OffsetDateTime DATE, NoteVis VIS, String RENOTE_ID, Note REPLY_NOTE, String CW_TEXT, boolean KaiMention) {
		this.BUILD = BUILD;
		this.USER = USER;
		this.ID = ID;
		this.TEXT = TEXT;
		this.DATE = DATE;
		this.VIS = VIS;
		this.RENOTE_ID = RENOTE_ID;
		this.REPLY_NOTE = REPLY_NOTE;

		//CW
		if (CW_TEXT != null) {
			CW = true;
			this.CW_TEXT = CW_TEXT;
		}

		this.KaiMention = KaiMention;
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

	public Note getReply() {
		return REPLY_NOTE;
	}

	public boolean isREPLY() {
		if (REPLY_NOTE != null) {
			return true;
		} else {
			return false;
		}
	}

	public boolean isCW() {
		return CW;
	}

	public boolean isKaiMention() {
		return KaiMention;
	}

	public String getCW() {
		return CW_TEXT;
	}

	public OffsetDateTime getDATE() {
		return DATE;
	}
}

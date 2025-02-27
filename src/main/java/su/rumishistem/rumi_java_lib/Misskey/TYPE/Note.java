package su.rumishistem.rumi_java_lib.Misskey.TYPE;

import su.rumishistem.rumi_java_lib.Misskey.API.NoteReaction;
import su.rumishistem.rumi_java_lib.Misskey.API.NoteShow;
import su.rumishistem.rumi_java_lib.Misskey.MODULE.ConvertType;

import java.io.IOException;
import java.time.OffsetDateTime;

public class Note {
	private User Kai;
	private String DOMAIN;
	private String TOKEN;
	private User USER;
	private String ID;
	private String TEXT;
	private OffsetDateTime DATE;
	private NoteVis VIS;
	private String RENOTE_ID;
	private String REPLY_ID;
	private boolean CW = false;
	private String CW_TEXT;
	private boolean KaiMention = false;

	private boolean BUILD = false;

	public Note(boolean BUILD, User Kai, String DOMAIN, String TOKEN, User USER, String ID, String TEXT, OffsetDateTime DATE, NoteVis VIS, String RENOTE_ID, String REPLY_ID, String CW_TEXT, boolean KaiMention) {
		this.Kai = Kai;
		this.DOMAIN = DOMAIN;
		this.TOKEN = TOKEN;
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

		this.KaiMention = KaiMention;
	}

	public void SetKai(User Kai) {
		this.Kai = Kai;
	}

	public void SetDOMAIN(String DOMAIN) {
		this.DOMAIN = DOMAIN;
	}

	public void SetTOKEN(String TOKEN) {
		this.TOKEN = TOKEN;
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

	public Note getReply() throws IOException {
		return ConvertType.ConvertNote(NoteShow.Main(DOMAIN, TOKEN, REPLY_ID), Kai, DOMAIN, TOKEN);
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

package com.rumisystem.rumi_java_lib.Misskey.Builder;

import com.rumisystem.rumi_java_lib.Misskey.TYPE.Note;
import com.rumisystem.rumi_java_lib.Misskey.TYPE.NoteVis;

public class NoteBuilder {
	private String TEXT = "";
	private String[] FILE = null;
	private NoteVis VIS = NoteVis.PUBLIC;
	private String CW_TEXT = null;
	private Note REPLY_NOTE = null;

	public void setTEXT(String TEXT) {
		//a
		this.TEXT = TEXT;
	}

	public void setVIS(NoteVis VIS) {
		//a
		this.VIS = VIS;
	}

	public void setCW(String CW_TEXT) {
		//a
		this.CW_TEXT = CW_TEXT;
	}

	public void setREPLY(Note NOTE) {
		//a
		this.REPLY_NOTE = NOTE;
	}

	public Note Build() {
		Note NOTE = new Note(
			false,
			null,
			null,
			TEXT,
			null,
			VIS,
			null,
			REPLY_NOTE,
			CW_TEXT,
			false
		);

		return NOTE;
	}
}

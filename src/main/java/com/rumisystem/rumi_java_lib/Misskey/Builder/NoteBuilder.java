package com.rumisystem.rumi_java_lib.Misskey.Builder;

import com.rumisystem.rumi_java_lib.Misskey.Class.Note;
import com.rumisystem.rumi_java_lib.Misskey.Class.NoteVis;

import java.time.OffsetDateTime;

public class NoteBuilder {
	private String TEXT = "";
	private String[] FILE = null;
	private NoteVis VIS = NoteVis.PUBLIC;
	private String CW_TEXT = null;

	public void setTEXT(String TEXT) {
		this.TEXT = TEXT;
	}

	public void setVIS(NoteVis VIS) {
		this.VIS = VIS;
	}

	public void setCW(String CW_TEXT) {
		this.CW_TEXT = CW_TEXT;
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
				null,
				CW_TEXT
		);

		return NOTE;
	}
}

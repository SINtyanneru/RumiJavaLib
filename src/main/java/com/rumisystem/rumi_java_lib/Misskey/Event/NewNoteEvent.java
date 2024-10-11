package com.rumisystem.rumi_java_lib.Misskey.Event;

import com.rumisystem.rumi_java_lib.Misskey.Class.Note;
import com.rumisystem.rumi_java_lib.Misskey.Class.User;

public class NewNoteEvent {
	private Note NOTE = null;

	public NewNoteEvent(Note NOTE) {
		this.NOTE = NOTE;
	}

	public Note getNOTE() {
		return NOTE;
	}

	public User getUSER() {
		return NOTE.getUSER();
	}
}
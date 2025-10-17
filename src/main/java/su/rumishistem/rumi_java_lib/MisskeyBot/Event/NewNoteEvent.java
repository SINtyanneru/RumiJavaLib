package su.rumishistem.rumi_java_lib.MisskeyBot.Event;

import su.rumishistem.rumi_java_lib.MisskeyBot.Type.Note;

public class NewNoteEvent {
	private Note note;

	public NewNoteEvent(Note note) {
		this.note = note;
	}

	public Note get_note() {
		return note;
	}
}

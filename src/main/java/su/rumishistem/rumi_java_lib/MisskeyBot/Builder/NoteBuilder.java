package su.rumishistem.rumi_java_lib.MisskeyBot.Builder;

import su.rumishistem.rumi_java_lib.MisskeyBot.Type.Note;
import su.rumishistem.rumi_java_lib.MisskeyBot.Type.NoteVisibility;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class NoteBuilder {
	public String text;
	public String cw;
	public List<File> file_list = new ArrayList<>();
	public String reply_id;
	public String renote_id;

	public boolean local_only = false;
	public NoteVisibility visibility = NoteVisibility.Public;

	public void set_text(String text) {
		this.text = text;
	}

	public void set_cw(String cw) {
		this.cw = cw;
	}

	public void add_file(File file) {
		file_list.add(file);
	}

	public void set_reply(Note note) {
		reply_id = note.get_id();
	}

	public void set_renote(Note note) {
		renote_id = note.get_id();
	}

	public void local_only() {
		local_only = true;
	}

	public void set_visibility(NoteVisibility visibility) {
		this.visibility = visibility;
	}
}

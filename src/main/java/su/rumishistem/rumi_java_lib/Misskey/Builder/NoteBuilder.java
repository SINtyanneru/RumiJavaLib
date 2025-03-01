package su.rumishistem.rumi_java_lib.Misskey.Builder;

import su.rumishistem.rumi_java_lib.FETCH;
import su.rumishistem.rumi_java_lib.FormData;
import su.rumishistem.rumi_java_lib.Misskey.TYPE.AttachFile;
import su.rumishistem.rumi_java_lib.Misskey.TYPE.Note;
import su.rumishistem.rumi_java_lib.Misskey.TYPE.NoteVis;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;

public class NoteBuilder {
	private String TEXT = "";
	private List<File> FileList = new ArrayList<>();
	private NoteVis VIS = NoteVis.PUBLIC;
	private String CW_TEXT = null;
	private Note REPLY_NOTE = null;

	public void AddFile(File F) {
		FileList.add(F);
	}

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

	public Note Build() throws IOException {
		String ReplyID = null;
		if (REPLY_NOTE != null) {
			ReplyID = REPLY_NOTE.getID();
		}

		List<AttachFile> FileURLList = new ArrayList<>();
		for (File F:FileList) {
			FileURLList.add(new AttachFile(F.toPath().toString(), F.getName(), false));
		}

		Note NOTE = new Note(
			false,
			null,
			null,
			null,
			null,
			null,
			TEXT,
			null,
			VIS,
			null,
			ReplyID,
			CW_TEXT,
			false,
			FileURLList
		);

		return NOTE;
	}
}

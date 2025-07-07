package su.rumishistem.rumi_java_lib.Misskey.API;

import com.fasterxml.jackson.databind.ObjectMapper;
import su.rumishistem.rumi_java_lib.FETCH;
import su.rumishistem.rumi_java_lib.FETCH_RESULT;
import su.rumishistem.rumi_java_lib.HTTP_REQUEST;
import su.rumishistem.rumi_java_lib.Misskey.TYPE.Note;

import java.io.IOException;
import java.util.HashMap;

public class NoteReaction {
	public static void Create(Note NOTE, String Reaction, String TOKEN, String DOMAIN) throws IOException {
		FETCH AJAX = new FETCH("https://" + DOMAIN + "/api/notes/reactions/create");
		AJAX.SetHEADER("Content-Type", "application/json; charset=utf-8");

		HashMap<String, String> POST_BODY = new HashMap<>();
		POST_BODY.put("i", TOKEN);
		POST_BODY.put("noteId", NOTE.getID());
		POST_BODY.put("reaction", Reaction);

		//実行
		FETCH_RESULT RESULT = AJAX.POST(new ObjectMapper().writeValueAsString(POST_BODY).getBytes());
		if (RESULT.getStatusCode() != 204) {
			throw new Error("Reaction Error");
		}
	}
}

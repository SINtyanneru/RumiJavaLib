package su.rumishistem.rumi_java_lib.Misskey.API;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import su.rumishistem.rumi_java_lib.HTTP_REQUEST;

import java.io.IOException;

public class NoteShow {
	public static JsonNode Main(String DOMAIN, String TOKEN, String NoteID) throws IOException {
		HTTP_REQUEST SHOW_AJAX = new HTTP_REQUEST("https://" + DOMAIN + "/api/notes/show");
		SHOW_AJAX.HEADER_SET("Content-Type", "application/json; charset=utf-8");

		return new ObjectMapper().readTree(SHOW_AJAX.POST("{\"noteId\":\"" + NoteID + "\",\"i\":\"" + TOKEN + "\"}"));
	}
}

package com.rumisystem.rumi_java_lib.Misskey.API;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.rumisystem.rumi_java_lib.HTTP_REQUEST;
import com.rumisystem.rumi_java_lib.Misskey.Class.Note;

import java.io.IOException;

public class CreateNote {
	public static void Post(String DOMAIN, String TOKEN, Note NOTE) throws IOException {
		HTTP_REQUEST NOTE_AJAX = new HTTP_REQUEST("https://" + DOMAIN + "/api/notes/create");
		NOTE_AJAX.HEADER_SET("Content-Type", "application/json; charset=utf-8");

		JsonNode RESULT = new ObjectMapper().readTree(NOTE_AJAX.POST(
				"{\"text\":\"" + NOTE.getTEXT() + "\"," +
						"\"poll\":null," +
						"\"cw\":null," +
						"\"localOnly\":false," +
						"\"visibility\":\"public\"," +
						"\"reactionAcceptance\":null," +
						"\"i\":\"" + TOKEN + "\"}"
		));
	}
}

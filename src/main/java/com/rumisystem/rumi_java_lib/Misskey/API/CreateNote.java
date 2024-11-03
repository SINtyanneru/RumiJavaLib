package com.rumisystem.rumi_java_lib.Misskey.API;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.rumisystem.rumi_java_lib.HTTP_REQUEST;
import com.rumisystem.rumi_java_lib.Misskey.TYPE.Note;

import java.io.IOException;
import java.util.HashMap;

public class CreateNote {
	public static void Post(String DOMAIN, String TOKEN, Note NOTE) throws IOException {
		HTTP_REQUEST NOTE_AJAX = new HTTP_REQUEST("https://" + DOMAIN + "/api/notes/create");
		NOTE_AJAX.HEADER_SET("Content-Type", "application/json; charset=utf-8");

		ObjectMapper OM = new ObjectMapper();

		HashMap<String, Object> POST_DATA = new HashMap<>();
		POST_DATA.put("i", TOKEN);
		POST_DATA.put("text", NOTE.getTEXT());
		POST_DATA.put("poll", null);
		POST_DATA.put("cw", null);
		POST_DATA.put("localOnly", false);
		POST_DATA.put("visibility", "public");
		POST_DATA.put("reactionAcceptance", null);

		JsonNode RESULT = new ObjectMapper().readTree(NOTE_AJAX.POST(OM.writeValueAsString(POST_DATA)));
	}
}

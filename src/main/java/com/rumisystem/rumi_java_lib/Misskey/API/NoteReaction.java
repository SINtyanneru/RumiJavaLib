package com.rumisystem.rumi_java_lib.Misskey.API;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.rumisystem.rumi_java_lib.FETCH;
import com.rumisystem.rumi_java_lib.FETCH_RESULT;
import com.rumisystem.rumi_java_lib.HTTP_REQUEST;
import com.rumisystem.rumi_java_lib.Misskey.TYPE.Note;

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
		if (RESULT.GetSTATUS_CODE() != 204) {
			throw new Error("Reaction Error");
		}
	}
}

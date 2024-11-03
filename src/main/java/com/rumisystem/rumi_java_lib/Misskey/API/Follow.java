package com.rumisystem.rumi_java_lib.Misskey.API;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.rumisystem.rumi_java_lib.HTTP_REQUEST;

import java.io.IOException;

public class Follow {
	public static void Create(String UID, String TOKEN, String DOMAIN) throws IOException {
		HTTP_REQUEST NOTE_AJAX = new HTTP_REQUEST("https://" + DOMAIN + "/api/following/create");
		NOTE_AJAX.HEADER_SET("Content-Type", "application/json; charset=utf-8");

		NOTE_AJAX.POST(
			"{"
				+"\"i\":\"" + TOKEN + "\","
				+"\"userId\": \"" + UID + "\","
				+"\"withReplies\": false"
			+"}"
		);
	}
}

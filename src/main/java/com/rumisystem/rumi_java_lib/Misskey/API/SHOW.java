package com.rumisystem.rumi_java_lib.Misskey.API;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.rumisystem.rumi_java_lib.HTTP_REQUEST;

import java.io.IOException;

public class SHOW {
	public static JsonNode Main(String DOMAIN, String UID) throws IOException {
		HTTP_REQUEST SHOW_AJAX = new HTTP_REQUEST("https://" + DOMAIN + "/api/users/show");
		SHOW_AJAX.HEADER_SET("Content-Type", "application/json; charset=utf-8");

		return new ObjectMapper().readTree(SHOW_AJAX.POST("{\"username\":\"" + UID + "\"}"));
	}
}

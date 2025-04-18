package su.rumishistem.rumi_java_lib.Misskey.API;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import su.rumishistem.rumi_java_lib.HTTP_REQUEST;

import java.io.IOException;

public class I {
	public static JsonNode Main(String TOKEN, String DOMAIN) throws IOException {
		HTTP_REQUEST NOTE_AJAX = new HTTP_REQUEST("https://" + DOMAIN + "/api/i");
		NOTE_AJAX.HEADER_SET("Content-Type", "application/json; charset=utf-8");

		return new ObjectMapper().readTree(
			NOTE_AJAX.POST(
				"{\"i\":\"" + TOKEN + "\"}"
			)
		);
	}
}

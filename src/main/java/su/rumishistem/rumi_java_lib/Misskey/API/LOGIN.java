package su.rumishistem.rumi_java_lib.Misskey.API;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import su.rumishistem.rumi_java_lib.HTTP_REQUEST;

import java.io.IOException;

public class LOGIN {
	public static String Main(String DOMAIN, String UID, String PASS) throws IOException {
		HTTP_REQUEST LOGIN_AJAX = new HTTP_REQUEST("https://" + DOMAIN + "/api/signin");
		LOGIN_AJAX.HEADER_SET("Content-Type", "application/json; charset=utf-8");

		JsonNode RESULT = new ObjectMapper().readTree(LOGIN_AJAX.POST("{\"username\":\"" + UID + "\",\"password\":\"" + PASS + "\"}"));

		if (RESULT.get("i") != null) {
			return RESULT.get("i").asText();
		} else {
			return null;
		}
	}

	public static String TOTP(String DOMAIN, String UID, String PASS, String TOTP) throws IOException {
		HTTP_REQUEST LOGIN_AJAX = new HTTP_REQUEST("https://" + DOMAIN + "/api/signin");
		LOGIN_AJAX.HEADER_SET("Content-Type", "application/json; charset=utf-8");

		JsonNode RESULT = new ObjectMapper().readTree(LOGIN_AJAX.POST("{\"username\":\"" + UID + "\",\"password\":\"" + PASS + "\", \"token\":\"" + TOTP + "\"}"));

		if (RESULT.get("i") != null) {
			return RESULT.get("i").asText();
		} else {
			return null;
		}
	}
}

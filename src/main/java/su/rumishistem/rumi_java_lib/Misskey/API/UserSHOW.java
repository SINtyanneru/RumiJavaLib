package su.rumishistem.rumi_java_lib.Misskey.API;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import su.rumishistem.rumi_java_lib.FETCH;
import su.rumishistem.rumi_java_lib.FETCH_RESULT;
import su.rumishistem.rumi_java_lib.Misskey.MODULE.ConvertType;
import su.rumishistem.rumi_java_lib.Misskey.TYPE.User;

import java.io.IOException;

public class UserSHOW {
	public static User GetUID(String DOMAIN, String UID, User Kai, String TOKEN) throws IOException {
		FETCH AJAX = new FETCH("https://" + DOMAIN + "/api/users/show");
		AJAX.SetHEADER("Content-Type", "application/json; charset=utf-8");
		FETCH_RESULT RESULT = AJAX.POST(("{\"username\":\"" + UID + "\"}").getBytes());
		JsonNode UserData = new ObjectMapper().readTree(RESULT.GetRAW());

		return ConvertType.ConvertUser(UserData, Kai, DOMAIN, TOKEN);
	}

	public static User GetID(String DOMAIN, String ID, User Kai, String TOKEN) throws IOException {
		FETCH AJAX = new FETCH("https://" + DOMAIN + "/api/users/show");
		AJAX.SetHEADER("Content-Type", "application/json; charset=utf-8");
		FETCH_RESULT RESULT = AJAX.POST(("{\"userId\":\"" + ID + "\"}").getBytes());
		JsonNode UserData = new ObjectMapper().readTree(RESULT.GetRAW());

		return ConvertType.ConvertUser(UserData, Kai, DOMAIN, TOKEN);
	}
}

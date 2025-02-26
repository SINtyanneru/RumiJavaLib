package su.rumishistem.rumi_java_lib.Misskey.API;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import su.rumishistem.rumi_java_lib.FETCH;
import su.rumishistem.rumi_java_lib.FETCH_RESULT;
import su.rumishistem.rumi_java_lib.HTTP_REQUEST;
import su.rumishistem.rumi_java_lib.Misskey.MODULE.ConvertUser;
import su.rumishistem.rumi_java_lib.Misskey.TYPE.User;

import java.io.IOException;

public class UserSHOW {
	public static User GetUID(String DOMAIN, String UID) throws IOException {
		FETCH AJAX = new FETCH("https://" + DOMAIN + "/api/users/show");
		AJAX.SetHEADER("Content-Type", "application/json; charset=utf-8");
		FETCH_RESULT RESULT = AJAX.POST(("{\"username\":\"" + UID + "\"}").getBytes());
		JsonNode UserData = new ObjectMapper().readTree(RESULT.GetRAW());

		return ConvertUser.Convert(UserData);
	}

	public static User GetID(String DOMAIN, String ID) throws IOException {
		FETCH AJAX = new FETCH("https://" + DOMAIN + "/api/users/show");
		AJAX.SetHEADER("Content-Type", "application/json; charset=utf-8");
		FETCH_RESULT RESULT = AJAX.POST(("{\"userId\":\"" + ID + "\"}").getBytes());
		JsonNode UserData = new ObjectMapper().readTree(RESULT.GetRAW());

		return ConvertUser.Convert(UserData);
	}
}

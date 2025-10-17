package su.rumishistem.rumi_java_lib.MisskeyBot.API;

import com.fasterxml.jackson.databind.*;
import su.rumishistem.rumi_java_lib.Ajax.*;
import su.rumishistem.rumi_java_lib.MisskeyBot.Exception.LoginException;
import su.rumishistem.rumi_java_lib.MisskeyBot.MisskeyClient;

import java.io.IOException;
import java.util.HashMap;

public class GetI {
	private MisskeyClient client;

	public GetI(MisskeyClient client) {
		this.client = client;
	}

	public JsonNode get() throws IOException {
		HashMap<String, String> i_body = new HashMap<>();
		i_body.put("i", client.get_token());
		Ajax ajax = new Ajax("https://"+client.get_host()+"/api/i");
		ajax.set_header("Content-Type", client.MIME_JSON);

		//解析
		AjaxResult result = ajax.POST(new ObjectMapper().writeValueAsBytes(i_body));
		if (result.get_code() != 200) {
			//ログイン失敗
			throw new LoginException();
		} else {
			return new ObjectMapper().readTree(result.get_body_as_string());
		}
	}
}

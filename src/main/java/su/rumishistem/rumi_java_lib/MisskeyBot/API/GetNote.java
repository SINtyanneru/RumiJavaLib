package su.rumishistem.rumi_java_lib.MisskeyBot.API;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import su.rumishistem.rumi_java_lib.Ajax.Ajax;
import su.rumishistem.rumi_java_lib.Ajax.AjaxResult;
import su.rumishistem.rumi_java_lib.MisskeyBot.Exception.LoginException;
import su.rumishistem.rumi_java_lib.MisskeyBot.MisskeyClient;

import java.io.IOException;
import java.util.HashMap;

public class GetNote {
	private MisskeyClient client;

	public GetNote(MisskeyClient client) {
		this.client = client;
	}

	public JsonNode get(String id) throws IOException {
		HashMap<String, String> i_body = new HashMap<>();
		i_body.put("i", client.get_token());
		i_body.put("noteId", id);

		Ajax ajax = new Ajax("https://"+client.get_host()+"/api/notes/show");
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

package su.rumishistem.rumi_java_lib.MisskeyBot;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import su.rumishistem.rumi_java_lib.Ajax.Ajax;
import su.rumishistem.rumi_java_lib.Ajax.AjaxResult;
import su.rumishistem.rumi_java_lib.MisskeyBot.Exception.LoginException;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.HashMap;

public class MisskeyClient {
	private static final String MIME_JSON = "application/json; charset=UTF-8";

	private ObjectMapper om = new ObjectMapper();
	private String host;
	private String token;

	public MisskeyClient(String host, String token) {
		this.host = host;
		this.token = token;

		try {
			//ログイン
			HashMap<String, String> i_body = new HashMap<>();
			i_body.put("i", token);
			Ajax ajax = new Ajax("https://"+host+"/api/i");
			ajax.set_header("Content-Type", MIME_JSON);

			//解析
			AjaxResult result = ajax.POST(om.writeValueAsBytes(i_body));
			if (result.get_code() == 200) {
				JsonNode parse_body = om.readTree(result.get_body_as_string());
			} else {
				//ログイン失敗
				throw new LoginException();
			}
		} catch (IOException e) {
			//あ？
		}
	}
}

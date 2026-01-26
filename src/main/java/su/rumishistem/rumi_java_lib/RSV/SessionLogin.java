package su.rumishistem.rumi_java_lib.RSV;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import su.rumishistem.rsdf_java.RSDFDecoder;
import su.rumishistem.rumi_java_lib.FETCH;
import su.rumishistem.rumi_java_lib.FETCH_RESULT;

import java.util.Map;

public class SessionLogin {
	private boolean login = false;
	private String session = null;
	private JsonNode account_data = null;

	public SessionLogin(String host, String session) {
		try {
			FETCH ajax = new FETCH("http://" + host + "/api/Session?ID=" + session);
			FETCH_RESULT result = ajax.GET();
			if (result.getStatusCode() == 200) {
				Map<String, Object> d = RSDFDecoder.decode(result.getRaw()).get_dict();

				if ((Boolean)d.get("STATUS")) {
					this.login = true;
					this.session = session;

					JsonNode json = new ObjectMapper().readTree(new ObjectMapper().writeValueAsString(d.get("ACCOUNT_DATA")));
					this.account_data = json;
				}
			}
		} catch (Exception EX) {
			EX.printStackTrace();
		}
	}

	public boolean get_status() {
		return login;
	}

	public JsonNode get_account_data() {
		return account_data;
	}

	public String get_session() {
		return session;
	}
}
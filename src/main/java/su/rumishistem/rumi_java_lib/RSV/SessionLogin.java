package su.rumishistem.rumi_java_lib.RSV;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import su.rumishistem.rumi_java_lib.FETCH;
import su.rumishistem.rumi_java_lib.FETCH_RESULT;

public class SessionLogin {
	private boolean login = false;
	private String session = null;
	private JsonNode account_data = null;

	public SessionLogin(String host, String session) {
		try {
			FETCH ajax = new FETCH("http://" + host + "/Session?ID=" + session + "&SERVICE=STORAGE");
			FETCH_RESULT result = ajax.GET();
			if (result.getStatusCode() == 200) {
				JsonNode RESULT = new ObjectMapper().readTree(result.getString());
				if (RESULT.get("STATUS").asBoolean()) {
					this.login = true;
					this.session = session;
					this.account_data = RESULT.get("ACCOUNT_DATA");
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
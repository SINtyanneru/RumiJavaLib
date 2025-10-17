package su.rumishistem.rumi_java_lib.MisskeyBot.Type;

import com.fasterxml.jackson.databind.JsonNode;
import su.rumishistem.rumi_java_lib.MisskeyBot.MisskeyClient;

public class SelfUser {
	private User user;

	public SelfUser(MisskeyClient client, JsonNode body) {
		user = new User(client, body);
	}

	public User get_user() {
		return user;
	}
}

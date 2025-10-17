package su.rumishistem.rumi_java_lib.MisskeyBot.Type;

import com.fasterxml.jackson.databind.JsonNode;
import su.rumishistem.rumi_java_lib.MisskeyBot.MisskeyClient;

import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;

public class User {
	private MisskeyClient client;

	private String id;
	private String username;
	private String name;
	private String host;
	private String icon_url;
	private boolean is_cat;
	private boolean is_bot;
	private ZonedDateTime regist_date;
	private ZonedDateTime update_date;

	public User(MisskeyClient client, JsonNode body) {
		id = body.get("id").asText();
		username = body.get("username").asText();
		name = body.get("name").asText();

		if (!body.get("host").isNull()) {
			host = body.get("host").asText();
		}

		icon_url = body.get("avatarUrl").asText();
		is_cat = body.get("isCat").asBoolean();
		is_bot = body.get("isBot").asBoolean();

		Instant regist_instant = Instant.parse(body.get("createdAt").asText());
		regist_date = regist_instant.atZone(ZoneId.systemDefault());

		Instant update_instant = Instant.parse(body.get("updatedAt").asText());
		update_date = update_instant.atZone(ZoneId.systemDefault());
	}

	public String get_id() {
		return id;
	}

	public String get_username() {
		return username;
	}

	public String get_name() {
		if (name != null) return name;
		return username;
	}

	public boolean is_name_null() {
		return name == null;
	}

	public String get_host() {
		if (host == null) return client.get_host();
		return host;
	}

	public boolean is_local() {
		return host == null;
	}

	public String get_icon_url() {
		return icon_url;
	}

	public boolean is_cat() {
		return is_cat;
	}

	public boolean is_bot() {
		return is_bot;
	}

	public ZonedDateTime get_regist_date() {
		return regist_date;
	}

	public ZonedDateTime geT_update_date() {
		return update_date;
	}
}

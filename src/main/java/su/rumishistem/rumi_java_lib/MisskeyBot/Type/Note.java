package su.rumishistem.rumi_java_lib.MisskeyBot.Type;

import com.fasterxml.jackson.databind.JsonNode;
import su.rumishistem.rumi_java_lib.MisskeyBot.API.GetNote;
import su.rumishistem.rumi_java_lib.MisskeyBot.API.GetUser;
import su.rumishistem.rumi_java_lib.MisskeyBot.MisskeyClient;

import java.io.IOException;

public class Note {
	private MisskeyClient client;

	private User user;
	private String id;
	private String text;
	private String cw;
	private NoteVisibility visibility = NoteVisibility.Public;
	private boolean local_only = false;
	private String reply_id;
	private String renote_id;

	public Note(MisskeyClient client, JsonNode body) throws IOException {
		this.client = client;
		user = new User(client, new GetUser(client).get_from_id(body.get("user").get("id").asText()));

		id = body.get("id").asText();

		if (!body.get("text").isNull()) {
			text = body.get("text").asText();
		}

		if (!body.get("cw").isNull()) {
			cw = body.get("cw").asText();
		}

		//公開範囲
		switch (body.get("visibility").asText()) {
			case "public": visibility = NoteVisibility.Public; break;
			case "home": visibility = NoteVisibility.Home; break;
			case "followers": visibility = NoteVisibility.Followers; break;
			case "specified": visibility = NoteVisibility.DM; break;
		}
		local_only = body.get("localOnly").asBoolean();

		if (!body.get("replyId").isNull()) {
			reply_id = body.get("replyId").asText();
		}

		if (!body.get("renoteId").isNull()) {
			renote_id = body.get("renoteId").asText();
		}
	}

	public User get_user() {
		return user;
	}

	public String get_id() {
		return id;
	}

	public String get_text() {
		return text;
	}

	public boolean is_cw() {
		return cw != null;
	}

	public String get_cw() {
		return cw;
	}

	public NoteVisibility get_visibility() {
		return visibility;
	}

	public boolean is_local_only() {
		return local_only;
	}

	public Note get_reply() {
		try {
			return new Note(client, new GetNote(client).get(reply_id));
		} catch (IOException ex) {
			ex.printStackTrace();
			throw new RuntimeException("ノートが無い");
		}
	}

	public Note get_renote() {
		try {
			return new Note(client, new GetNote(client).get(renote_id));
		} catch (IOException ex) {
			ex.printStackTrace();
			throw new RuntimeException("ノートが無い");
		}
	}

	public boolean is_mention() {
		return text.contains("@" + client.get_self().get_user().get_username());
	}
}

package su.rumishistem.rumi_java_lib.MisskeyBot;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import su.rumishistem.rumi_java_lib.Ajax.Ajax;
import su.rumishistem.rumi_java_lib.Ajax.AjaxResult;
import su.rumishistem.rumi_java_lib.MisskeyBot.API.GetI;
import su.rumishistem.rumi_java_lib.MisskeyBot.API.GetNote;
import su.rumishistem.rumi_java_lib.MisskeyBot.API.GetUser;
import su.rumishistem.rumi_java_lib.MisskeyBot.API.UploadFile;
import su.rumishistem.rumi_java_lib.MisskeyBot.Builder.NoteBuilder;
import su.rumishistem.rumi_java_lib.MisskeyBot.Event.MisskeyEventListener;
import su.rumishistem.rumi_java_lib.MisskeyBot.Exception.LoginException;
import su.rumishistem.rumi_java_lib.MisskeyBot.Type.Note;
import su.rumishistem.rumi_java_lib.MisskeyBot.Type.SelfUser;
import su.rumishistem.rumi_java_lib.MisskeyBot.Type.User;
import su.rumishistem.rumi_java_lib.WebSocket.Client.WebSocketClient;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MisskeyClient {
	public static final String MIME_JSON = "application/json; charset=UTF-8";

	private OkHttpClient ws;
	private StreamAPI sa;
	private String host;
	private String token;
	private SelfUser self;

	protected List<MisskeyEventListener> listener_list = new ArrayList<>();

	public MisskeyClient(String host, String token) throws IOException {
		this.host = host;
		this.token = token;

		//ログイン(できなければエラー落ち)
		JsonNode i = new GetI(this).get();
		self = new SelfUser(this, i);

		//WebSocketにログイン
		ws = new OkHttpClient();
		sa = new StreamAPI(this, ws);
		sa.connect();
	}

	public void add_event_listener(MisskeyEventListener listener) {
		listener_list.add(listener);
	}

	public OkHttpClient get_ws() {
		return ws;
	}

	public String get_host() {
		return host;
	}

	public String get_token() {
		return token;
	}

	public SelfUser get_self() {
		return self;
	}

	public String create_note(NoteBuilder build) {
		HashMap<String, Object> body = new HashMap<>();
		body.put("i", token);
		body.put("text", build.text);
		body.put("cw", build.cw);
		body.put("replyId", build.reply_id);
		body.put("renoteId", build.renote_id);
		body.put("localOnly", build.local_only);

		//閲覧
		switch (build.visibility) {
			case Public: body.put("visibility", "public"); break;
			case Home: body.put("visibility", "home"); break;
			case Followers: body.put("visibility", "followers"); break;
			case DM: body.put("visibility", "specified"); break;
		}

		//ファイル
		if (!build.file_list.isEmpty()) {
			List<String> file_list = new ArrayList<>();
			for (File file:build.file_list) {
				try {
					JsonNode d = new UploadFile(this).create_from_file(file.getName(), file);
					file_list.add(d.get("id").asText());
				} catch (IOException ex) {
					throw new RuntimeException("ドライブにアップロード失敗：IOException");
				}
			}
			body.put("fileIds", file_list);
		}

		try {
			Ajax ajax = new Ajax("https://"+host+"/api/notes/create");
			ajax.set_header("Content-Type", MIME_JSON);
			AjaxResult result = ajax.POST(new ObjectMapper().writeValueAsBytes(body));
			JsonNode result_body = new ObjectMapper().readTree(result.get_body_as_string());

			if (result.get_code() != 200) {
				throw new RuntimeException("ノートを作れなかった:" + result_body);
			}

			return result_body.get("createdNote").get("id").asText();
		} catch (IOException ex) {
			ex.printStackTrace();
			throw new RuntimeException("ノートを作れなかった: IOException");
		}
	}

	public void create_reaction(Note note, String emoji) {
		HashMap<String, Object> body = new HashMap<>();
		body.put("i", token);
		body.put("noteId", note.get_id());
		body.put("reaction", ":"+emoji+":");

		try {
			Ajax ajax = new Ajax("https://"+host+"/api/notes/reactions/create");
			ajax.set_header("Content-Type", MIME_JSON);
			AjaxResult result = ajax.POST(new ObjectMapper().writeValueAsBytes(body));

			if (result.get_code() != 204) {
				JsonNode result_body = new ObjectMapper().readTree(result.get_body_as_string());
				throw new RuntimeException("リアクション失敗:" + result_body);
			}
		} catch (IOException ex) {
			ex.printStackTrace();
			throw new RuntimeException("リアクション失敗: IOException");
		}
	}

	public void follow(User user) {
		HashMap<String, Object> body = new HashMap<>();
		body.put("i", token);
		body.put("userId", user.get_id());

		try {
			Ajax ajax = new Ajax("https://"+host+"/api/following/create");
			ajax.set_header("Content-Type", MIME_JSON);
			AjaxResult result = ajax.POST(new ObjectMapper().writeValueAsBytes(body));

			if (result.get_code() != 200) {
				JsonNode result_body = new ObjectMapper().readTree(result.get_body_as_string());
				throw new RuntimeException("フォロー失敗:" + result_body);
			}
		} catch (IOException ex) {
			ex.printStackTrace();
			throw new RuntimeException("フォロー失敗: IOException");
		}
	}

	public void unfollow(User user) {
		HashMap<String, Object> body = new HashMap<>();
		body.put("i", token);
		body.put("userId", user.get_id());

		try {
			Ajax ajax = new Ajax("https://"+host+"/api/following/delete");
			ajax.set_header("Content-Type", MIME_JSON);
			AjaxResult result = ajax.POST(new ObjectMapper().writeValueAsBytes(body));

			if (result.get_code() != 200) {
				JsonNode result_body = new ObjectMapper().readTree(result.get_body_as_string());
				throw new RuntimeException("フォロー失敗:" + result_body);
			}
		} catch (IOException ex) {
			ex.printStackTrace();
			throw new RuntimeException("フォロー失敗: IOException");
		}
	}

	public Note get_note_from_id(String id) {
		try {
			return new Note(this, new GetNote(this).get(id));
		} catch (IOException ex) {
			throw new RuntimeException("ノート取得失敗");
		}
	}

	public User get_user_from_id(String id) {
		try {
			return new User(this, new GetUser(this).get_from_id(id));
		} catch (IOException ex) {
			throw new RuntimeException("ユーザー取得失敗");
		}
	}

	public User get_user_from_id(String name, String host) {
		try {
			return new User(this, new GetUser(this).get_from_name(name, host));
		} catch (IOException ex) {
			throw new RuntimeException("ユーザー取得失敗");
		}
	}
}

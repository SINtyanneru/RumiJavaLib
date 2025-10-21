package su.rumishistem.rumi_java_lib.MisskeyBot;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import okhttp3.*;
import org.jetbrains.annotations.NotNull;
import su.rumishistem.rumi_java_lib.MisskeyBot.API.GetUser;
import su.rumishistem.rumi_java_lib.MisskeyBot.Event.*;
import su.rumishistem.rumi_java_lib.MisskeyBot.Type.Note;
import su.rumishistem.rumi_java_lib.MisskeyBot.Type.User;

import java.io.IOException;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class StreamAPI extends WebSocketListener {
	private static final String HOME_TL_ID = "1";
	private static final String MAIN_ID = "2";

	private WebSocket ws;
	private OkHttpClient ohp;
	private MisskeyClient client;
	public StreamAPI(MisskeyClient client, OkHttpClient ohp) {
		this.client = client;
		this.ohp = ohp;
	}

	public void connect() {
		Request req = new Request.Builder().url("wss://"+client.get_host()+"/streaming?i=" + client.get_token()).build();
		ws = ohp.newWebSocket(req, this);
	}

	@Override
	public void onOpen(WebSocket s, Response r) {
		s.send("{\"type\":\"connect\",\"body\":{\"channel\":\"main\",\"id\":\""+MAIN_ID+"\"}}");
		s.send("{\"type\":\"connect\",\"body\":{\"channel\":\"homeTimeline\",\"id\":\""+HOME_TL_ID+"\",\"params\":{\"withRenotes\":true}}}");

		//PING
		ScheduledExecutorService schedule = Executors.newScheduledThreadPool(1);
		schedule.scheduleAtFixedRate(new Runnable() {
			@Override
			public void run() {
				s.send("h");
			}
		}, 0, 1, TimeUnit.MINUTES);

		for (MisskeyEventListener listener:client.listener_list) {
			listener.Ready();
		}
	}

	@Override
	public void onMessage(WebSocket s, String text) {
		try {
			JsonNode body = new ObjectMapper().readTree(text);

			if (body.get("body").get("type").asText().equals("note")) {
				//ノートが流れてきた
				Note note = new Note(client, body.get("body").get("body"));
				for (MisskeyEventListener listener:client.listener_list) {
					listener.NewNote(new NewNoteEvent(note));
				}
			} else if (body.get("body").get("type").asText().equals("followed")) {
				//フォローされた
				User user = new User(client, new GetUser(client).get_from_id(body.get("body").get("body").get("id").asText()));
				for (MisskeyEventListener listener:client.listener_list) {
					listener.NewFollow(new NewFollowEvent(user));
				}
			} else if (body.get("body").get("type").asText().equals("notification") && body.get("body").get("body").get("type").asText().equals("unfollow")) {
				//フォロー解除された
				User user = new User(client, new GetUser(client).get_from_id(body.get("body").get("body").get("user").get("id").asText()));
				for (MisskeyEventListener listener:client.listener_list) {
					listener.UnFollow(new UnFollowEvent(user));
				}
			} else if (body.get("body").get("type").asText().equals("notification") && body.get("body").get("body").get("type").asText().equals("blocked")) {
				//ブロックされた
				User user = new User(client, new GetUser(client).get_from_id(body.get("body").get("body").get("user").get("id").asText()));
				for (MisskeyEventListener listener:client.listener_list) {
					listener.NewBlock(new NewBlockEvent(user));
				}
			} else if (body.get("body").get("type").asText().equals("notification") && body.get("body").get("body").get("type").asText().equals("unblocked")) {
				//ブロック解除された
				User user = new User(client, new GetUser(client).get_from_id(body.get("body").get("body").get("user").get("id").asText()));
				for (MisskeyEventListener listener:client.listener_list) {
					listener.UnBlock(new UnBlockEvent(user));
				}
			}

		} catch (IOException ex) {
			ex.printStackTrace();
		}
	}

	@Override
	public void onClosed(WebSocket s, int code, String reason) {
		connect();
	}
}

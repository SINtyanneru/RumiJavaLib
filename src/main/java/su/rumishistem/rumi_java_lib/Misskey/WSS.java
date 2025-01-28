package su.rumishistem.rumi_java_lib.Misskey;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import su.rumishistem.rumi_java_lib.Misskey.Event.NewFollower;
import su.rumishistem.rumi_java_lib.Misskey.TYPE.Note;
import su.rumishistem.rumi_java_lib.Misskey.TYPE.NoteVis;
import su.rumishistem.rumi_java_lib.Misskey.TYPE.User;
import su.rumishistem.rumi_java_lib.Misskey.Event.EVENT_LISTENER;
import su.rumishistem.rumi_java_lib.Misskey.Event.NewNoteEvent;
import su.rumishistem.rumi_java_lib.WebSocket.Client.EVENT.CLOSE_EVENT;
import su.rumishistem.rumi_java_lib.WebSocket.Client.EVENT.CONNECT_EVENT;
import su.rumishistem.rumi_java_lib.WebSocket.Client.EVENT.MESSAGE_EVENT;
import su.rumishistem.rumi_java_lib.WebSocket.Client.EVENT.WS_EVENT_LISTENER;
import su.rumishistem.rumi_java_lib.WebSocket.Client.WebSocketClient;

import java.net.URISyntaxException;
import java.time.OffsetDateTime;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import static su.rumishistem.rumi_java_lib.Misskey.MisskeyClient.EL_LIST;

public class WSS {
	public WSS(String DOMAIN, String TOKEN, User Kai) throws URISyntaxException {
		WebSocketClient WSC = new WebSocketClient();
		WSC.SET_EVENT_LISTENER(new WS_EVENT_LISTENER() {
			@Override
			public void CONNECT(CONNECT_EVENT E) {
				E.SEND("{\"type\":\"connect\",\"body\":{\"channel\":\"main\",\"id\":\"main\"}}\n");
				E.SEND("{\"type\":\"connect\",\"body\":{\"channel\":\"homeTimeline\",\"id\":\"homeTL\",\"params\":{\"withRenotes\":true}}}\n");

				//イベント着火
				EVENT_LISTENER[] LISTENER_LIST = EL_LIST.getListeners(EVENT_LISTENER.class);
				for(EVENT_LISTENER LISTENER:LISTENER_LIST){
					LISTENER.onReady();
				}

				ScheduledExecutorService SHE = Executors.newScheduledThreadPool(1);
				Runnable PING = new Runnable() {
					@Override
					public void run() {
						E.SEND("h");
					}
				};
				SHE.scheduleAtFixedRate(PING, 0, 1, TimeUnit.MINUTES);
			}

			@Override
			public void MESSAGE(MESSAGE_EVENT E) {
				try {
					JsonNode MSG = new ObjectMapper().readTree(E.getMessage());
					switch (MSG.get("body").get("id").asText()) {
						case "main": {
							switch (MSG.get("body").get("type").asText()) {
								//メンション
								case "mention":{
									break;
								}

								//フォローされた
								case "followed": {
									User FROM = new User(
										MSG.get("body").get("body").get("id").asText(),
										MSG.get("body").get("body").get("username").asText(),
										MSG.get("body").get("body").get("name").asText(),
										MSG.get("body").get("body").get("avatarUrl").asText(),
										DOMAIN,
										TOKEN
									);

									//イベント着火
									EVENT_LISTENER[] LISTENER_LIST = EL_LIST.getListeners(EVENT_LISTENER.class);
									for(EVENT_LISTENER LISTENER:LISTENER_LIST){
										LISTENER.onNewFollower(new NewFollower(FROM));
									}
									break;
								}
							}
							break;
						}

						case "homeTL": {
							if (MSG.get("body").get("type").asText().equals("note")) {
								JsonNode USER_DATA = MSG.get("body").get("body").get("user");
								JsonNode NOTE_DATA = MSG.get("body").get("body");
								JsonNode RN_DATA = MSG.get("body").get("body").get("renote");
								JsonNode REPLY_DATA = MSG.get("body").get("body").get("reply");

								NoteVis VIS = null;
								String RN_ID = null;
								Note REPLY_NOTE = null;
								String CW = null;
								boolean KaiMention = false;

								//公開範囲
								switch (NOTE_DATA.get("visibility").asText()) {
									case "public": {
										VIS = NoteVis.PUBLIC;
										break;
									}

									case "home": {
										VIS = NoteVis.HOME;
										break;
									}

									case "followers": {
										VIS = NoteVis.FOLLOWER;
										break;
									}

									case "specified": {
										VIS = NoteVis.DM;
										break;
									}
								}

								//リノート
								if (RN_DATA != null) {
									RN_ID = RN_DATA.get("id").asText();
								}

								//返信
								if (REPLY_DATA != null) {
									REPLY_NOTE = null;
								}

								//CW
								if (NOTE_DATA.get("cw") != null) {
									CW = NOTE_DATA.get("cw").asText();
								}

								//自分がメンションされているか
								if (NOTE_DATA.get("mentions") != null) {
									for (int I = 0; I < NOTE_DATA.get("mentions").size(); I++) {
										if (NOTE_DATA.get("mentions").get(I).asText().equals(Kai.getID())) {
											KaiMention = true;
											break;
										}
									}
								}

								User USER = new User(
										USER_DATA.get("id").asText(),
										USER_DATA.get("username").asText(),
										USER_DATA.get("name").asText(),
										USER_DATA.get("avatarUrl").asText(),
										DOMAIN,
										TOKEN
								);
								Note NOTE = new Note(
										false,
										USER,
										NOTE_DATA.get("id").asText(),
										NOTE_DATA.get("text").asText(),
										OffsetDateTime.parse(NOTE_DATA.get("createdAt").asText()),
										VIS,
										RN_ID,
										REPLY_NOTE,
										CW,
										KaiMention
								);

								//イベント着火
								EVENT_LISTENER[] LISTENER_LIST = EL_LIST.getListeners(EVENT_LISTENER.class);
								for(EVENT_LISTENER LISTENER:LISTENER_LIST){
									LISTENER.onNewNote(new NewNoteEvent(NOTE));
								}
							}
							break;
						}
					}
				} catch (Exception EX) {
					EX.printStackTrace();
				}
			}

			@Override
			public void CLOSE(CLOSE_EVENT E) {
				//WebSocket接続
				WSC.CONNECT("wss://" + DOMAIN + "/streaming?i=" + TOKEN);
			}

			@Override
			public void EXCEPTION(Exception EX) {
				EX.printStackTrace();
			}
		});

		//WebSocket接続
		WSC.CONNECT("wss://" + DOMAIN + "/streaming?i=" + TOKEN);
	}
}

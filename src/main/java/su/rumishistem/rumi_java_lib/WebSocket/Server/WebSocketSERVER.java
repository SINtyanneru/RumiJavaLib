package su.rumishistem.rumi_java_lib.WebSocket.Server;

import su.rumishistem.rumi_java_lib.LOG_PRINT.LOG_TYPE;
import su.rumishistem.rumi_java_lib.SnowFlake;
import su.rumishistem.rumi_java_lib.Socket.Server.EVENT.CloseEvent;
import su.rumishistem.rumi_java_lib.Socket.Server.EVENT.EVENT_LISTENER;
import su.rumishistem.rumi_java_lib.Socket.Server.EVENT.MessageEvent;
import su.rumishistem.rumi_java_lib.Socket.Server.EVENT.ReceiveEvent;
import su.rumishistem.rumi_java_lib.Socket.Server.SocketServer;
import su.rumishistem.rumi_java_lib.WebSocket.Client.WebSocketClient;
import su.rumishistem.rumi_java_lib.WebSocket.Server.EVENT.CLOSE_EVENT;
import su.rumishistem.rumi_java_lib.WebSocket.Server.EVENT.MESSAGE_EVENT;
import su.rumishistem.rumi_java_lib.WebSocket.Server.EVENT.WS_EVENT_LISTENER;
import su.rumishistem.rumi_java_lib.WebSocket.Server.CONNECT_EVENT.CONNECT_EVENT;
import su.rumishistem.rumi_java_lib.WebSocket.Server.CONNECT_EVENT.CONNECT_EVENT_LISTENER;

import javax.swing.event.EventListenerList;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Base64;
import java.util.HashMap;

import static su.rumishistem.rumi_java_lib.LOG_PRINT.Main.LOG;

public class WebSocketSERVER {
	public static EventListenerList CONNECT_EL_LIST = new EventListenerList();
	public static EventListenerList EL_LIST = new EventListenerList();

	public static HashMap<Integer, String> CEL_LIST = new HashMap<>();		//接続後のイベントリスナーのhashCodeとUUIDを保存
	public static HashMap<String, su.rumishistem.rumi_java_lib.Socket.Server.CONNECT_EVENT.CONNECT_EVENT> SESSION_LIST = new HashMap<>();

	public void START(int PORT) {
		try {
			String User_HomeDIR = System.getProperty("user.home");
			String BINARY_PATH = User_HomeDIR + "/.bun/bin/bun";
			StringBuilder CODE = new StringBuilder();

			InputStream IS = WebSocketClient.class.getClassLoader().getResourceAsStream("WebSocketServer.js");
			BufferedReader CODE_BR = new BufferedReader(new InputStreamReader(IS));
			String LINE;
			while ((LINE = CODE_BR.readLine()) != null) {
				CODE.append(LINE + "\n");
			}
			String SCRIPT_FILENAME = "/tmp/" + SnowFlake.GEN() + ".js";
			Files.createFile(Path.of(SCRIPT_FILENAME));
			Files.write(Path.of(SCRIPT_FILENAME), CODE.toString().getBytes());

			if (!CODE.isEmpty()) {
				new Thread(new Runnable() {
					@Override
					public void run() {
						SocketServer SS = new SocketServer();
						SS.setEventListener(new su.rumishistem.rumi_java_lib.Socket.Server.CONNECT_EVENT.CONNECT_EVENT_LISTENER() {
							@Override
							public void CONNECT(su.rumishistem.rumi_java_lib.Socket.Server.CONNECT_EVENT.CONNECT_EVENT SESSION) throws IOException {
								String ID = String.valueOf(SnowFlake.GEN());

								SESSION_LIST.put(ID, SESSION);

								//コネクトイベント
								CONNECT_EVENT_LISTENER[] ELL = CONNECT_EL_LIST.getListeners(CONNECT_EVENT_LISTENER.class);
								for (CONNECT_EVENT_LISTENER EL:ELL) {
									EL.CONNECT_EVENT(new CONNECT_EVENT(SESSION.getIP(), ID));
								}

								SESSION.setEventListener(new EVENT_LISTENER() {
									@Override
									public void Message(MessageEvent E) {
										//メッセージ受信イベント
										WS_EVENT_LISTENER[] ELL = EL_LIST.getListeners(WS_EVENT_LISTENER.class);
										for (WS_EVENT_LISTENER EL:ELL) {
											if (CEL_LIST.get(EL.hashCode()).equals(ID)) {
												EL.MESSAGE(new MESSAGE_EVENT(E.getString()));
											}
										}
									}

									@Override
									public void Receive(ReceiveEvent E) {}

									@Override
									public void Close(CloseEvent E) {
										//セッションリストから消す
										SESSION_LIST.remove(ID);

										//クローズイベント
										WS_EVENT_LISTENER[] ELL = EL_LIST.getListeners(WS_EVENT_LISTENER.class);
										for (WS_EVENT_LISTENER EL:ELL) {
											if (CEL_LIST.get(EL.hashCode()).equals(ID)) {
												EL.CLOSE(new CLOSE_EVENT("", 0));
											}
										}
									}
								});
							}
						});
						try {
							SS.START(PORT + 1);
						} catch (InterruptedException EX) {
							EX.printStackTrace();
							System.exit(1);
						}
					}
				}).start();

				ProcessBuilder processBuilder = new ProcessBuilder(BINARY_PATH, SCRIPT_FILENAME, String.valueOf(PORT));
				processBuilder.redirectErrorStream(true);
				Process P = processBuilder.start();

				BufferedReader BR = new BufferedReader(new InputStreamReader(P.getInputStream()));

				new Thread(new Runnable() {
					@Override
					public void run() {
						try {
							String LINE;
							while((LINE = BR.readLine()) != null) {
								LOG(LOG_TYPE.INFO, "[JavaScript]" + LINE);
							}
						} catch (Exception EX) {
							EX.printStackTrace();
						}
					}
				}).start();

				LOG(LOG_TYPE.OK, "0.0.0.0:" + PORT + " de WebSocketServer kidou");

				//死活チェック用
				while (true) {
					if (!P.isAlive()) {
						LOG(LOG_TYPE.FAILED, "JS ga shinda");
						System.exit(1);
					}
				}
			} else {
				throw new Error("JS file ga nai");
			}
		} catch (Exception EX) {
			EX.printStackTrace();
		}
	}

	//イベントリスナーの受信先を設定するやつ
	public void SET_EVENT_VOID(CONNECT_EVENT_LISTENER EVL){
		CONNECT_EL_LIST.add(CONNECT_EVENT_LISTENER.class, EVL);
	}
}

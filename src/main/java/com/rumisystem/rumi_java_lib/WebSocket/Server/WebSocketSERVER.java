package com.rumisystem.rumi_java_lib.WebSocket.Server;

import com.rumisystem.rumi_java_lib.FILER;
import com.rumisystem.rumi_java_lib.LOG_PRINT.LOG_TYPE;
import com.rumisystem.rumi_java_lib.WebSocket.Client.WebSocketClient;
import com.rumisystem.rumi_java_lib.WebSocket.Server.EVENT.CLOSE_EVENT;
import com.rumisystem.rumi_java_lib.WebSocket.Server.EVENT.MESSAGE_EVENT;
import com.rumisystem.rumi_java_lib.WebSocket.Server.EVENT.WS_EVENT_LISTENER;
import com.rumisystem.rumi_java_lib.WebSocket.Server.CONNECT_EVENT.CONNECT_EVENT;
import com.rumisystem.rumi_java_lib.WebSocket.Server.CONNECT_EVENT.CONNECT_EVENT_LISTENER;

import javax.swing.event.EventListenerList;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.nio.file.Paths;
import java.util.Base64;
import java.util.HashMap;

import static com.rumisystem.rumi_java_lib.LOG_PRINT.Main.LOG;

public class WebSocketSERVER {
	public static EventListenerList CONNECT_EL_LIST = new EventListenerList();
	public static EventListenerList EL_LIST = new EventListenerList();

	public static HashMap<Integer, String> CEL_LIST = new HashMap<>();		//接続後のイベントリスナーのhashCodeとUUIDを保存

	private static BufferedReader BR = null;
	private static PrintWriter BW  = null;

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

			if (!CODE.isEmpty()) {
				ProcessBuilder processBuilder = new ProcessBuilder(BINARY_PATH, "-e", CODE.toString());
				processBuilder.redirectErrorStream(true);

				Process P = processBuilder.start();

				BR = new BufferedReader(new InputStreamReader(P.getInputStream()));
				BW = new PrintWriter(P.getOutputStream());

				new Thread(new Runnable() {
					@Override
					public void run() {
						try {
							String LINE;
							while((LINE = BR.readLine()) != null) {
								String[] CMD = LINE.split(" ");

								switch (CMD[0]) {
									case "NEW": {
										String ID = CMD[1];
										String IP = CMD[2];

										CONNECT_EVENT_LISTENER[] ELL = CONNECT_EL_LIST.getListeners(CONNECT_EVENT_LISTENER.class);
										for (CONNECT_EVENT_LISTENER EL:ELL) {
											EL.CONNECT_EVENT(new CONNECT_EVENT(IP, ID));
										}
										break;
									}

									case "RECEIVE": {
										WS_EVENT_LISTENER[] ELL = EL_LIST.getListeners(WS_EVENT_LISTENER.class);
										for (WS_EVENT_LISTENER EL:ELL) {
											if (CEL_LIST.get(EL.hashCode()).equals(CMD[1])) {
												EL.MESSAGE(new MESSAGE_EVENT(new String(Base64.getDecoder().decode(CMD[2].getBytes(StandardCharsets.UTF_8)))));
											}
										}
										break;
									}

									case "CLOSE": {
										WS_EVENT_LISTENER[] ELL = EL_LIST.getListeners(WS_EVENT_LISTENER.class);
										for (WS_EVENT_LISTENER EL:ELL) {
											if (CEL_LIST.get(EL.hashCode()).equals(CMD[1])) {
												EL.CLOSE(new CLOSE_EVENT("", 0));
											}
										}
									}
								}
							}
						} catch (Exception EX) {
							EX.printStackTrace();
						}
					}
				}).start();

				RunCMD("START " + PORT);

				LOG(LOG_TYPE.OK, "0.0.0.0:" + PORT + " de WebSocketServer kidou");
			} else {
				throw new Error("JS file ga nai");
			}
		} catch (Exception EX) {
			EX.printStackTrace();
		}
	}


	public static void RunCMD(String CMD) {
		BW.println(CMD);
		BW.flush();
	}

	//イベントリスナーの受信先を設定するやつ
	public void SET_EVENT_VOID(CONNECT_EVENT_LISTENER EVL){
		CONNECT_EL_LIST.add(CONNECT_EVENT_LISTENER.class, EVL);
	}
}

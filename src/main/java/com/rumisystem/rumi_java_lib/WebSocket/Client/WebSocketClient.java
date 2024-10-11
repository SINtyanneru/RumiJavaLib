package com.rumisystem.rumi_java_lib.WebSocket.Client;

import com.rumisystem.rumi_java_lib.FILER;
import com.rumisystem.rumi_java_lib.WebSocket.Client.EVENT.CLOSE_EVENT;
import com.rumisystem.rumi_java_lib.WebSocket.Client.EVENT.CONNECT_EVENT;
import com.rumisystem.rumi_java_lib.WebSocket.Client.EVENT.MESSAGE_EVENT;
import com.rumisystem.rumi_java_lib.WebSocket.Client.EVENT.WS_EVENT_LISTENER;

import javax.swing.event.EventListenerList;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.URISyntaxException;
import java.nio.file.Paths;
import java.util.Base64;

public class WebSocketClient {
	public static EventListenerList EL_LIST = new EventListenerList();

	private static BufferedReader BR = null;
	private static PrintWriter BW  = null;

	public void CONNECT(String URL) {
		try {
			String User_HomeDIR = System.getProperty("user.home");
			String BINARY_PATH = User_HomeDIR + "/.bun/bin/bun";
			StringBuilder CODE = new StringBuilder();

			InputStream IS = WebSocketClient.class.getClassLoader().getResourceAsStream("WebSocketClient.js");
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
							while ((LINE = BR.readLine()) != null) {
								String[] CMD = LINE.split(" ");

								switch (CMD[0]) {
									case "OPEN": {
										WS_EVENT_LISTENER[] ELL = EL_LIST.getListeners(WS_EVENT_LISTENER.class);
										for (WS_EVENT_LISTENER EL:ELL) {
											EL.CONNECT(new CONNECT_EVENT());
										}
										break;
									}

									case "RECEIVE": {
										WS_EVENT_LISTENER[] ELL = EL_LIST.getListeners(WS_EVENT_LISTENER.class);
										for (WS_EVENT_LISTENER EL:ELL) {
											EL.MESSAGE(new MESSAGE_EVENT(Base64.getDecoder().decode(CMD[1]).toString()));
										}
										break;
									}

									case "CLOSE": {
										WS_EVENT_LISTENER[] ELL = EL_LIST.getListeners(WS_EVENT_LISTENER.class);
										for (WS_EVENT_LISTENER EL:ELL) {
											EL.CLOSE(new CLOSE_EVENT("", 0));
										}
										break;
									}
								}
							}
						} catch (Exception EX) {
							EX.printStackTrace();
						}
					}
				}).start();
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

	public void SET_EVENT_LISTENER(WS_EVENT_LISTENER EVENT_LISTENER) {
		EL_LIST.add(WS_EVENT_LISTENER.class, EVENT_LISTENER);
	}
}

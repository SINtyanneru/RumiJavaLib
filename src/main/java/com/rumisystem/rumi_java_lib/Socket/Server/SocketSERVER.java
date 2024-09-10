package com.rumisystem.rumi_java_lib.Socket.Server;

import com.rumisystem.rumi_java_lib.Socket.Server.EVENT.CONNECT_EVENT;
import com.rumisystem.rumi_java_lib.Socket.Server.EVENT.EVENT_LISTENER;
import com.rumisystem.rumi_java_lib.Socket.Server.EVENT.MESSAGE_EVENT;

import javax.swing.event.EventListenerList;
import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Set;
import java.util.UUID;

public class SocketSERVER {
	public static EventListenerList EL_LIST = new EventListenerList();				//イベントリスナーのリスト
	public static HashMap<Socket, String> CLIENT_LIST = new HashMap<>();			//クライアント一覧
	public static HashMap<String, BufferedReader> BR_LIST = new HashMap<>();
	public static HashMap<String, PrintWriter> BW_LIST = new HashMap<>();

	public void START(int PORT) throws IOException {
		ServerSocket SS = new ServerSocket(PORT);

		new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					while(true) {
						Socket SOCKET = SS.accept();
						String ID = UUID.randomUUID().toString();

						//リストに追加
						CLIENT_LIST.put(SOCKET, UUID.randomUUID().toString());
						BR_LIST.put(ID, new BufferedReader(new InputStreamReader(SOCKET.getInputStream())));
						BW_LIST.put(ID, new PrintWriter(SOCKET.getOutputStream(), true));

						//イベント発火
						EVENT_LISTENER[] ELL = EL_LIST.getListeners(EVENT_LISTENER.class);
						for(EVENT_LISTENER EL:ELL) {
							EL.CONNECT(new CONNECT_EVENT(ID, SOCKET));
						}

						//TODO:スレッド量産するのも良くない気がする
						Thread MESSAGE_THREAD = new Thread(new Runnable() {
							@Override
							public void run() {
								try {
									BufferedReader BR = BR_LIST.get(ID);
									if (BR != null) {
										String TEXT = "";
										while((TEXT = BR.readLine()) != null) {
											//イベント発火
											EVENT_LISTENER[] ELL = EL_LIST.getListeners(EVENT_LISTENER.class);
											for(EVENT_LISTENER EL:ELL) {
												EL.MESSAGE(new MESSAGE_EVENT(ID, TEXT));
											}
										}
									}
								} catch (Exception EX) {
									EX.printStackTrace();
								}
							}
						});
					}
				} catch (Exception EX) {
					EX.printStackTrace();
				}
			}
		}).start();
	}

	public void SET_EVENT_LISTENER(EVENT_LISTENER EL) {
		EL_LIST.add(EVENT_LISTENER.class, EL);
	}
}

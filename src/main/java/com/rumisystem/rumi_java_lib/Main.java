package com.rumisystem.rumi_java_lib;

import com.rumisystem.rumi_java_lib.Socket.Server.CONNECT_EVENT.CONNECT_EVENT;
import com.rumisystem.rumi_java_lib.Socket.Server.CONNECT_EVENT.CONNECT_EVENT_LISTENER;
import com.rumisystem.rumi_java_lib.Socket.Server.EVENT.CloseEvent;
import com.rumisystem.rumi_java_lib.Socket.Server.EVENT.EVENT_LISTENER;
import com.rumisystem.rumi_java_lib.Socket.Server.EVENT.MessageEvent;
import com.rumisystem.rumi_java_lib.Socket.Server.SocketServer;

public class Main {
	public static void main(String[] args) {
		try {
			SocketServer SS = new SocketServer();

			SS.setEventListener(new CONNECT_EVENT_LISTENER() {
				@Override
				public void CONNECT(CONNECT_EVENT SESSION) {
					try {
						SESSION.sendMessage("a\n");

						SESSION.setEventListener(new EVENT_LISTENER() {
							@Override
							public void Message(MessageEvent E) {
								System.out.println("受信：" + E.getString());
							}

							@Override
							public void Close(CloseEvent E) {

							}
						});
					} catch (Exception EX) {
						EX.printStackTrace();
					}
				}
			});

			SS.START(8081);

			/*
			MisskeyClient MC = new MisskeyClient("ussr.rumiserver.com");
			if (MC.TOKEN_LOGIN("4LfqJNO9w8x1x6rD") == LOGIN_RESULT.DONE) {
				MC.SET_EVENT_LISTENER(new EVENT_LISTENER() {
					@Override
					public void onReady() {
						System.out.println("サーバーに接続した");
					}

					@Override
					public void onNewNote(NewNoteEvent E) {
						System.out.println(E.getUSER().getNAME() + "さんのノート「" + E.getNOTE().getTEXT() + "」");
					}
				});
			} else {
				System.out.println("ログイン失敗");
			}*/
		} catch (Exception EX) {
			EX.printStackTrace();
		}
	}
}

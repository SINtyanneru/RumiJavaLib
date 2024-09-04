package com.rumisystem.rumi_java_lib;

import com.rumisystem.rumi_java_lib.WebSocket.CONNECT_EVENT.CONNECT_EVENT;
import com.rumisystem.rumi_java_lib.WebSocket.CONNECT_EVENT.CONNECT_EVENT_LISTENER;
import com.rumisystem.rumi_java_lib.WebSocket.EVENT.CLOSE_EVENT;
import com.rumisystem.rumi_java_lib.WebSocket.EVENT.MESSAGE_EVENT;
import com.rumisystem.rumi_java_lib.WebSocket.EVENT.WS_EVENT_LISTENER;
import com.rumisystem.rumi_java_lib.WebSocket.WebSocketSERVER;

public class Main {
	public static void main(String[] args) {
		WebSocketSERVER WSS = new WebSocketSERVER();

		WSS.SET_EVENT_VOID(new CONNECT_EVENT_LISTENER() {
			@Override
			public void CONNECT_EVENT(CONNECT_EVENT SESSION) {
				System.out.println("新しい接続");
				SESSION.SET_EVENT_LISTENER(new WS_EVENT_LISTENER() {
					@Override
					public void MESSAGE(MESSAGE_EVENT E) {
						System.out.println(E.getMessage());

						E.SEND_MESSAGE("あいうえおおえおえお");
					}

					@Override
					public void CLOSE(CLOSE_EVENT E) {
						System.out.println("切断された");
					}

					@Override
					public void EXCEPTION(Exception EX){
						EX.printStackTrace();
					}
				});
			}
		});

		WSS.START(8000);
	}
}

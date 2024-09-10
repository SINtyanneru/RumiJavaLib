package com.rumisystem.rumi_java_lib;

import com.rumisystem.rumi_java_lib.Socket.Server.EVENT.CONNECT_EVENT;
import com.rumisystem.rumi_java_lib.Socket.Server.EVENT.EVENT_LISTENER;
import com.rumisystem.rumi_java_lib.Socket.Server.EVENT.MESSAGE_EVENT;
import com.rumisystem.rumi_java_lib.Socket.Server.SocketSERVER;

public class Main {
	public static void main(String[] args) {
		try {
			SocketSERVER SS = new SocketSERVER();
			SS.SET_EVENT_LISTENER(new EVENT_LISTENER() {
				@Override
				public void CONNECT(CONNECT_EVENT E) {
					System.out.println("つながった");
					E.SendMESSAGE("Welcomemmmmm");
				}

				@Override
				public void MESSAGE(MESSAGE_EVENT E) {
					System.out.println("メッセージ受信" + E.getMessage());
					E.SendMESSAGE("hej");
				}
			});

			SS.START(2300);
		} catch (Exception EX) {
			EX.printStackTrace();
		}
	}
}

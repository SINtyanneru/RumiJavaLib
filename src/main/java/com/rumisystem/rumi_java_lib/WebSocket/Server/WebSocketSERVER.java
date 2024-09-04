package com.rumisystem.rumi_java_lib.WebSocket.Server;

import com.rumisystem.rumi_java_lib.WebSocket.Server.CONNECT_EVENT.CONNECT_EVENT_LISTENER;

import javax.swing.event.EventListenerList;
import java.net.InetSocketAddress;
import org.java_websocket.WebSocket;
import java.util.HashMap;

public class WebSocketSERVER {
	public static EventListenerList CONNECT_EL_LIST = new EventListenerList();
	public static EventListenerList EL_LIST = new EventListenerList();

	public static HashMap<Integer, String> CEL_LIST = new HashMap<>();		//接続後のイベントリスナーのhashCodeとUUIDを保存
	public static HashMap<WebSocket, String> CLIENT_LIST = new HashMap<>();

	public void START(int PORT) {
		InetSocketAddress ADDRESS = new InetSocketAddress("0.0.0.0", PORT);
		WS_HS WS = new WS_HS(ADDRESS);
		WS.start();
	}

	//イベントリスナーの受信先を設定するやつ
	public void SET_EVENT_VOID(CONNECT_EVENT_LISTENER EVL){
		CONNECT_EL_LIST.add(CONNECT_EVENT_LISTENER.class, EVL);
	}
}

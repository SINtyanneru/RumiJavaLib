package com.rumisystem.rumi_java_lib.WebSocket.Client;

import com.rumisystem.rumi_java_lib.WebSocket.Client.EVENT.CLOSE_EVENT;
import com.rumisystem.rumi_java_lib.WebSocket.Client.EVENT.CONNECT_EVENT;
import com.rumisystem.rumi_java_lib.WebSocket.Client.EVENT.MESSAGE_EVENT;
import com.rumisystem.rumi_java_lib.WebSocket.Client.EVENT.WS_EVENT_LISTENER;
import com.rumisystem.rumi_java_lib.WebSocket.Server.CONNECT_EVENT.CONNECT_EVENT_LISTENER;
import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;

import java.net.URI;

import static com.rumisystem.rumi_java_lib.WebSocket.Client.WebSocketClient.EL_LIST;

public class WS_HS extends WebSocketClient {
	public WS_HS(URI SERVER_URI) {
		super(SERVER_URI);
	}

	@Override
	public void onOpen(ServerHandshake HS) {
		//接続

		//イベント着火
		WS_EVENT_LISTENER[] LISTENER_LIST = EL_LIST.getListeners(WS_EVENT_LISTENER.class);
		for(WS_EVENT_LISTENER LISTENER:LISTENER_LIST){
			LISTENER.CONNECT(new CONNECT_EVENT(this));
		}
	}

	@Override
	public void onMessage(String MSG) {
		//受信

		//イベント着火
		WS_EVENT_LISTENER[] LISTENER_LIST = EL_LIST.getListeners(WS_EVENT_LISTENER.class);
		for(WS_EVENT_LISTENER LISTENER:LISTENER_LIST){
			LISTENER.MESSAGE(new MESSAGE_EVENT(this, MSG));
		}
	}

	@Override
	public void onClose(int CODE, String REASON, boolean REMOTE) {
		//切断
		//イベント着火
		WS_EVENT_LISTENER[] LISTENER_LIST = EL_LIST.getListeners(WS_EVENT_LISTENER.class);
		for(WS_EVENT_LISTENER LISTENER:LISTENER_LIST){
			LISTENER.CLOSE(new CLOSE_EVENT(REASON, CODE));
		}
	}

	@Override
	public void onError(Exception EX) {
		//エラー
		//イベント着火
		WS_EVENT_LISTENER[] LISTENER_LIST = EL_LIST.getListeners(WS_EVENT_LISTENER.class);
		for(WS_EVENT_LISTENER LISTENER:LISTENER_LIST){
			LISTENER.EXCEPTION(EX);
		}
	}
}

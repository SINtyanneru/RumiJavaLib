package com.rumisystem.rumi_java_lib.WebSocket;

import com.rumisystem.rumi_java_lib.WebSocket.CONNECT_EVENT.CONNECT_EVENT;
import com.rumisystem.rumi_java_lib.WebSocket.CONNECT_EVENT.CONNECT_EVENT_LISTENER;
import com.rumisystem.rumi_java_lib.WebSocket.EVENT.CLOSE_EVENT;
import com.rumisystem.rumi_java_lib.WebSocket.EVENT.MESSAGE_EVENT;
import com.rumisystem.rumi_java_lib.WebSocket.EVENT.WS_EVENT_LISTENER;
import org.java_websocket.WebSocket;
import org.java_websocket.handshake.ClientHandshake;
import org.java_websocket.server.WebSocketServer;

import java.net.InetSocketAddress;
import java.util.UUID;

import static com.rumisystem.rumi_java_lib.WebSocket.WebSocketSERVER.*;

public class WS_HS extends WebSocketServer {
	public WS_HS(InetSocketAddress ADDRESS) {
		super(ADDRESS);
	}

	@Override
	public void onOpen(WebSocket CON, ClientHandshake HS) {
		String ID = UUID.randomUUID().toString();
		//クライアント一覧にセット
		CLIENT_LIST.put(CON, ID);

		//イベント着火
		CONNECT_EVENT_LISTENER[] LISTENER_LIST = CONNECT_EL_LIST.getListeners(CONNECT_EVENT_LISTENER.class);
		for(CONNECT_EVENT_LISTENER LISTENER:LISTENER_LIST){
			LISTENER.CONNECT_EVENT(new CONNECT_EVENT(CON.getRemoteSocketAddress().toString(), ID));
		}
	}

	@Override
	public void onClose(WebSocket CON, int CODE, String REASON, boolean REMOTE) {
		//イベント着火
		WS_EVENT_LISTENER[] LISTENER_LIST = EL_LIST.getListeners(WS_EVENT_LISTENER.class);
		for(WS_EVENT_LISTENER LISTENER:LISTENER_LIST){
			String CEL_TO_ID = CEL_LIST.get(LISTENER.hashCode());
			String CON_TO_ID = CLIENT_LIST.get(CON);
			//Nullチェック
			if(CEL_TO_ID != null && CON_TO_ID != null){
				//両リストのIDが一致している事を確認
				if(CEL_TO_ID.equals(CON_TO_ID)){
					//切断イベント発火
					LISTENER.CLOSE(new CLOSE_EVENT(REASON, CODE));

					//両リストから削除
					CEL_LIST.remove(LISTENER.hashCode());
					CLIENT_LIST.remove(CON);
				}
			}
		}
	}

	@Override
	public void onMessage(WebSocket CON, String MSG) {
		//イベント着火
		WS_EVENT_LISTENER[] LISTENER_LIST = EL_LIST.getListeners(WS_EVENT_LISTENER.class);
		for(WS_EVENT_LISTENER LISTENER:LISTENER_LIST){
			String CEL_TO_ID = CEL_LIST.get(LISTENER.hashCode());
			String CON_TO_ID = CLIENT_LIST.get(CON);
			//Nullチェック
			if(CEL_TO_ID != null && CON_TO_ID != null){
				//両リストのIDが一致している事を確認
				if(CEL_TO_ID.equals(CON_TO_ID)){
					//イベント発火
					LISTENER.MESSAGE(new MESSAGE_EVENT(MSG, CON));
				}
			}
		}
	}

	@Override
	public void onError(WebSocket CON, Exception EX) {
		//イベント着火
		WS_EVENT_LISTENER[] LISTENER_LIST = EL_LIST.getListeners(WS_EVENT_LISTENER.class);
		for(WS_EVENT_LISTENER LISTENER:LISTENER_LIST){
			String CEL_TO_ID = CEL_LIST.get(LISTENER.hashCode());
			String CON_TO_ID = CLIENT_LIST.get(CON);
			//Nullチェック
			if(CEL_TO_ID != null && CON_TO_ID != null){
				//両リストのIDが一致している事を確認
				if(CEL_TO_ID.equals(CON_TO_ID)){
					//イベント発火
					LISTENER.EXCEPTION(EX);
				}
			}
		}
	}

	@Override
	public void onStart() {
		//使う予定無いわ
	}
}

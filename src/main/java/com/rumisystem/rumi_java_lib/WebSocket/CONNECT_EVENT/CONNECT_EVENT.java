package com.rumisystem.rumi_java_lib.WebSocket.CONNECT_EVENT;

import com.rumisystem.rumi_java_lib.WebSocket.EVENT.WS_EVENT_LISTENER;
import static com.rumisystem.rumi_java_lib.WebSocket.WebSocketSERVER.EL_LIST;
import static com.rumisystem.rumi_java_lib.WebSocket.WebSocketSERVER.CEL_LIST;

public class CONNECT_EVENT {
	private String IP = null;
	private String ID = null;

	public CONNECT_EVENT(String IP, String ID) {
		this.IP = IP;
		this.ID = ID;
	}

	public void SET_EVENT_LISTENER(WS_EVENT_LISTENER WEL){
		EL_LIST.add(WS_EVENT_LISTENER.class, WEL);
		CEL_LIST.put(WEL.hashCode(), ID);
	}
}

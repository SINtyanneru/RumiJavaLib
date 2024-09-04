package com.rumisystem.rumi_java_lib.WebSocket.Client.EVENT;

import com.rumisystem.rumi_java_lib.WebSocket.Client.WS_HS;

public class CONNECT_EVENT {
	private WS_HS HS = null;

	public CONNECT_EVENT(WS_HS HS) {
		this.HS = HS;
	}

	public void SEND(String TEXT) {
		HS.send(TEXT);
	}
}

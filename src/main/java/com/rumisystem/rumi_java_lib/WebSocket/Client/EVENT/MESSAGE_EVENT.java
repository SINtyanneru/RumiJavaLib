package com.rumisystem.rumi_java_lib.WebSocket.Client.EVENT;

import com.rumisystem.rumi_java_lib.WebSocket.Client.WS_HS;

public class MESSAGE_EVENT {
	private WS_HS HS = null;
	private String TEXT = null;

	public MESSAGE_EVENT(WS_HS HS, String TEXT) {
		this.HS = HS;
		this.TEXT = TEXT;
	}

	public void SEND(String TEXT) {
		HS.send(TEXT);
	}

	public String getMessage() {
		return TEXT;
	}
}

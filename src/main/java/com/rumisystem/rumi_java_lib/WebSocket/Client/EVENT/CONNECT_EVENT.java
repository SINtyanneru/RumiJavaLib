package com.rumisystem.rumi_java_lib.WebSocket.Client.EVENT;

import okhttp3.WebSocket;
import okio.ByteString;

public class CONNECT_EVENT {
	private WebSocket SESSION;

	public CONNECT_EVENT(WebSocket SESSION) {
		this.SESSION = SESSION;
	}

	public void SEND(String TEXT) {
		SESSION.send(TEXT);
	}

	public void SEND(byte[] BYTES) {
		SESSION.send(ByteString.of(BYTES));
	}
}

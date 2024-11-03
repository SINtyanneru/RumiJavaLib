package com.rumisystem.rumi_java_lib.WebSocket.Client.EVENT;

import java.nio.charset.StandardCharsets;
import java.util.Base64;

import static com.rumisystem.rumi_java_lib.WebSocket.Client.WebSocketClient.RunCMD;

public class CONNECT_EVENT {
	public CONNECT_EVENT() {
	}

	public void SEND(String TEXT) {
		RunCMD("SEND " + Base64.getEncoder().encodeToString(TEXT.getBytes(StandardCharsets.UTF_8)));
	}
}

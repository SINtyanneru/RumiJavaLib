package com.rumisystem.rumi_java_lib.WebSocket.Client.EVENT;

import java.nio.charset.StandardCharsets;
import java.util.Base64;

import static com.rumisystem.rumi_java_lib.WebSocket.Client.WebSocketClient.RunCMD;

public class MESSAGE_EVENT {
	private String TEXT = null;

	public MESSAGE_EVENT(String TEXT) {
		this.TEXT = TEXT;
	}

	public void SEND(String TEXT) {
		RunCMD("SEND" + Base64.getEncoder().encodeToString(TEXT.getBytes(StandardCharsets.UTF_8)));
	}

	public String getMessage() {
		return TEXT;
	}
}

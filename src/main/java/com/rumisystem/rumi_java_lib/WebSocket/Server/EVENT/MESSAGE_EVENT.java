package com.rumisystem.rumi_java_lib.WebSocket.Server.EVENT;

import org.java_websocket.WebSocket;

public class MESSAGE_EVENT {
	private String MESSAGE = null;
	private WebSocket CON = null;

	public MESSAGE_EVENT(String MESSAGE, WebSocket CON){
		this.MESSAGE = MESSAGE;
		this.CON = CON;
	}

	public String getMessage(){
		return MESSAGE;
	}

	public void SEND_MESSAGE(String TEXT){
		CON.send(TEXT);
	}
}

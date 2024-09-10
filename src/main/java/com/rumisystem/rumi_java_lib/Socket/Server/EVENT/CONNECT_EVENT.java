package com.rumisystem.rumi_java_lib.Socket.Server.EVENT;

import java.net.Socket;

import static com.rumisystem.rumi_java_lib.Socket.Server.SocketSERVER.BR_LIST;
import static com.rumisystem.rumi_java_lib.Socket.Server.SocketSERVER.BW_LIST;

public class CONNECT_EVENT {
	private String ID;
	private Socket SESSION;

	public CONNECT_EVENT(String ID, Socket SESSION) {
		this.ID = ID;
		this.SESSION = SESSION;
	}

	public String getID() {
		return ID;
	}

	public Socket getSESSION() {
		return SESSION;
	}

	public void SendMESSAGE(String TEXT) {
		if (BW_LIST.get(ID) != null) {
			BW_LIST.get(ID).print(TEXT + "\n");
			BW_LIST.get(ID).flush();
		} else {
			throw new Error("BWを取得できませんでした");
		}
	}

	public void SendSMTPMESSAGE(String TEXT) {
		if (BW_LIST.get(ID) != null) {
			BW_LIST.get(ID).print(TEXT + "\r\n");
			BW_LIST.get(ID).flush();
		} else {
			throw new Error("BWを取得できませんでした");
		}
	}
}

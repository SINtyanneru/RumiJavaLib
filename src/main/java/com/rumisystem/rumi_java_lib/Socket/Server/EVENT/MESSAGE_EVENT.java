package com.rumisystem.rumi_java_lib.Socket.Server.EVENT;

import static com.rumisystem.rumi_java_lib.Socket.Server.SocketSERVER.BW_LIST;

public class MESSAGE_EVENT {
	private String ID;
	private String MESSAGE;

	public MESSAGE_EVENT(String ID, String MESSAGE) {
		this.ID = ID;
		this.MESSAGE = MESSAGE;
	}

	public String getMessage() {
		return MESSAGE;
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

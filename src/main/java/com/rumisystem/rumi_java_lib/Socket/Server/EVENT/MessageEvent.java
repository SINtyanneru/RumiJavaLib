package com.rumisystem.rumi_java_lib.Socket.Server.EVENT;

public class MessageEvent {
	private byte[] DATA;

	public MessageEvent(byte[] DATA) {
		this.DATA = DATA;
	}

	public byte[] getByte() {
		return DATA;
	}

	public String getString() {
		return new String(DATA);
	}
}

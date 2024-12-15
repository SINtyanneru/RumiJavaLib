package com.rumisystem.rumi_java_lib.Socket.Server.EVENT;

public class ReceiveEvent {
	private byte[] DATA;

	public ReceiveEvent(byte[] DATA) {
		this.DATA = DATA;
	}

	public byte[] getByte() {
		return DATA;
	}

	public String getString() {
		return new String(DATA);
	}
}

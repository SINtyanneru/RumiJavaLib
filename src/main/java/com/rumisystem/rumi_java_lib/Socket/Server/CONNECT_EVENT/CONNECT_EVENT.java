package com.rumisystem.rumi_java_lib.Socket.Server.CONNECT_EVENT;

import com.rumisystem.rumi_java_lib.Socket.Server.EVENT.EVENT_LISTENER;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;

import static com.rumisystem.rumi_java_lib.Socket.Server.SocketServer.CEL_LIST;
import static com.rumisystem.rumi_java_lib.Socket.Server.SocketServer.EL_LIST;

public class CONNECT_EVENT {
	private String ID = "";
	private SocketChannel SES = null;

	public CONNECT_EVENT(String ID, SocketChannel SES) {
		this.ID = ID;
		this.SES = SES;
	}

	public void setEventListener(EVENT_LISTENER EL) {
		EL_LIST.add(EVENT_LISTENER.class, EL);
		CEL_LIST.put(EL.hashCode(), ID);
	}

	public void sendMessage(String MSG) throws IOException {
		SES.write(ByteBuffer.wrap(MSG.getBytes()));
	}
}

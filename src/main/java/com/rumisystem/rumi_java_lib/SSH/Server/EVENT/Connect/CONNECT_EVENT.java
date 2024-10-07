package com.rumisystem.rumi_java_lib.SSH.Server.EVENT.Connect;

import com.rumisystem.rumi_java_lib.SSH.Server.EVENT.EVENT_LISTENER;
import org.apache.sshd.server.channel.ChannelSession;

import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.util.concurrent.CompletableFuture;
import java.util.function.Supplier;

import static com.rumisystem.rumi_java_lib.SSH.Server.SSHServer.EL_LIST;
import static com.rumisystem.rumi_java_lib.SSH.Server.SSHServer.SESSION_LIST;

public class CONNECT_EVENT {
	private String ID;
	public CONNECT_EVENT(String ID) {
		this.ID = ID;
	}

	public void SET_EVENT_LISTENER(EVENT_LISTENER EL) {
		EL_LIST.add(EVENT_LISTENER.class, EL);
		SESSION_LIST.get(ID).put("ELL", EL.hashCode());
	}

	public void Send(String TEXT) {
		PrintStream BW = (PrintStream) SESSION_LIST.get(ID).get("BW");
		BW.print(TEXT);
		BW.flush();
	}

	public void Close() throws IOException {
		ChannelSession CS = (ChannelSession) SESSION_LIST.get(ID).get("SESSION");
		CS.getSession().close();
	}
}

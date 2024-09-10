package com.rumisystem.rumi_java_lib.Socket.Server.EVENT;

import java.util.EventListener;

public interface EVENT_LISTENER extends EventListener {
	public void CONNECT(CONNECT_EVENT E);
	public void MESSAGE(MESSAGE_EVENT E);
}

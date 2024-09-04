package com.rumisystem.rumi_java_lib.WebSocket.Server.EVENT;

import java.util.EventListener;

public interface WS_EVENT_LISTENER extends EventListener {
	public void MESSAGE(MESSAGE_EVENT E);
	public void CLOSE(CLOSE_EVENT E);
	public void EXCEPTION(Exception EX);
}

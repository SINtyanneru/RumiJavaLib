package com.rumisystem.rumi_java_lib.Socket.Server.EVENT;

import java.util.EventListener;

public interface EVENT_LISTENER extends EventListener {
	public void Message(MessageEvent E);
	public void Receive(ReceiveEvent E);
	public void Close(CloseEvent E);
}

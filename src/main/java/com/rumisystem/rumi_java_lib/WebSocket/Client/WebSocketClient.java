package com.rumisystem.rumi_java_lib.WebSocket.Client;

import com.rumisystem.rumi_java_lib.WebSocket.Client.EVENT.WS_EVENT_LISTENER;

import javax.swing.event.EventListenerList;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.EventListener;

public class WebSocketClient {
	public static EventListenerList EL_LIST = new EventListenerList();

	public void CONNECT(String URL) throws URISyntaxException {
		WS_HS CLIENT = new WS_HS(new URI(URL));
		CLIENT.connect();
	}

	public void SET_EVENT_LISTENER(WS_EVENT_LISTENER EVENT_LISTENER) {
		EL_LIST.add(WS_EVENT_LISTENER.class, EVENT_LISTENER);
	}
}

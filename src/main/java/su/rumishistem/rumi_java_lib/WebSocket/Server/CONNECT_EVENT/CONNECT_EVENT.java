package su.rumishistem.rumi_java_lib.WebSocket.Server.CONNECT_EVENT;

import su.rumishistem.rumi_java_lib.WebSocket.Server.EVENT.WS_EVENT_LISTENER;

import java.nio.charset.StandardCharsets;
import java.util.Base64;

import static su.rumishistem.rumi_java_lib.WebSocket.Server.WebSocketSERVER.*;

public class CONNECT_EVENT {
	private String IP = null;
	private String ID = null;

	public CONNECT_EVENT(String IP, String ID) {
		this.IP = IP;
		this.ID = ID;
	}

	public String getIP() {
		return IP;
	}

	public void SET_EVENT_LISTENER(WS_EVENT_LISTENER WEL){
		EL_LIST.add(WS_EVENT_LISTENER.class, WEL);
		CEL_LIST.put(WEL.hashCode(), ID);
	}

	public void SendMessage(String TEXT) {
		try {
			if (SESSION_LIST.get(ID) != null) {
				SESSION_LIST.get(ID).sendMessage(Base64.getEncoder().encodeToString((TEXT + "\r\n").getBytes()));
			}
		} catch (Exception EX) {
			//もみ消す
		}
	}
}

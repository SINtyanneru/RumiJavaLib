package su.rumishistem.rumi_java_lib.WebSocket.Server.CONNECT_EVENT;

import io.netty.buffer.Unpooled;
import io.netty.handler.codec.http.websocketx.BinaryWebSocketFrame;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import su.rumishistem.rumi_java_lib.WebSocket.Server.EVENT.WS_EVENT_LISTENER;

import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.HashMap;

import static su.rumishistem.rumi_java_lib.WebSocket.Server.WebSocketSERVER.*;

public class CONNECT_EVENT {
	private String IP = null;
	private String ID = null;
	private HashMap<String, String> uri_param = null;

	public CONNECT_EVENT(String IP, String ID, HashMap<String, String> uri_param) {
		this.IP = IP;
		this.ID = ID;
		this.uri_param = uri_param;
	}

	public String getIP() {
		return IP;
	}

	public String get_uri_param(String key) {
		return uri_param.get(key);
	}

	public void SET_EVENT_LISTENER(WS_EVENT_LISTENER WEL){
		EL_LIST.add(WS_EVENT_LISTENER.class, WEL);
		CEL_LIST.put(WEL.hashCode(), ID);
	}

	public void SendMessage(String TEXT) {
		try {
			if (SESSION_LIST.get(ID) != null) {
				SESSION_LIST.get(ID).channel().writeAndFlush(new TextWebSocketFrame(TEXT));
			}
		} catch (Exception EX) {
			//もみ消す
		}
	}

	public void SendByte(byte[] b) {
		try {
			if (SESSION_LIST.get(ID) != null) {
				SESSION_LIST.get(ID).channel().writeAndFlush(new BinaryWebSocketFrame(Unpooled.wrappedBuffer(b)));
			}
		} catch (Exception ex) {
			//もみ消す
		}
	}
}

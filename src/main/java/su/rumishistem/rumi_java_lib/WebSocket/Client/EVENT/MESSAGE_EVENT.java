package su.rumishistem.rumi_java_lib.WebSocket.Client.EVENT;

import okhttp3.WebSocket;
import okio.ByteString;

import java.nio.charset.StandardCharsets;

public class MESSAGE_EVENT {
	private byte[] DATA;
	private WebSocket SESSION;

	public MESSAGE_EVENT(byte[] DATA, WebSocket SESSION) {
		this.DATA = DATA;
		this.SESSION = SESSION;
	}

	public void SEND(String TEXT) {
		SESSION.send(TEXT);
	}

	public void SEND(byte[] BYTES) {
		SESSION.send(ByteString.of(BYTES));
	}

	public String getMessage() {
		return new String(DATA, StandardCharsets.UTF_8);
	}
}

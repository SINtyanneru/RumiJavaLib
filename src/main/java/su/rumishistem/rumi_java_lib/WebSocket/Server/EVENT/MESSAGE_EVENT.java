package su.rumishistem.rumi_java_lib.WebSocket.Server.EVENT;

import kotlin.text.Charsets;

import java.nio.charset.StandardCharsets;
import java.util.Base64;

public class MESSAGE_EVENT {
	private byte[] data = null;

	public MESSAGE_EVENT(byte[] data){
		this.data = data;
	}

	public byte[] getData() {
		return data;
	}

	public String getMessage(){
		return new String(data, Charsets.UTF_8);
	}
}

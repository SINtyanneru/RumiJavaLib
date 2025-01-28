package su.rumishistem.rumi_java_lib.WebSocket.Client;

public class HEADER_TYPE {
	private String KEY;
	private String VAL;

	public HEADER_TYPE(String KEY, String VAL) {
		this.KEY = KEY;
		this.VAL = VAL;
	}

	public String GetKEY() {
		return KEY;
	}

	public String GetVAL() {
		return VAL;
	}
}

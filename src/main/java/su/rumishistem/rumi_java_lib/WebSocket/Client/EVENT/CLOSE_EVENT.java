package su.rumishistem.rumi_java_lib.WebSocket.Client.EVENT;

public class CLOSE_EVENT {
	private String REASON = null;
	private int CODE = 0;

	public CLOSE_EVENT(String REASON, int CODE) {
		this.REASON = REASON;
		this.CODE = CODE;
	}

	public String getReason() {
		return REASON;
	}

	public int getCode() {
		return CODE;
	}
}

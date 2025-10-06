package su.rumishistem.rumi_java_lib.WebSocket.Client.EVENT;

/**
 * WebSocketの切断イベント
 */
public class CLOSE_EVENT {
	private String REASON = null;
	private int CODE = 0;

	/**
	 * イベント
	 * @param REASON 理由
	 * @param CODE コード
	 */
	public CLOSE_EVENT(String REASON, int CODE) {
		this.REASON = REASON;
		this.CODE = CODE;
	}

	/**
	 * 理由を取得します
	 * @return 理由
	 */
	public String getReason() {
		return REASON;
	}

	/**
	 * コードを取得します
	 * @return コード
	 */
	public int getCode() {
		return CODE;
	}
}

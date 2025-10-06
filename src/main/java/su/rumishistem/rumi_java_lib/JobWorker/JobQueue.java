package su.rumishistem.rumi_java_lib.JobWorker;

import su.rumishistem.rumi_java_lib.ExceptionRunnable;

/**
 * ジョブの本体
 */
public class JobQueue {
	private String id;
	private String name;
	private ExceptionRunnable task;
	private boolean retry = false;
	private int retry_count = 0;
	private Exception ex;

	/**
	 * ジョブ本体
	 * @param id ID
	 * @param name 名前
	 * @param task タスク
	 * @param retry 失敗時に再チャレンジするか？
	 */
	public JobQueue(String id, String name, ExceptionRunnable task, boolean retry) {
		this.id = id;
		this.name = name;
		this.task = task;
		this.retry = retry;
	}

	/**
	 * ジョブのIDを取得します
	 * @return ID
	 */
	public String get_id() {
		return id;
	}

	/**
	 * ジョブの名前を取得します
	 * @return 名前
	 */
	public String get_name() {
		return name;
	}

	/**
	 * ジョブを実行します
	 * @throws Exception エラー
	 */
	public void start_task() throws Exception {
		task.run();
	}

	/**
	 * 失敗時に再チャレンジするか確認します
	 * @return ?
	 */
	public boolean is_retry() {
		return retry;
	}

	/**
	 * 今までにリトライした数を取得します
	 * @return 回数
	 */
	public int get_retry_count() {
		return retry_count;
	}

	/**
	 * 失敗回数のインクリメント
	 */
	protected void add_retry_count() {
		retry_count += 1;
	}

	/**
	 * エラーを取得します
	 * @return エラー
	 */
	public Exception get_exception() {
		return ex;
	}

	/**
	 * エラーをセットします
	 * @param ex エラー
	 */
	protected void set_exception(Exception ex) {
		this.ex = ex;
	}
}

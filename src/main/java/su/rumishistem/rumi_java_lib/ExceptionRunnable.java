package su.rumishistem.rumi_java_lib;

/**
 * Exception付きのRunnable
 */
public interface ExceptionRunnable {
	/**
	 * Runnableと一緒
	 * @throws Exception エラー
	 */
	void run() throws Exception;
}

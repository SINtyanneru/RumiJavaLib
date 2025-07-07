package su.rumishistem.rumi_java_lib;

import java.util.concurrent.CountDownLatch;

/**
 * 〜RustのOptionを丸パクリしてNullPointerExceptionをボコボコにしよう祭り第1回〜
 * (使いやすさ目的でResultも融合した)
 * @param <T> 型
 */
public class Option<T> {
	private boolean SomeFlag;
	private T Value = null;
	private Exception EX = null;

	/**
	 * 実行するやつ
	 * @param <T>
	 */
	public interface OptionRunnable<T> {
		void Some(T Value);
		void None(Exception EX);
	}

	/**
	 * 正常終了
	 * @param Value 返り値
	 */
	public Option(T Value) {
		if (Value == null) throw  new RuntimeException("SomeにNullを入れるな");
		this.Value = Value;
		this.SomeFlag = true;
	}

	/**
	 * 異常終了
	 * @param EX エラー内容
	 */
	public Option(Exception EX) {
		this.EX = EX;
	}

	/**
	 * 値があるか？
	 * @return
	 */
	public boolean isSome() {
		return SomeFlag;
	}

	/**
	 * 値が無いか？
	 * @return
	 */
	public boolean isNone() {
		return !SomeFlag;
	}

	/**
	 * Rustのmatchのパクリ
	 * @param Run
	 */
	public void match(OptionRunnable<T> Run) {
		if (Run == null) return;

		if (isSome()) {
			Run.Some(Value);
		} else {
			if (EX != null) {
				Run.None(EX);
			} else {
				Run.None(new RuntimeException("Exceptionは指定されていません"));
			}
		}
	}
}

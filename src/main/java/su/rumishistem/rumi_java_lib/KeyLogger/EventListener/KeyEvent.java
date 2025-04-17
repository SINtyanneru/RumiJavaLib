package su.rumishistem.rumi_java_lib.KeyLogger.EventListener;

public class KeyEvent {
	private int KeyCode = 0;

	public KeyEvent(int KeyCode) {
		this.KeyCode = KeyCode;
	}

	public int GetKeyCode() {
		return KeyCode;
	}
}

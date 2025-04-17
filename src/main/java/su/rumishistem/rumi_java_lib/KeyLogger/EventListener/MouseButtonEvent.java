package su.rumishistem.rumi_java_lib.KeyLogger.EventListener;

import su.rumishistem.rumi_java_lib.KeyLogger.Type.MouseButton;

public class MouseButtonEvent {
	private int X;
	private int Y;
	private MouseButton Button;

	public MouseButtonEvent(int X, int Y, MouseButton Button) {
		this.X = X;
		this.Y = Y;
		this.Button = Button;
	}

	public int GetX() {
		return X;
	}

	public int GetY() {
		return Y;
	}

	public MouseButton GetButton() {
		return Button;
	}
}

package su.rumishistem.rumi_java_lib.KeyLogger.EventListener;

import java.util.EventListener;

public interface KeyLoggerEventListener extends EventListener {
	default void KeyDown(KeyEvent e) {}
	default void KeyUp(KeyEvent e) {}
	default void KeyType(KeyEvent e) {}

	default void MouseClick(MouseButtonEvent e) {}
	default void MouseDown(MouseButtonEvent e) {}
	default void MouseUp(MouseButtonEvent e) {}

	default void MouseMove(MouseEvent e) {}
	default void MouseDrag(MouseEvent e) {}
}

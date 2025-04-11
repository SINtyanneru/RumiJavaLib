package su.rumishistem.rumi_java_lib.KeyLogger.EventListener;

import java.util.EventListener;

//TODO:イベントにeが必要

public interface KeyLoggerEventListener extends EventListener {
	default void KeyDown() {}
	default void KeyUp() {}
	default void KeyType() {}

	default void MouseClick() {}
	default void MouseDown() {}
	default void MouseUp() {}

	default void MouseMove() {}
	default void MouseDrag() {}
}

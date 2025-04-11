package su.rumishistem.rumi_java_lib.KeyLogger.Listener;

import com.github.kwhat.jnativehook.keyboard.NativeKeyEvent;
import com.github.kwhat.jnativehook.keyboard.NativeKeyListener;
import su.rumishistem.rumi_java_lib.KeyLogger.EventListener.KeyLoggerEventListener;

import javax.swing.event.EventListenerList;
import java.util.HashMap;

public class KeyLoggerListener implements NativeKeyListener {
	private EventListenerList ELL = null;

	public KeyLoggerListener(EventListenerList ELL) {
		this.ELL = ELL;
	}

	@Override
	public void nativeKeyPressed(NativeKeyEvent e) {
		for (KeyLoggerEventListener EL:ELL.getListeners(KeyLoggerEventListener.class)) {
			EL.KeyDown();
		}
	}

	@Override
	public void nativeKeyReleased(NativeKeyEvent e) {
		for (KeyLoggerEventListener EL:ELL.getListeners(KeyLoggerEventListener.class)) {
			EL.MouseUp();
		}
	}

	@Override
	public void nativeKeyTyped(NativeKeyEvent e) {
		for (KeyLoggerEventListener EL:ELL.getListeners(KeyLoggerEventListener.class)) {
			EL.KeyType();
		}
	}
}

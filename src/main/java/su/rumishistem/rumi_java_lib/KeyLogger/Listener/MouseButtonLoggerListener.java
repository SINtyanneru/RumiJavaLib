package su.rumishistem.rumi_java_lib.KeyLogger.Listener;

import com.github.kwhat.jnativehook.mouse.NativeMouseEvent;
import com.github.kwhat.jnativehook.mouse.NativeMouseListener;
import su.rumishistem.rumi_java_lib.KeyLogger.EventListener.KeyLoggerEventListener;

import javax.swing.event.EventListenerList;

public class MouseButtonLoggerListener implements NativeMouseListener {
	private EventListenerList ELL = null;

	public MouseButtonLoggerListener(EventListenerList ELL) {
		this.ELL = ELL;
	}

	@Override
	public void nativeMouseClicked(NativeMouseEvent e) {
		for (KeyLoggerEventListener EL:ELL.getListeners(KeyLoggerEventListener.class)) {
			EL.MouseClick();
		}
	}

	@Override
	public void nativeMousePressed(NativeMouseEvent e) {
		for (KeyLoggerEventListener EL:ELL.getListeners(KeyLoggerEventListener.class)) {
			EL.MouseDown();
		}
	}

	@Override
	public void nativeMouseReleased(NativeMouseEvent e) {
		for (KeyLoggerEventListener EL:ELL.getListeners(KeyLoggerEventListener.class)) {
			EL.MouseUp();
		}
	}
}

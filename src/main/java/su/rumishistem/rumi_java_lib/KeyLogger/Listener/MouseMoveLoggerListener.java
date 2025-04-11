package su.rumishistem.rumi_java_lib.KeyLogger.Listener;

import com.github.kwhat.jnativehook.mouse.NativeMouseEvent;
import com.github.kwhat.jnativehook.mouse.NativeMouseMotionListener;
import su.rumishistem.rumi_java_lib.KeyLogger.EventListener.KeyLoggerEventListener;

import javax.swing.event.EventListenerList;

public class MouseMoveLoggerListener implements NativeMouseMotionListener {
	private EventListenerList ELL = null;

	public MouseMoveLoggerListener(EventListenerList ELL) {
		this.ELL = ELL;
	}

	@Override
	public void nativeMouseMoved(NativeMouseEvent e) {
		for (KeyLoggerEventListener EL:ELL.getListeners(KeyLoggerEventListener.class)) {
			EL.MouseMove();
		}
	}

	@Override
	public void nativeMouseDragged(NativeMouseEvent e) {
		for (KeyLoggerEventListener EL:ELL.getListeners(KeyLoggerEventListener.class)) {
			EL.MouseDrag();
		}
	}
}

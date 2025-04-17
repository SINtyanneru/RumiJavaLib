package su.rumishistem.rumi_java_lib.KeyLogger.Listener;

import com.github.kwhat.jnativehook.mouse.NativeMouseEvent;
import com.github.kwhat.jnativehook.mouse.NativeMouseMotionListener;
import su.rumishistem.rumi_java_lib.KeyLogger.EventListener.KeyLoggerEventListener;
import su.rumishistem.rumi_java_lib.KeyLogger.EventListener.MouseEvent;
import su.rumishistem.rumi_java_lib.KeyLogger.Type.MouseButton;

import javax.swing.event.EventListenerList;

public class MouseMoveLoggerListener implements NativeMouseMotionListener {
	private EventListenerList ELL = null;

	public MouseMoveLoggerListener(EventListenerList ELL) {
		this.ELL = ELL;
	}

	@Override
	public void nativeMouseMoved(NativeMouseEvent e) {
		for (KeyLoggerEventListener EL:ELL.getListeners(KeyLoggerEventListener.class)) {
			EL.MouseMove(new MouseEvent(e.getX(), e.getY(), MouseButton.values()[e.getButton()]));
		}
	}

	@Override
	public void nativeMouseDragged(NativeMouseEvent e) {
		for (KeyLoggerEventListener EL:ELL.getListeners(KeyLoggerEventListener.class)) {
			EL.MouseDrag(new MouseEvent(e.getX(), e.getY(), MouseButton.values()[e.getButton()]));
		}
	}
}

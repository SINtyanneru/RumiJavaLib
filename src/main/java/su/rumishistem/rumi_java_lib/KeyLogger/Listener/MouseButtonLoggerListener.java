package su.rumishistem.rumi_java_lib.KeyLogger.Listener;

import com.github.kwhat.jnativehook.mouse.NativeMouseEvent;
import com.github.kwhat.jnativehook.mouse.NativeMouseListener;
import su.rumishistem.rumi_java_lib.KeyLogger.EventListener.KeyLoggerEventListener;
import su.rumishistem.rumi_java_lib.KeyLogger.EventListener.MouseButtonEvent;
import su.rumishistem.rumi_java_lib.KeyLogger.Type.MouseButton;

import javax.swing.event.EventListenerList;

public class MouseButtonLoggerListener implements NativeMouseListener {
	private EventListenerList ELL = null;

	public MouseButtonLoggerListener(EventListenerList ELL) {
		this.ELL = ELL;
	}

	@Override
	public void nativeMouseClicked(NativeMouseEvent e) {
		for (KeyLoggerEventListener EL:ELL.getListeners(KeyLoggerEventListener.class)) {
			EL.MouseClick(new MouseButtonEvent(e.getX(), e.getY(), MouseButton.values()[e.getButton()]));
		}
	}

	@Override
	public void nativeMousePressed(NativeMouseEvent e) {
		for (KeyLoggerEventListener EL:ELL.getListeners(KeyLoggerEventListener.class)) {
			EL.MouseDown(new MouseButtonEvent(e.getX(), e.getY(), MouseButton.values()[e.getButton()]));
		}
	}

	@Override
	public void nativeMouseReleased(NativeMouseEvent e) {
		for (KeyLoggerEventListener EL:ELL.getListeners(KeyLoggerEventListener.class)) {
			EL.MouseUp(new MouseButtonEvent(e.getX(), e.getY(), MouseButton.values()[e.getButton()]));
		}
	}
}

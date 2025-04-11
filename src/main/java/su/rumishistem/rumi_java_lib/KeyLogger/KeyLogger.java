package su.rumishistem.rumi_java_lib.KeyLogger;

import com.github.kwhat.jnativehook.GlobalScreen;
import com.github.kwhat.jnativehook.NativeHookException;
import su.rumishistem.rumi_java_lib.KeyLogger.EventListener.KeyLoggerEventListener;
import su.rumishistem.rumi_java_lib.KeyLogger.Listener.KeyLoggerListener;
import su.rumishistem.rumi_java_lib.KeyLogger.Listener.MouseButtonLoggerListener;
import su.rumishistem.rumi_java_lib.KeyLogger.Listener.MouseMoveLoggerListener;

import javax.swing.event.EventListenerList;
import java.util.logging.Level;
import java.util.logging.LogManager;
import java.util.logging.Logger;

public class KeyLogger {
	private EventListenerList ELL = new EventListenerList();

	public KeyLogger() throws NativeHookException {
		//ログ出力を無効化
		LogManager.getLogManager().reset();
		Logger LG = Logger.getLogger(GlobalScreen.class.getPackage().getName());
		LG.setLevel(Level.OFF);

		GlobalScreen.registerNativeHook();
		GlobalScreen.addNativeKeyListener(new KeyLoggerListener(ELL));
		GlobalScreen.addNativeMouseListener(new MouseButtonLoggerListener(ELL));
		GlobalScreen.addNativeMouseMotionListener(new MouseMoveLoggerListener(ELL));
	}

	public void AddEventListener(KeyLoggerEventListener EL) {
		ELL.add(KeyLoggerEventListener.class, EL);
	}
}

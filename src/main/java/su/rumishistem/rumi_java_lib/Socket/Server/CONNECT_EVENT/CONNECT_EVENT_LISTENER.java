package su.rumishistem.rumi_java_lib.Socket.Server.CONNECT_EVENT;

import java.io.IOException;
import java.util.EventListener;

public interface CONNECT_EVENT_LISTENER extends EventListener {
	public void CONNECT(CONNECT_EVENT SESSION) throws IOException;
}

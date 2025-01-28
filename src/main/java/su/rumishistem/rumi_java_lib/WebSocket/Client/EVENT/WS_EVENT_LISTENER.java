package su.rumishistem.rumi_java_lib.WebSocket.Client.EVENT;

import java.util.EventListener;

public interface WS_EVENT_LISTENER extends EventListener {
	public void CONNECT(CONNECT_EVENT E);
	public void MESSAGE(MESSAGE_EVENT E);
	public void CLOSE(CLOSE_EVENT E);
	public void EXCEPTION(Exception EX);
}

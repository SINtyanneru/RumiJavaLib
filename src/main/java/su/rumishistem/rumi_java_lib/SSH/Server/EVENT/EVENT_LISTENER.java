package su.rumishistem.rumi_java_lib.SSH.Server.EVENT;

import java.util.EventListener;

public interface EVENT_LISTENER extends EventListener {
	public void Send(SendEvent E);
	public void Close();
}

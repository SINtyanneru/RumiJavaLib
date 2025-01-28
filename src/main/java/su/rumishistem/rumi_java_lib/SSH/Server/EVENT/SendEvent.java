package su.rumishistem.rumi_java_lib.SSH.Server.EVENT;

import java.io.IOException;
import java.io.InputStream;

import static su.rumishistem.rumi_java_lib.SSH.Server.SSHServer.SESSION_LIST;

public class SendEvent {
	private String ID;
	private String TEXT;

	public SendEvent(String ID, String TEXT) {
		this.ID = ID;
		this.TEXT = TEXT;
	}

	public String getTEXT() {
		return TEXT;
	}

	public String MoreRead() throws IOException {
		InputStream BR = (InputStream) SESSION_LIST.get(ID).get("BR");
		return String.valueOf(BR.read());
	}
}

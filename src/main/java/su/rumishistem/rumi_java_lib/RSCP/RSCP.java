package su.rumishistem.rumi_java_lib.RSCP;

import java.io.IOException;

public class RSCP {
	private String host;

	public RSCP(String host) {
		this.host = host;
	}

	public FileUploader upload(String bucket, String name, boolean is_public) throws IOException {
		return new FileUploader(host, bucket, name, is_public);
	}
}

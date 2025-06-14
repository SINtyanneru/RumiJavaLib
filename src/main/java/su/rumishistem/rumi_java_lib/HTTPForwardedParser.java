package su.rumishistem.rumi_java_lib;

public class HTTPForwardedParser {
	private String IP = null;
	private boolean SSL = false;
	private String HOST = null;

	public HTTPForwardedParser(String Forwarded) {
		try {
			for (String ROW:Forwarded.split(";")) {
				String KEY = ROW.split("=")[0];
				String VAL = ROW.split("=")[1];
				switch (KEY.toUpperCase()) {
					case "FOR": {
						IP = VAL;
						break;
					}

					case "PROTO": {
						if (VAL.equalsIgnoreCase("HTTPS")) {
							SSL = true;
						}
						break;
					}

					case "HOST": {
						HOST = VAL;
						break;
					}
				}
			}
		} catch (NullPointerException a) {
		}
	}

	public String GetIP() {
		return IP;
	}

	public boolean isSSL() {
		return SSL;
	}

	public String getHOST() {
		return HOST;
	}
}

package su.rumishistem.rumi_java_lib.SmartHTTP;

public class HTTP_RESULT {
	public int STATUS = 0;
	public byte[] DATA = null;
	public String MIME;

	public HTTP_RESULT(int STATUS, byte[] DATA, String MIME) {
		this.STATUS = STATUS;
		this.DATA = DATA;
		this.MIME = MIME;
	}
}

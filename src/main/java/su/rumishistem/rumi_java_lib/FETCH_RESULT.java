package su.rumishistem.rumi_java_lib;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

public class FETCH_RESULT {
	private int STATUS_CODE = 0;
	private byte[] BODY = null;

	public FETCH_RESULT(int STATUS_CODE, byte[] BODY) {
		this.STATUS_CODE = STATUS_CODE;
		this.BODY = BODY;
	}

	public int GetSTATUS_CODE() {
		return STATUS_CODE;
	}

	public byte[] GetRAW() {
		return BODY;
	}

	public String GetString() {
		return new String(BODY, StandardCharsets.UTF_8);
	}

	public String GetString(Charset CHAR_CODE) {
		return new String(BODY, CHAR_CODE);
	}

	public void SaveFile(File FILE) throws IOException {
		if(STATUS_CODE == HttpURLConnection.HTTP_OK){
			FileOutputStream FOS = new FileOutputStream(FILE);
			FOS.write(BODY);
			FOS.flush();
			FOS.close();
		}
	}
}

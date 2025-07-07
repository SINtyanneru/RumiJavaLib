package su.rumishistem.rumi_java_lib;

import java.io.*;
import java.net.HttpURLConnection;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;

public class FETCH_RESULT {
	private int ResponseCode = 0;
	private long PING = 0;
	private HttpURLConnection HUC;
	private int MaxBodySize;
	private HashMap<String, String> HeaderTable = new HashMap<>();

	public FETCH_RESULT(int ResponseCode, long PING, HttpURLConnection HUC, int MaxBodySize) {
		this.ResponseCode = ResponseCode;
		this.PING = PING;
		this.HUC = HUC;
		this.MaxBodySize = MaxBodySize;

		for (String Key:HUC.getHeaderFields().keySet()) {
			if (Key == null) continue;

			HeaderTable.put(Key.toUpperCase(), HUC.getHeaderField(Key));
		}
	}

	public int getStatusCode() {
		return ResponseCode;
	}

	public long getPing() {
		return PING;
	}

	public String getHeader(String Key) {
		return HeaderTable.get(Key.toUpperCase());
	}

	public byte[] getRaw() throws IOException {
		ByteArrayOutputStream BAOS = new ByteArrayOutputStream();
		InputStream IS = null;

		if (ResponseCode >= 200 && ResponseCode <= 299) {
			IS = HUC.getInputStream();
		} else if (ResponseCode >= 400 && ResponseCode <= 599) {
			IS = HUC.getErrorStream();
		}

		if (IS == null) throw  new IOException("InputStreamがなーい");

		//ボディーを読む
		byte[] Buffer = new byte[1024];
		int BytesRead;
		int TotalRead = 0;
		while ((BytesRead = IS.read(Buffer, 0, Buffer.length)) != -1) {
			TotalRead += BytesRead;

			if (TotalRead > MaxBodySize) {
				IS.close();
				throw new IOException("データサイズが" + MaxBodySize + "を超えました");
			}

			BAOS.write(Buffer, 0, BytesRead);
		}
		IS.close();

		return BAOS.toByteArray();
	}

	public String getString() throws IOException {
		return new String(getRaw(), StandardCharsets.UTF_8);
	}

	public String getString(Charset CHAR_CODE) throws IOException {
		return new String(getRaw(), CHAR_CODE);
	}

	public void SaveFile(File FILE) throws IOException {
		if (ResponseCode >= 200 && ResponseCode <= 299) {
			FileOutputStream FOS = new FileOutputStream(FILE);
			FOS.write(getRaw());
			FOS.flush();
			FOS.close();
		}
	}
}

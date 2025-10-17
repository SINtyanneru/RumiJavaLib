package su.rumishistem.rumi_java_lib.Ajax;

import kotlin.text.Charsets;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class AjaxResult {
	private int code = 0;
	private InputStream is;

	public AjaxResult(int code, InputStream is) {
		this.code = code;
		this.is = is;
	}

	public int get_code() {
		return code;
	}

	public byte[] get_body_as_byte() throws IOException {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();

		byte[] buffer = new byte[1024];
		int read_length = 0;
		while ((read_length = is.read(buffer)) != -1) {
			baos.write(buffer, 0, read_length);
		}
		is.close();

		return baos.toByteArray();
	}

	public String get_body_as_string() throws IOException {
		return new String(get_body_as_byte(), Charsets.UTF_8);
	}
}

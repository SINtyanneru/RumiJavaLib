package su.rumishistem.rumi_java_lib.Ajax;

import kotlin.text.Charsets;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.util.List;
import java.util.Map;

public class AjaxResult {
	private int code = 0;
	private Map<String, java.util.List<String>> header_list;
	private byte[] body;

	public AjaxResult(int code, Map<String, java.util.List<String>> header_list, byte[] body) {
		this.code = code;
		this.header_list = header_list;
		this.body = body;
	}

	public int get_code() {
		return code;
	}

	public String get_header(String key) {
		return header_list.get(key).get(0);
	}

	public String[] get_header_list(String key) {
		List<String> list = header_list.get(key);
		String[] array = new String[list.size()];

		for (int i = 0; i < array.length; i++) {
			array[i] = list.get(0);
		}

		return array;
	}

	public byte[] get_body_as_byte() throws IOException {
		return body;
	}

	public String get_body_as_string() throws IOException {
		return new String(get_body_as_byte(), Charsets.UTF_8);
	}
}

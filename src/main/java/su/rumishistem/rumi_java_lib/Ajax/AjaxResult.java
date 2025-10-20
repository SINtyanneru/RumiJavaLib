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
	private HttpURLConnection connection;
	private InputStream is;

	public AjaxResult(int code, HttpURLConnection connection, InputStream is) {
		this.code = code;
		this.connection = connection;
		this.is = is;
	}

	public int get_code() {
		return code;
	}

	public String get_header(String key) {
		for (Map.Entry<String, List<String>> entrie: connection.getHeaderFields().entrySet()) {
			if (entrie.getKey() == null) continue;
			if (entrie.getKey().equalsIgnoreCase(key)) {
				return entrie.getValue().getFirst();
			}
		}

		return null;
	}

	public String[] get_header_list(String key) {
		for (Map.Entry<String, List<String>> entrie: connection.getHeaderFields().entrySet()) {
			if (entrie.getKey() == null) continue;
			if (entrie.getKey().equalsIgnoreCase(key)) {
				String[] value_list = new String[entrie.getValue().size()];
				for (int i = 0; i < value_list.length; i++) {
					value_list[i] = entrie.getValue().get(i);
				}
				return value_list;
			}
		}

		return null;
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

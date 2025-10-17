package su.rumishistem.rumi_java_lib.Ajax;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;

public class Ajax {
	private URL url;
	private HashMap<String, String> header_list = new HashMap<>(){{
		put("USER-AGENT", "Ajax/1.0");
	}};

	public Ajax(String url) throws MalformedURLException {
		this.url = new URL(url);
	}

	public void set_header(String key, String value) {
		header_list.put(key.toUpperCase(), value);
	}

	public AjaxResult GET() throws IOException {
		HttpURLConnection connection = open_connection();
		connection.setRequestMethod("GET");
		int code = connection.getResponseCode();
		return new AjaxResult(code, get_br(code, connection));
	}

	public AjaxResult DELETE() throws IOException {
		HttpURLConnection connection = open_connection();
		connection.setRequestMethod("DELETE");
		int code = connection.getResponseCode();
		return new AjaxResult(code, get_br(code, connection));
	}

	public AjaxResult POST(byte[] body) throws IOException {
		HttpURLConnection connection = open_connection();
		connection.setRequestMethod("POST");
		connection.setDoOutput(true);

		//送信
		OutputStream os = connection.getOutputStream();
		os.write(body, 0, body.length);
		os.close();

		int code = connection.getResponseCode();
		return new AjaxResult(code, get_br(code, connection));
	}

	private InputStream get_br(int code, HttpURLConnection connection) throws IOException {
		InputStream is = null;
		if (code >= 200 && code <= 299) {
			is = connection.getInputStream();
		} else if (code >= 400 && code <= 599) {
			is = connection.getErrorStream();
		}
		return is;
	}

	private HttpURLConnection open_connection() throws IOException {
		HttpURLConnection connection = (HttpURLConnection) url.openConnection();

		for (String key:header_list.keySet()) {
			connection.setRequestProperty(key, header_list.get(key));
		}

		return connection;
	}
}

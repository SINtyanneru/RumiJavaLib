package su.rumishistem.rumi_java_lib.Ajax;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.HashMap;
import java.util.Map;

public class Ajax {
	private HttpClient client;
	private URI uri;
	private HashMap<String, String> header_list = new HashMap<>(){{
		put("USER-AGENT", "Ajax/1.0");
	}};
	private boolean follow_redirect = true;

	public Ajax(String url) throws MalformedURLException {
		uri = URI.create(url);
		rebuild_client();
	}

	public void set_header(String key, String value) {
		if (key.equalsIgnoreCase("HOST")) return;
		header_list.put(key.toUpperCase(), value);
	}

	public void set_follow_redirect(boolean bl) {
		follow_redirect = bl;
		rebuild_client();
	}

	public AjaxResult GET() throws IOException {
		HttpRequest r = base_build().GET().build();
		return send(r);
	}

	public AjaxResult DELETE() throws IOException {
		HttpRequest r = base_build().DELETE().build();
		return send(r);
	}

	public AjaxResult POST(byte[] body) throws IOException {
		HttpRequest r = base_build().POST(HttpRequest.BodyPublishers.ofByteArray(body)).build();
		return send(r);
	}

	public AjaxResult PATCH(byte[] body) throws IOException {
		HttpRequest r = base_build().method("PATCH", HttpRequest.BodyPublishers.ofByteArray(body)).build();
		return send(r);
	}

	private void rebuild_client() {
		HttpClient.Builder builder = HttpClient.newBuilder();
		if (follow_redirect) {
			builder.followRedirects(HttpClient.Redirect.NORMAL);
		} else {
			builder.followRedirects(HttpClient.Redirect.NEVER);
		}
		client = builder.build();
	}

	private HttpRequest.Builder base_build() {
		HttpRequest.Builder builder = HttpRequest.newBuilder();
		builder.uri(uri);

		for (Map.Entry<String, String> entry:header_list.entrySet()) {
			builder.header(entry.getKey(), entry.getValue());
		}

		return builder;
	}

	private AjaxResult send(HttpRequest r) throws IOException {
		try {
			HttpResponse<byte[]> response = client.send(r, HttpResponse.BodyHandlers.ofByteArray());

			return new AjaxResult(response.statusCode(), response.headers().map(), response.body());
		} catch (InterruptedException ex) {
			//は？
			return null;
		}
	}
}

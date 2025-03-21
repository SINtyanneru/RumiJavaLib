package su.rumishistem.rumi_java_lib;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class FETCH {
	private URL URI = null;
	private List<HashMap<String, String>> HEADER_LIST = new ArrayList<>();

	public FETCH(String URI) throws MalformedURLException {
		this.URI = new URL(URI);
	}

	//ヘッダーをセットするやつ
	public void SetHEADER(String KEY, String VAL){
		HashMap<String, String> HEADER = new HashMap<>();
		HEADER.put("KEY", KEY);
		HEADER.put("VAL", VAL);

		HEADER_LIST.add(HEADER);
	}

	public FETCH_RESULT GET() {
		try {
			long START = System.currentTimeMillis();

			HttpURLConnection HUC = (HttpURLConnection) URI.openConnection();

			//GETリクエストだと主張する
			HUC.setRequestMethod("GET");

			//ヘッダーを入れる
			for(HashMap<String, String> HEADER:HEADER_LIST){
				HUC.setRequestProperty(HEADER.get("KEY"), HEADER.get("VAL"));
			}

			//接続
			HUC.connect();

			//レスポンスコード
			int RES_CODE = HUC.getResponseCode();

			//応答内容
			ByteArrayOutputStream BAOS = new ByteArrayOutputStream();
			InputStream IS = null;

			if (RES_CODE >= 200 && RES_CODE <= 299) {
				IS = HUC.getInputStream();
			} else if (RES_CODE >= 400 && RES_CODE <= 599) {
				IS = HUC.getErrorStream();
			}

			if (IS != null) {
				//応答を読み込み
				byte[] BUFFER = new byte[1024];
				int BYTE_READ;
				while ((BYTE_READ = IS.read(BUFFER, 0, BUFFER.length)) != -1) {
					BAOS.write(BUFFER, 0, BYTE_READ);
				}
				IS.close();
			}

			long END = System.currentTimeMillis();

			FETCH_RESULT RESULT = new FETCH_RESULT(RES_CODE, BAOS.toByteArray(), (END - START));
			return RESULT;
		} catch (Exception EX) {
			//あ
			return null;
		}
	}

	public FETCH_RESULT POST(byte[] BODY) {
		try {
			long START = System.currentTimeMillis();

			HttpURLConnection HUC = (HttpURLConnection) URI.openConnection();

			//GETリクエストだと主張する
			HUC.setRequestMethod("POST");

			//POST可能に
			HUC.setDoInput(true);
			HUC.setDoOutput(true);

			//ヘッダーを入れる
			for(HashMap<String, String> HEADER:HEADER_LIST){
				HUC.setRequestProperty(HEADER.get("KEY"), HEADER.get("VAL"));
			}

			//接続
			HUC.connect();

			//リクエストボディに送信したいデータを書き込む
			PrintStream PS = new PrintStream(HUC.getOutputStream());
			PS.write(BODY);
			PS.close();

			//レスポンスコード
			int RES_CODE = HUC.getResponseCode();
			//応答内容
			ByteArrayOutputStream BAOS = new ByteArrayOutputStream();
			InputStream IS = null;

			if (RES_CODE >= 200 && RES_CODE <= 299) {
				IS = HUC.getInputStream();
			} else if (RES_CODE >= 400 && RES_CODE <= 499) {
				IS = HUC.getErrorStream();
			} else if (RES_CODE >= 500 && RES_CODE <= 599) {
				IS = HUC.getErrorStream();
			} else {
				IS = HUC.getInputStream();
			}

			byte[] BUFFER = new byte[1024];
			int BYTE_READ;
			while ((BYTE_READ = IS.read(BUFFER, 0, BUFFER.length)) != -1) {
				BAOS.write(BUFFER, 0, BYTE_READ);
			}

			long END = System.currentTimeMillis();

			FETCH_RESULT RESULT = new FETCH_RESULT(RES_CODE, BAOS.toByteArray(), (END - START));
			return RESULT;
		} catch (Exception EX) {
			//あ
			EX.printStackTrace();
			return null;
		}
	}

	public FETCH_RESULT DELETE() {
		try {
			long START = System.currentTimeMillis();

			HttpURLConnection HUC = (HttpURLConnection) URI.openConnection();

			//GETリクエストだと主張する
			HUC.setRequestMethod("DELETE");

			//接続
			HUC.connect();

			//ヘッダーを入れる
			for(HashMap<String, String> HEADER:HEADER_LIST){
				HUC.setRequestProperty(HEADER.get("KEY"), HEADER.get("VAL"));
			}

			//レスポンスコード
			int RES_CODE = HUC.getResponseCode();
			//応答内容
			ByteArrayOutputStream BAOS = new ByteArrayOutputStream();

			if (RES_CODE == HttpURLConnection.HTTP_OK) {
				InputStream IS = HUC.getInputStream();
				byte[] BUFFER = new byte[1024];
				int BYTE_READ;
				while ((BYTE_READ = IS.read(BUFFER, 0, BUFFER.length)) != -1) {
					BAOS.write(BUFFER, 0, BYTE_READ);
				}
			} else {
				InputStream IS = HUC.getErrorStream();
				byte[] BUFFER = new byte[1024];
				int BYTE_READ;
				while ((BYTE_READ = IS.read(BUFFER, 0, BUFFER.length)) != -1) {
					BAOS.write(BUFFER, 0, BYTE_READ);
				}
			}

			long END = System.currentTimeMillis();

			FETCH_RESULT RESULT = new FETCH_RESULT(RES_CODE, BAOS.toByteArray(), (END - START));
			return RESULT;
		} catch (Exception EX) {
			//あ
			return null;
		}
	}
}

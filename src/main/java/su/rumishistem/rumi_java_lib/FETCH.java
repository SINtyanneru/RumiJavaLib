package su.rumishistem.rumi_java_lib;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class FETCH {
	private URL URI = null;
	private List<HashMap<String, String>> HEADER_LIST = new ArrayList<>();
	private int MaxBodySize = 1000 * 1024 * 1024;

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

	public void setMaxBodySize(int Size) {
		MaxBodySize = Size;
	}

	private FETCH_RESULT GenResult(int ResponseCode, HttpURLConnection HUC, long Ping) throws IOException {
		ByteArrayOutputStream BAOS = new ByteArrayOutputStream();
		InputStream IS = null;

		if (ResponseCode >= 200 && ResponseCode <= 299) {
			IS = HUC.getInputStream();
		} else if (ResponseCode >= 400 && ResponseCode <= 599) {
			IS = HUC.getErrorStream();
		}

		if (IS == null) throw  new Error("InputStreamがなーい");

		//ボディーを読む
		byte[] Buffer = new byte[1024];
		int BytesRead;
		int TotalRead = 0;
		while ((BytesRead = IS.read(Buffer, 0, Buffer.length)) != -1) {
			TotalRead += BytesRead;

			if (TotalRead > MaxBodySize) {
				IS.close();
				throw new Error("データサイズが" + MaxBodySize + "を超えました");
			}

			BAOS.write(Buffer, 0, BytesRead);
		}
		IS.close();

		return new FETCH_RESULT(ResponseCode, BAOS.toByteArray(), Ping);
	}

	public FETCH_RESULT GET() {
		try {
			long StartTime = System.currentTimeMillis();

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
			int ResponseCode = HUC.getResponseCode();
			long EndTime = System.currentTimeMillis();

			return GenResult(ResponseCode, HUC, (EndTime - StartTime));
		} catch (Exception EX) {
			//あ
			return null;
		}
	}

	public FETCH_RESULT POST(byte[] BODY) {
		try {
			long StartTime = System.currentTimeMillis();

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
			int ResponseCode = HUC.getResponseCode();
			long EndTime = System.currentTimeMillis();

			return GenResult(ResponseCode, HUC, (EndTime - StartTime));
		} catch (Exception EX) {
			//あ
			EX.printStackTrace();
			return null;
		}
	}

	public FETCH_RESULT DELETE() {
		try {
			long StartTime = System.currentTimeMillis();

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
			int ResponseCode = HUC.getResponseCode();
			long EndTime = System.currentTimeMillis();

			return GenResult(ResponseCode, HUC, (EndTime - StartTime));
		} catch (Exception EX) {
			//あ
			return null;
		}
	}
}

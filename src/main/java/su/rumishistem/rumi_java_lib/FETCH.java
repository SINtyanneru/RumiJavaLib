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

	public void setMaxBodySizeKB(int Size) {
		setMaxBodySize(Size * 1024);
	}

	public void setMaxBodySizeMB(int Size) {
		setMaxBodySize(Size * 1024 * 1024);
	}

	public void setMaxBodySizeGB(int Size) {
		setMaxBodySize(Size * 1024 * 1024 * 1024);
	}

	private FETCH_RESULT GenResult(int ResponseCode, HttpURLConnection HUC, long Ping) throws IOException {
		return new FETCH_RESULT(ResponseCode, Ping, HUC, MaxBodySize);
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

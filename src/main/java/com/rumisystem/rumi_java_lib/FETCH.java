package com.rumisystem.rumi_java_lib;

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
			HttpURLConnection HUC = (HttpURLConnection) URI.openConnection();

			//GETリクエストだと主張する
			HUC.setRequestMethod("GET");

			//ヘッダーを入れる
			for(HashMap<String, String> HEADER:HEADER_LIST){
				HUC.setRequestProperty(HEADER.get("KEY"), HEADER.get("VAL"));
			}

			//レスポンスコード
			int RES_CODE = HUC.getResponseCode();

			InputStream IS = HUC.getInputStream();
			ByteArrayOutputStream BAOS = new ByteArrayOutputStream();
			byte[] BUFFER = new byte[1024];
			int BYTE_READ;
			while ((BYTE_READ = IS.read(BUFFER, 0, BUFFER.length)) != -1) {
				BAOS.write(BUFFER, 0, BYTE_READ);
			}

			FETCH_RESULT RESULT = new FETCH_RESULT(RES_CODE, BAOS.toByteArray());
			return RESULT;
		} catch (Exception EX) {
			//あ
			return null;
		}
	}

	public FETCH_RESULT POST(byte[] BODY) {
		try {
			HttpURLConnection HUC = (HttpURLConnection) URI.openConnection();

			//GETリクエストだと主張する
			HUC.setRequestMethod("GET");

			//POST可能に
			HUC.setDoInput(true);
			HUC.setDoOutput(true);

			HUC.connect();

			//リクエストボディに送信したいデータを書き込む
			PrintStream PS = new PrintStream(HUC.getOutputStream());
			PS.write(BODY);
			PS.close();

			//ヘッダーを入れる
			for(HashMap<String, String> HEADER:HEADER_LIST){
				HUC.setRequestProperty(HEADER.get("KEY"), HEADER.get("VAL"));
			}

			//レスポンスコード
			int RES_CODE = HUC.getResponseCode();

			InputStream IS = HUC.getInputStream();
			ByteArrayOutputStream BAOS = new ByteArrayOutputStream();
			byte[] BUFFER = new byte[1024];
			int BYTE_READ;
			while ((BYTE_READ = IS.read(BUFFER, 0, BUFFER.length)) != -1) {
				BAOS.write(BUFFER, 0, BYTE_READ);
			}

			FETCH_RESULT RESULT = new FETCH_RESULT(RES_CODE, BAOS.toByteArray());
			return RESULT;
		} catch (Exception EX) {
			//あ
			return null;
		}
	}
}

package com.rumisystem.rumi_java_lib;

import com.rumisystem.rumi_java_lib.LOG_PRINT.LOG_TYPE;

import java.io.InputStream;
import java.net.ProtocolException;
import java.net.URL;
import java.io.*;
import java.net.HttpURLConnection;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static com.rumisystem.rumi_java_lib.LOG_PRINT.Main.LOG;

public class HTTP_REQUEST {
	private URL REQIEST_URI = null;
	private List<HashMap<String, String>> HEADER_LIST = new ArrayList<>();
	public HTTP_REQUEST(String INPUT_REQ_URL){
		try{
			REQIEST_URI = new URL(INPUT_REQ_URL);
		}catch (Exception EX) {
			System.err.println(EX);
			System.exit(1);
		}
	}

	//クッキーをセットするやつ
	public void HEADER_SET(String KEY, String VAL){
		HashMap<String, String> HEADER = new HashMap<>();
		HEADER.put("KEY", KEY);
		HEADER.put("VAL", VAL);

		HEADER_LIST.add(HEADER);
	}

	//GET
	public String GET() throws IOException {
		HttpURLConnection HUC = (HttpURLConnection) REQIEST_URI.openConnection();

		//GETリクエストだと主張する
		HUC.setRequestMethod("GET");

		//ヘッダーを入れる
		for(HashMap<String, String> HEADER:HEADER_LIST){
			HUC.setRequestProperty(HEADER.get("KEY"), HEADER.get("VAL"));
		}

		//レスポンスコード
		int RES_CODE = HUC.getResponseCode();

		BufferedReader BR = new BufferedReader(new InputStreamReader(HUC.getInputStream(), StandardCharsets.UTF_8));
		StringBuilder RES_STRING = new StringBuilder();

		String INPUT_LINE;
		while ((INPUT_LINE = BR.readLine()) != null){
			RES_STRING.append(INPUT_LINE);
		}

		BR.close();
		return RES_STRING.toString();
	}

	public String POST(String POST_BODY) throws IOException {
		HttpURLConnection HUC = (HttpURLConnection) REQIEST_URI.openConnection();

		//ヘッダーを入れる
		for(HashMap<String, String> HEADER:HEADER_LIST){
			HUC.setRequestProperty(HEADER.get("KEY"), HEADER.get("VAL"));
		}

		//POSTだと主張する
		HUC.setRequestMethod("POST");

		//POST可能に
		HUC.setDoInput(true);
		HUC.setDoOutput(true);

		HUC.connect();

		//リクエストボディに送信したいデータを書き込む
		PrintStream PS = new PrintStream(HUC.getOutputStream());
		PS.print(POST_BODY);
		PS.close();

		//レスポンスコード
		int RES_CODE = HUC.getResponseCode();
		BufferedReader BR = new BufferedReader(new InputStreamReader(HUC.getInputStream(), StandardCharsets.UTF_8));
		StringBuilder RES_STRING = new StringBuilder();

		String INPUT_LINE;
		while ((INPUT_LINE = BR.readLine()) != null){
			RES_STRING.append(INPUT_LINE);
		}

		BR.close();
		return RES_STRING.toString();
	}

	//ダウンロード
	public void DOWNLOAD(String PATH) throws IOException {
		//名前が長すぎるので切り落としたよ
		HttpURLConnection HUC = (HttpURLConnection) REQIEST_URI.openConnection();

		//ヘッダーを入れる
		for(HashMap<String, String> HEADER:HEADER_LIST){
			HUC.setRequestProperty(HEADER.get("KEY"), HEADER.get("VAL"));
		}

		//GETリクエストだと主張する
		HUC.setRequestMethod("GET");

		//レスポンスコード
		int RES_CODE = HUC.getResponseCode();
		if(RES_CODE == HttpURLConnection.HTTP_OK){
			//ファイルを保存する機構
			InputStream IS = HUC.getInputStream();
			FileOutputStream OS = new FileOutputStream(PATH);
			byte[] BUFFER = new byte[4096];
			int BYTES_READ;
			while((BYTES_READ = IS.read(BUFFER)) != -1){
				OS.write(BUFFER, 0, BYTES_READ);
			}

		}
	}
}

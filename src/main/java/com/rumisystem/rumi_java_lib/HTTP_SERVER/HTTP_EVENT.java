package com.rumisystem.rumi_java_lib.HTTP_SERVER;

import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;

import javax.swing.event.EventListenerList;
import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.util.EventObject;
import java.util.HashMap;

public class HTTP_EVENT extends EventObject {
	private static HttpExchange EXCHANGE;
	private static HashMap<String, String> URI_PARAM;
	private static byte[] POST_DATA;
	private static HashMap<String, String> HEADER_DATA;
	private static Headers RES_HEADER;

	public HTTP_EVENT(Object source, HttpExchange EXCHANGE, HashMap<String, String> URI_PARAM, byte[] POST_DATA, HashMap<String, String> HEADER_DATA) {
		super(source);

		this.EXCHANGE = EXCHANGE;
		this.URI_PARAM = URI_PARAM;
		this.POST_DATA = POST_DATA;
		this.HEADER_DATA = HEADER_DATA;

		RES_HEADER = EXCHANGE.getResponseHeaders();
	}

	public HttpExchange getEXCHANGE(){
		return EXCHANGE;
	}

	public HashMap<String, String> getURI_PARAM(){
		return URI_PARAM;
	}

	public String getPOST_DATA(){
		return new String(POST_DATA, StandardCharsets.UTF_8);
	}

	public byte[] getPOST_DATA_BIN(){
		return POST_DATA;
	}

	public HashMap<String, String> getHEADER_DATA(){
		return HEADER_DATA;
	}

	/**
	 * ヘッダーをセットします
	 * @param KEY キー
	 * @param VAL 内容
	 */
	public void setHEADER(String KEY, String VAL){
		RES_HEADER.set(KEY, VAL);
	}

	/**
	 * バイトの配列で応答を返します
	 * @throws IOException 例外
	 */
	public void REPLY_BYTE(int STATUS, byte[] BODY) throws IOException {
		//ステータスコードと文字数
		EXCHANGE.sendResponseHeaders(STATUS, BODY.length);
		//書き込むやつ
		OutputStream OS = EXCHANGE.getResponseBody();
		//文字列を書き込む
		OS.write(BODY);
		//フラッシュする
		OS.flush();
		//終了
		OS.close();
	}

	/**
	 * 文字列型で応答を返します
	 * @throws IOException 例外
	 */
	public void REPLY_String(int STATUS, String BODY) throws IOException {
		byte[] BS = BODY.getBytes(StandardCharsets.UTF_8);

		//ステータスコードと文字数
		EXCHANGE.sendResponseHeaders(STATUS, BS.length);
		//書き込むやつ
		OutputStream OS = EXCHANGE.getResponseBody();
		//文字列を書き込む
		OS.write(BS);
		//フラッシュする
		OS.flush();
		//終了
		OS.close();
	}
}

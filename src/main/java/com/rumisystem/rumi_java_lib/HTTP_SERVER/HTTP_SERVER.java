package com.rumisystem.rumi_java_lib.HTTP_SERVER;

import com.rumisystem.rumi_java_lib.LOG_PRINT.LOG_TYPE;
import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;

import javax.swing.event.EventListenerList;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetSocketAddress;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.rumisystem.rumi_java_lib.LOG_PRINT.Main.LOG;

public class HTTP_SERVER {
	private int PORT;

	EventListenerList EL_LIST = new EventListenerList();

	public HTTP_SERVER(int PORT){
		this.PORT = PORT;
	}


	//HTTPリクエストの受信先を指定する
	public void SET_EVENT_VOID(HTTP_EVENT_LISTENER EVL){
		EL_LIST.add(HTTP_EVENT_LISTENER.class, EVL);
	}

	//HTTP鯖を実行する
	public void START_HTTPSERVER() throws IOException {
		HttpServer SERVER = HttpServer.create(new InetSocketAddress(PORT), 0);

		SERVER.createContext("/", new HTTP_HANDLER());
		SERVER.setExecutor(null); //デフォルトのExecutorを使用

		SERVER.start();
		LOG(LOG_TYPE.OK, "Started HTTP Server!");
		LOG(LOG_TYPE.OK, "Port:" + PORT);
	}


	private class HTTP_HANDLER implements HttpHandler {
		@Override
		public void handle(HttpExchange EXCHANGE) {
			try {
				LOG(LOG_TYPE.INFO, "HTTP Request:" + EXCHANGE.getRequestMethod() + " " + EXCHANGE.getRequestURI());

				//URIパラメーターを解析するやつ
				HashMap<String, String> URI_PARAM_HM = new HashMap<String, String>();//URIパラメーターを解析した結果のハッシュマップ
				int MARK_INDEX = EXCHANGE.getRequestURI().toString().indexOf("?");
				if (MARK_INDEX != -1) {//?が見つかった場合
					//?以降の文字列を取得
					String[] URI_PARAM_ARRAY = EXCHANGE.getRequestURI().toString().substring(MARK_INDEX + 1).split("&");
					for (String URI_PARAM : URI_PARAM_ARRAY) {
						URI_PARAM_HM.put(URI_PARAM.split("=")[0], URI_PARAM.split("=")[1]);
					}
				}

				//POSTされたデータ
				String POST_DATA = null;
				if ("POST".equals(EXCHANGE.getRequestMethod())) {
					//リクエストのInputStreamからデータを読み取る
					InputStreamReader ISR = new InputStreamReader(EXCHANGE.getRequestBody(), StandardCharsets.UTF_8);
					BufferedReader BR = new BufferedReader(ISR);
					StringBuilder POST_DATA_SB = new StringBuilder();
					String LINE;
					while ((LINE = BR.readLine()) != null) {
						POST_DATA_SB.append(LINE);
					}
					POST_DATA = POST_DATA_SB.toString();
				}

				//リクエストヘッダーを取得
				Map<String, List<String>> HEADER_TEMP = EXCHANGE.getRequestHeaders();
				HashMap<String, String> HEADER_DATA = new HashMap<String, String>();
				for (Map.Entry<String, List<String>> entry : HEADER_TEMP.entrySet()) {
					String HEADER_KEY = entry.getKey();
					List<String> HEADER_VAL = entry.getValue();
					//データをhashMapに入れるのだちんちんまーんこ
					HEADER_DATA.put(HEADER_KEY.toUpperCase(), HEADER_VAL.get(0).toString());
				}


				//イベントを発火する
				HTTP_EVENT_LISTENER[] LISTENER_LIST = EL_LIST.getListeners(HTTP_EVENT_LISTENER.class);
				for(HTTP_EVENT_LISTENER LISTENER:LISTENER_LIST){
					LISTENER.REQUEST_EVENT(new HTTP_EVENT(this, EXCHANGE, URI_PARAM_HM, POST_DATA, HEADER_DATA));
				}
			} catch (Exception EX) {
				LOG(LOG_TYPE.FAILED, "HTTP Server ERR");
				EX.printStackTrace();
			}
		}
	}
}

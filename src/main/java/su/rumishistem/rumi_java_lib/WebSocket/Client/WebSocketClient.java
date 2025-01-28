package su.rumishistem.rumi_java_lib.WebSocket.Client;

import su.rumishistem.rumi_java_lib.WebSocket.Client.EVENT.CLOSE_EVENT;
import su.rumishistem.rumi_java_lib.WebSocket.Client.EVENT.CONNECT_EVENT;
import su.rumishistem.rumi_java_lib.WebSocket.Client.EVENT.MESSAGE_EVENT;
import su.rumishistem.rumi_java_lib.WebSocket.Client.EVENT.WS_EVENT_LISTENER;
import kotlin.text.Charsets;
import okhttp3.*;
import okio.ByteString;
import javax.swing.event.EventListenerList;
import java.io.BufferedReader;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

public class WebSocketClient {
	private EventListenerList EL_LIST = new EventListenerList();
	private BufferedReader BR = null;
	private PrintWriter BW  = null;
	private List<HEADER_TYPE> HEADER_LIST = new ArrayList<>();
	private WebSocket WS = null;
	private boolean RECONNECT = true;

	public void CONNECT(String URL) {
		try {
			//クライアントを準備
			OkHttpClient CLIENT = new OkHttpClient();
			Request.Builder REQUEST = new Request.Builder();
			REQUEST.url(URL);

			for (HEADER_TYPE HEADER:HEADER_LIST) {
				REQUEST.addHeader(HEADER.GetKEY(), HEADER.GetVAL());
			}

			//イベントリスナー
			WebSocketListener WSL = new WebSocketListener() {
				@Override
				public void onOpen(WebSocket SESSION, Response RES) {
					//接続
					WS_EVENT_LISTENER[] ELL = EL_LIST.getListeners(WS_EVENT_LISTENER.class);
					for (WS_EVENT_LISTENER EL:ELL) {
						EL.CONNECT(new CONNECT_EVENT(SESSION));
					}
				}

				@Override
				public void onMessage(WebSocket SESSION, String TEXT) {
					//受信(文字列)
					WS_EVENT_LISTENER[] ELL = EL_LIST.getListeners(WS_EVENT_LISTENER.class);
					for (WS_EVENT_LISTENER EL:ELL) {
						EL.MESSAGE(new MESSAGE_EVENT(TEXT.getBytes(Charsets.UTF_8), SESSION));
					}
				}

				@Override
				public void onMessage(WebSocket SESSION, ByteString BYTES) {
					//受信(バイト)
					WS_EVENT_LISTENER[] ELL = EL_LIST.getListeners(WS_EVENT_LISTENER.class);
					for (WS_EVENT_LISTENER EL:ELL) {
						EL.MESSAGE(new MESSAGE_EVENT(BYTES.toByteArray(), SESSION));
					}
				}

				@Override
				public void onClosing(WebSocket SESSION, int CODE, String REASON) {
					//切断
					SESSION.close(1000, null);

					WS_EVENT_LISTENER[] ELL = EL_LIST.getListeners(WS_EVENT_LISTENER.class);
					for (WS_EVENT_LISTENER EL:ELL) {
						EL.CLOSE(new CLOSE_EVENT(REASON, CODE));
					}

					//再接続
					if (RECONNECT) {
						try {
							WS.close(0, "");
						} catch (Exception EX) {
							//無視
						}
						WS = CLIENT.newWebSocket(REQUEST.build(), this);
					}
				}

				@Override
				public void onFailure(WebSocket SESSION, Throwable T, Response RES) {
					//エラー
				}
			};

			//接続
			WS = CLIENT.newWebSocket(REQUEST.build(), WSL);

			//アプリ終了時に切断処理
			Runtime.getRuntime().addShutdownHook(new Thread(new Runnable() {
				@Override
				public void run() {
					CLIENT.dispatcher().executorService().shutdown();
				}
			}));
		} catch (Exception EX) {
			EX.printStackTrace();
		}
	}

	public void SET_EVENT_LISTENER(WS_EVENT_LISTENER EVENT_LISTENER) {
		EL_LIST.add(WS_EVENT_LISTENER.class, EVENT_LISTENER);
	}

	public void SetHEADER(String KEY, String VAL) {
		HEADER_LIST.add(new HEADER_TYPE(KEY, VAL));
	}
}

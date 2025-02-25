package su.rumishistem.rumi_java_lib.WebSocket.Client;

import su.rumishistem.rumi_java_lib.WebSocket.Client.EVENT.*;
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
	private OkHttpClient CLIENT = new OkHttpClient();
	private String URL;
	private final int MAX_RETRY_DELAY = 60000;

	public void CONNECT(String URL) {
		this.URL = URL;
		Connect(0);
	}

	private void Connect(int RetryDelay) {
		try {
			Request.Builder REQUEST = new Request.Builder().url(URL);
			for (HEADER_TYPE HEADER : HEADER_LIST) {
				REQUEST.addHeader(HEADER.GetKEY(), HEADER.GetVAL());
			}

			WebSocketListener WSL = new WebSocketListener() {
				@Override
				public void onOpen(WebSocket SESSION, Response RES) {
					WS = SESSION;

					WS_EVENT_LISTENER[] ELL = EL_LIST.getListeners(WS_EVENT_LISTENER.class);
					for (WS_EVENT_LISTENER EL : ELL) {
						EL.CONNECT(new CONNECT_EVENT(SESSION));
					}
				}

				@Override
				public void onMessage(WebSocket SESSION, String TEXT) {
					WS_EVENT_LISTENER[] ELL = EL_LIST.getListeners(WS_EVENT_LISTENER.class);
					for (WS_EVENT_LISTENER EL : ELL) {
						EL.MESSAGE(new MESSAGE_EVENT(TEXT.getBytes(Charsets.UTF_8), SESSION));
					}
				}

				@Override
				public void onMessage(WebSocket SESSION, ByteString BYTES) {
					WS_EVENT_LISTENER[] ELL = EL_LIST.getListeners(WS_EVENT_LISTENER.class);
					for (WS_EVENT_LISTENER EL : ELL) {
						EL.MESSAGE(new MESSAGE_EVENT(BYTES.toByteArray(), SESSION));
					}
				}

				@Override
				public void onClosing(WebSocket SESSION, int CODE, String REASON) {
					SESSION.close(1000, null);

					WS_EVENT_LISTENER[] ELL = EL_LIST.getListeners(WS_EVENT_LISTENER.class);
					for (WS_EVENT_LISTENER EL : ELL) {
						EL.CLOSE(new CLOSE_EVENT(REASON, CODE));
					}

					if (RECONNECT) {
						int NextDelay = Math.min(RetryDelay * 2, MAX_RETRY_DELAY);
						scheduleReconnect(NextDelay);
					}
				}

				@Override
				public void onFailure(WebSocket SESSION, Throwable T, Response RES) {
					if (RECONNECT) {
						int NextDelay = Math.min((RetryDelay == 0 ? 1000 : RetryDelay * 2), MAX_RETRY_DELAY);
						scheduleReconnect(NextDelay);
					}
				}
			};

			WS = CLIENT.newWebSocket(REQUEST.build(), WSL);

		} catch (Exception EX) {
			EX.printStackTrace();
		}
	}

	private void scheduleReconnect(int Delay) {
		new Thread(() -> {
			try {
				Thread.sleep(Delay);
				Connect(Delay);
			} catch (InterruptedException ignored) {}
		}).start();
	}

	public void SET_EVENT_LISTENER(WS_EVENT_LISTENER EVENT_LISTENER) {
		EL_LIST.add(WS_EVENT_LISTENER.class, EVENT_LISTENER);
	}

	public void SetHEADER(String KEY, String VAL) {
		HEADER_LIST.add(new HEADER_TYPE(KEY, VAL));
	}
}

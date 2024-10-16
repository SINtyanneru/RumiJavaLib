package com.rumisystem.rumi_java_lib.Socket.Server;

import com.rumisystem.rumi_java_lib.Socket.Server.CONNECT_EVENT.CONNECT_EVENT;
import com.rumisystem.rumi_java_lib.Socket.Server.CONNECT_EVENT.CONNECT_EVENT_LISTENER;
import com.rumisystem.rumi_java_lib.Socket.Server.EVENT.CloseEvent;
import com.rumisystem.rumi_java_lib.Socket.Server.EVENT.EVENT_LISTENER;
import com.rumisystem.rumi_java_lib.Socket.Server.EVENT.MessageEvent;

import javax.swing.event.EventListenerList;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;


public class SocketServer {
	public static EventListenerList CONNECT_EL_LIST = new EventListenerList();
	public static EventListenerList EL_LIST = new EventListenerList();
	public static HashMap<Integer, String> CEL_LIST = new HashMap<>();		//接続後のイベントリスナーのhashCodeとUUIDを保存

	public void setEventListener(CONNECT_EVENT_LISTENER EL) {
		//追加
		CONNECT_EL_LIST.add(CONNECT_EVENT_LISTENER.class, EL);
	}

	public void START(int PORT) throws IOException {
		ServerSocketChannel SSC = ServerSocketChannel.open();
		SSC.bind(new InetSocketAddress(PORT));
		SSC.configureBlocking(false);

		//セレクタ用意
		Selector SELECTOR = Selector.open();
		SSC.register(SELECTOR, SelectionKey.OP_ACCEPT);

		System.out.println("Start SocketServer! Port:" + PORT);

		while(true) {
			//セレクタで準備完了のチャンネルを待つ
			SELECTOR.select();

			//チャンネルを取得
			Set<SelectionKey> SK = SELECTOR.selectedKeys();
			Iterator<SelectionKey> ITERATOR = SK.iterator();

			//配列を順番に回す
			while (ITERATOR.hasNext()) {
				SelectionKey KEY = ITERATOR.next();

				//新しいクライアントの接続
				if (KEY.isAcceptable()) {
					//接続を承認
					ServerSocketChannel SS = (ServerSocketChannel) KEY.channel();
					SocketChannel SES = SS.accept();
					SES.configureBlocking(false);
					SES.register(SELECTOR, SelectionKey.OP_READ);

					//接続されたイベントを発行
					CONNECT_EVENT_LISTENER[] ELL = CONNECT_EL_LIST.getListeners(CONNECT_EVENT_LISTENER.class);
					for (CONNECT_EVENT_LISTENER EL:ELL) {
						EL.CONNECT(new CONNECT_EVENT(String.valueOf(SES.hashCode()), SES));
					}
				} else if (KEY.isReadable()) {
					SocketChannel SES = (SocketChannel) KEY.channel();
					ByteBuffer BUFFER = ByteBuffer.allocate(256);
					int BYTE_READ = SES.read(BUFFER);

					if (BYTE_READ == -1) {
						//切断
						SES.close();

						//イベント発火
						EVENT_LISTENER[] ELL = EL_LIST.getListeners(EVENT_LISTENER.class);
						for (EVENT_LISTENER EL:ELL) {
							if (CEL_LIST.get(EL.hashCode()) != null) {
								if (CEL_LIST.get(EL.hashCode()).equals(String.valueOf(SES.hashCode()))) {
									EL.Close(new CloseEvent());
								}
							}
						}

						//CEL_LISTから削除
						for (int K:CEL_LIST.keySet()) {
							if (CEL_LIST.get(K).equals(String.valueOf(SES.hashCode()))) {
								CEL_LIST.remove(K);
							}
						}
					} else {
						//受信
						BUFFER.flip();
						byte[] DATA = new byte[BUFFER.remaining()];
						BUFFER.get(DATA);

						//イベント発火
						EVENT_LISTENER[] ELL = EL_LIST.getListeners(EVENT_LISTENER.class);
						for (EVENT_LISTENER EL:ELL) {
							if (CEL_LIST.get(EL.hashCode()) != null) {
								if (CEL_LIST.get(EL.hashCode()).equals(String.valueOf(SES.hashCode()))) {
									EL.Message(new MessageEvent(DATA));
								}
							}
						}
					}
				}
				ITERATOR.remove();
			}
		}
	}
}

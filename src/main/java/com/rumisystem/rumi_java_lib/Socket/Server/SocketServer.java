package com.rumisystem.rumi_java_lib.Socket.Server;

import com.rumisystem.rumi_java_lib.Socket.Server.CONNECT_EVENT.CONNECT_EVENT;
import com.rumisystem.rumi_java_lib.Socket.Server.CONNECT_EVENT.CONNECT_EVENT_LISTENER;
import com.rumisystem.rumi_java_lib.Socket.Server.EVENT.CloseEvent;
import com.rumisystem.rumi_java_lib.Socket.Server.EVENT.EVENT_LISTENER;
import com.rumisystem.rumi_java_lib.Socket.Server.EVENT.MessageEvent;

import javax.swing.event.EventListenerList;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.SocketException;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;


public class SocketServer {
	public EventListenerList CONNECT_EL_LIST = new EventListenerList();
	public EventListenerList EL_LIST = new EventListenerList();
	public HashMap<Integer, String> CEL_LIST = new HashMap<>();		//接続後のイベントリスナーのhashCodeとUUIDを保存

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
						EL.CONNECT(new CONNECT_EVENT(String.valueOf(SES.hashCode()), SES, EL_LIST, CEL_LIST, this));
					}
				} else if (KEY.isReadable()) {
					try {
						SocketChannel SES = (SocketChannel) KEY.channel();
						ByteBuffer BUFFER = ByteBuffer.allocate(256);
						int BYTE_READ = SES.read(BUFFER);

						if (BYTE_READ == -1) {
							//切断
							SES.close();

							Close(SES);
						} else {
							//受信
							BUFFER.flip();
							byte[] DATA = new byte[BUFFER.remaining()];
							BUFFER.get(DATA);

							//文字列に変換(そして語尾の改行コードを潰す)
							String S = new String(DATA).replaceAll("[\r\n]+$", "");
							String[] ULINE = null;

							//\rが有るなら\r\nで分割
							if (S.contains("\r")) {
								ULINE = S.split("\r\n");
							} else {
								ULINE = S.split("\n");
							}

							//改行で分けたやつを順番にイベント発火
							for (String LINE:ULINE) {
								byte[] BYTE_DATA = LINE.getBytes();
								//イベント発火
								EVENT_LISTENER[] ELL = EL_LIST.getListeners(EVENT_LISTENER.class);
								for (EVENT_LISTENER EL:ELL) {
									if (CEL_LIST.get(EL.hashCode()) != null) {
										if (CEL_LIST.get(EL.hashCode()).equals(String.valueOf(SES.hashCode()))) {
											EL.Message(new MessageEvent(BYTE_DATA));
										}
									}
								}
							}
						}
					} catch (SocketException EX) {
						//接続が切れた
					}catch (Exception EX) {
						EX.printStackTrace();
					}
				}
				ITERATOR.remove();
			}
		}
	}

	//切断時に必要な処理
	public void Close(SocketChannel SES) {
		//イベント発火
		EVENT_LISTENER[] ELL = EL_LIST.getListeners(EVENT_LISTENER.class);
		for (EVENT_LISTENER EL:ELL) {
			if (CEL_LIST.get(EL.hashCode()) != null) {
				if (CEL_LIST.get(EL.hashCode()).equals(String.valueOf(SES.hashCode()))) {
					EL.Close(new CloseEvent());
				}
			}
		}

		//CEL_LISTのイテレーターを作る(之をしないとエラー出る(java.util.ConcurrentModificationException))
		Iterator<HashMap.Entry<Integer, String>> CEL_ITERATOR = CEL_LIST.entrySet().iterator();

		//CEL_LISTから削除
		while (CEL_ITERATOR.hasNext()) {
			HashMap.Entry<Integer, String> ENTRY = CEL_ITERATOR.next();
			if (String.valueOf(SES.hashCode()).equals(ENTRY)) {
				//イテレーターを介することで、例のエラーは出なくなる
				CEL_ITERATOR.remove();
			}
		}
	}
}

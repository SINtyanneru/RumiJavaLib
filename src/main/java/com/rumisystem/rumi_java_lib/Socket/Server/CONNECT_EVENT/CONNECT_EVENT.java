package com.rumisystem.rumi_java_lib.Socket.Server.CONNECT_EVENT;

import com.rumisystem.rumi_java_lib.Socket.Server.EVENT.EVENT_LISTENER;

import javax.swing.event.EventListenerList;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.util.HashMap;

public class CONNECT_EVENT {
	private String ID = "";
	private SocketChannel SES = null;
	private HashMap<Integer, String> CEL_LIST = null;
	private EventListenerList EL_LIST = null;

	public CONNECT_EVENT(String ID, SocketChannel SES, EventListenerList EL_LIST, HashMap<Integer, String> CEL_LIST) {
		this.ID = ID;
		this.SES = SES;
		this.EL_LIST = EL_LIST;
		this.CEL_LIST = CEL_LIST;
	}

	public void setEventListener(EVENT_LISTENER EL) {
		EL_LIST.add(EVENT_LISTENER.class, EL);
		CEL_LIST.put(EL.hashCode(), ID);
	}

	/**
	 * メッセージを送信します
	 * @param MSG テキスト
	 * @throws IOException
	 */
	public void sendMessage(String MSG) throws IOException {
		SES.write(ByteBuffer.wrap(MSG.getBytes()));
	}


	/**
	 * 接続元のIPアドレスを取得します
	 * @return IP
	 * @throws IOException
	 */
	public String getIP() throws IOException {
		return  SES.getRemoteAddress().toString();
	}

	public void close() {
		try {
			SES.close();
		} catch (Exception EX) {
			//握り潰すことにした
		}
	}
}

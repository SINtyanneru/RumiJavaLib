package su.rumishistem.rumi_java_lib.Socket.Server.CONNECT_EVENT;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import su.rumishistem.rumi_java_lib.Socket.Server.EVENT.EVENT_LISTENER;
import su.rumishistem.rumi_java_lib.Socket.Server.SocketServer;

import javax.swing.event.EventListenerList;
import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.util.HashMap;

public class CONNECT_EVENT {
	private String ID = "";
	private ChannelHandlerContext CTX = null;
	private HashMap<Integer, String> CEL_LIST = null;
	private EventListenerList EL_LIST = null;

	public CONNECT_EVENT(String ID, ChannelHandlerContext CTX, EventListenerList EL_LIST, HashMap<Integer, String> CEL_LIST, SocketServer SS) {
		this.ID = ID;
		this.CTX = CTX;
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
		ByteBuf BB = Unpooled.copiedBuffer(MSG.getBytes());
		CTX.writeAndFlush(BB);
	}


	/**
	 * 接続元のIPアドレスを取得します
	 * @return IP
	 * @throws IOException
	 */
	public String getIP() throws IOException {
		InetSocketAddress CLIENT_ADDRESS = (InetSocketAddress) CTX.channel().remoteAddress();
		InetAddress ADDRESS = CLIENT_ADDRESS.getAddress();

		return ADDRESS.getHostAddress();
	}

	/**
	 * 接続を切断します
	 */
	public void close() {
		try {
			CTX.channel().close();
		} catch (Exception EX) {
			//握り潰すことにした
		}
	}
}

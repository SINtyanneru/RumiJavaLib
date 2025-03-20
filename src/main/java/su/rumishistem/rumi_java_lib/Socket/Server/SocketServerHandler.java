package su.rumishistem.rumi_java_lib.Socket.Server;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import su.rumishistem.rumi_java_lib.Socket.Server.CONNECT_EVENT.CONNECT_EVENT_LISTENER;
import su.rumishistem.rumi_java_lib.Socket.Server.CONNECT_EVENT.CONNECT_EVENT;
import su.rumishistem.rumi_java_lib.Socket.Server.EVENT.EVENT_LISTENER;
import su.rumishistem.rumi_java_lib.Socket.Server.EVENT.MessageEvent;
import su.rumishistem.rumi_java_lib.Socket.Server.EVENT.ReceiveEvent;

import javax.swing.event.EventListenerList;
import java.util.HashMap;
import java.util.UUID;

public class SocketServerHandler extends ChannelInboundHandlerAdapter {
	private SocketServer SS = null;
	private String ID = null;
	private StringBuilder ReceiveSB = new StringBuilder();

	public SocketServerHandler(SocketServer SS) {
		this.SS = SS;
		this.ID = UUID.randomUUID().toString();
	}

	@Override
	public void channelActive(ChannelHandlerContext CTX) throws Exception {
		super.channelActive(CTX);

		//接続されたイベントを発行
		CONNECT_EVENT_LISTENER[] ELL = SS.CONNECT_EL_LIST.getListeners(CONNECT_EVENT_LISTENER.class);
		for (CONNECT_EVENT_LISTENER EL:ELL) {
			EL.CONNECT(new CONNECT_EVENT(ID, CTX, SS.EL_LIST, SS.CEL_LIST, SS));
		}
	}

	@Override
	public void channelRead(ChannelHandlerContext CTX, Object MSG) {
		ByteBuf BB = (ByteBuf) MSG;
		String Text = BB.toString(io.netty.util.CharsetUtil.UTF_8);
		byte[] ByteArray = new byte[BB.readableBytes()];
		BB.readBytes(ByteArray);

		ReceiveSB.append(Text);

		//受信イベント発火
		EVENT_LISTENER[] ELL = SS.EL_LIST.getListeners(EVENT_LISTENER.class);
		for (EVENT_LISTENER EL:ELL) {
			if (SS.CEL_LIST.get(EL.hashCode()) != null) {
				if (SS.CEL_LIST.get(EL.hashCode()).equals(ID)) {
					EL.Receive(new ReceiveEvent(ByteArray));
				}
			}
		}

		//改行が有るならメッセージイベント
		if (ReceiveSB.lastIndexOf("\n") != -1) {
			String S = ReceiveSB.toString();
			String[] ULINE = null;

			ULINE = S.split("\r?\n");

			for (String LINE : ULINE) {
				byte[] BYTE_DATA = LINE.getBytes();
				for (EVENT_LISTENER EL : ELL) {
					if (SS.CEL_LIST.get(EL.hashCode()) != null) {
						if (SS.CEL_LIST.get(EL.hashCode()).equals(ID)) {
							EL.Message(new MessageEvent(BYTE_DATA));
						}
					}
				}
			}

			//メッセージ発火後にバッファーをクリア
			ReceiveSB = new StringBuilder();
		}
	}

	@Override
	public void channelInactive(ChannelHandlerContext CTX) throws Exception {
		super.channelInactive(CTX);
	}
}

package su.rumishistem.rumi_java_lib.WebSocket.Server;

import static su.rumishistem.rumi_java_lib.LOG_PRINT.Main.LOG;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.HttpHeaders;
import io.netty.handler.codec.http.websocketx.*;
import kotlin.text.Charsets;
import su.rumishistem.rumi_java_lib.LOG_PRINT.LOG_TYPE;
import su.rumishistem.rumi_java_lib.SnowFlake;
import su.rumishistem.rumi_java_lib.WebSocket.Server.CONNECT_EVENT.CONNECT_EVENT;
import su.rumishistem.rumi_java_lib.WebSocket.Server.CONNECT_EVENT.CONNECT_EVENT_LISTENER;
import su.rumishistem.rumi_java_lib.WebSocket.Server.EVENT.CLOSE_EVENT;
import su.rumishistem.rumi_java_lib.WebSocket.Server.EVENT.MESSAGE_EVENT;
import su.rumishistem.rumi_java_lib.WebSocket.Server.EVENT.WS_EVENT_LISTENER;

import java.util.HashMap;

public class WebSocketFrameHandler extends SimpleChannelInboundHandler<WebSocketFrame> {
	private WebSocketSERVER S;

	public WebSocketFrameHandler(WebSocketSERVER S) {
		this.S = S;
	}

	@Override
	protected void channelRead0(ChannelHandlerContext C, WebSocketFrame F) throws Exception {
		String ID = C.channel().id().asLongText();

		if (F instanceof TextWebSocketFrame) {
			//文章
			String Text = ((TextWebSocketFrame) F).text();
			//メッセージ受信イベント
			WS_EVENT_LISTENER[] ELL = S.EL_LIST.getListeners(WS_EVENT_LISTENER.class);
			for (WS_EVENT_LISTENER EL:ELL) {
				if (S.CEL_LIST.get(EL.hashCode()).equals(ID)) {
					EL.MESSAGE(new MESSAGE_EVENT(Text.getBytes(Charsets.UTF_8)));
				}
			}
		} else if (F instanceof BinaryWebSocketFrame) {
			ByteBuf buffer = ((BinaryWebSocketFrame)F).content();
			byte[] data = new byte[buffer.readableBytes()];
			buffer.readBytes(data);

			//メッセージ受信イベント
			WS_EVENT_LISTENER[] ELL = S.EL_LIST.getListeners(WS_EVENT_LISTENER.class);
			for (WS_EVENT_LISTENER EL:ELL) {
				if (S.CEL_LIST.get(EL.hashCode()).equals(ID)) {
					EL.MESSAGE(new MESSAGE_EVENT(data));
				}
			}
		} else if (F instanceof CloseWebSocketFrame) {
			//切断
			//セッションリストから消す
			S.SESSION_LIST.remove(ID);

			//クローズイベント
			WS_EVENT_LISTENER[] ELL = S.EL_LIST.getListeners(WS_EVENT_LISTENER.class);
			for (WS_EVENT_LISTENER EL:ELL) {
				if (S.CEL_LIST.get(EL.hashCode()).equals(ID)) {
					EL.CLOSE(new CLOSE_EVENT("", 0));
				}
			}

			C.channel().writeAndFlush(F.retain()).addListener(Future -> C.close());
		} else if (F instanceof PingWebSocketFrame) {
			//Ping
			C.channel().writeAndFlush(new PongWebSocketFrame(F.content().retain()));
		} else if (F instanceof PongWebSocketFrame) {
			//Pong
		} else {
			LOG(LOG_TYPE.INFO, "非対応なフレームです:" + F.getClass().getSimpleName());
		}
	}

	@Override
	public void channelInactive(ChannelHandlerContext C) throws Exception {
		//切断

		String ID = C.channel().id().asLongText();

		//セッションリストから消す
		S.SESSION_LIST.remove(ID);

		//クローズイベント
		WS_EVENT_LISTENER[] ELL = S.EL_LIST.getListeners(WS_EVENT_LISTENER.class);
		for (WS_EVENT_LISTENER EL:ELL) {
			if (S.CEL_LIST.get(EL.hashCode()).equals(ID)) {
				EL.CLOSE(new CLOSE_EVENT("", 0));
			}
		}

		super.channelInactive(C);
	}

	@Override
	public void userEventTriggered(ChannelHandlerContext C, Object e) throws Exception {
		if (e == WebSocketServerProtocolHandler.ServerHandshakeStateEvent.HANDSHAKE_COMPLETE) {
			String ID = C.channel().id().asLongText();

			LOG(LOG_TYPE.INFO, "WebSocket New Session " + ID);

			S.SESSION_LIST.put(ID, C);

			//HttpHeaders header = ((WebSocketServerProtocolHandler.HandshakeComplete) e).requestHeaders();

			HashMap<String, String> uri_param = new HashMap<>();
			String uri = ((WebSocketServerProtocolHandler.HandshakeComplete) e).requestUri();
			if (uri.contains("?")) {
				String param = uri.substring(uri.indexOf("?") + 1);
				String[] param_list = param.split("&");
				for (String row:param_list) {
					String[] kv = row.split("=", 2);
					uri_param.put(kv[0], (kv.length > 1 ? kv[1]:""));
				}
			}

			//コネクトイベント
			CONNECT_EVENT_LISTENER[] ELL = S.CONNECT_EL_LIST.getListeners(CONNECT_EVENT_LISTENER.class);
			for (CONNECT_EVENT_LISTENER EL:ELL) {
				EL.CONNECT_EVENT(new CONNECT_EVENT(C.channel().remoteAddress().toString(), ID, uri_param));
			}
		} else {
			super.userEventTriggered(C, e);
		}
	}
}

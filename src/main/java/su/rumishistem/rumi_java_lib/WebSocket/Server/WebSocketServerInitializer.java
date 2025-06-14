package su.rumishistem.rumi_java_lib.WebSocket.Server;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.codec.http.websocketx.WebSocketServerProtocolHandler;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;

public class WebSocketServerInitializer extends ChannelInitializer<SocketChannel> {
	private WebSocketSERVER S;

	public WebSocketServerInitializer(WebSocketSERVER S) {
		this.S = S;
	}

	@Override
	protected void initChannel(SocketChannel Ch) throws Exception {
		ChannelPipeline PL = Ch.pipeline();
		PL.addLast(new HttpServerCodec());
		PL.addLast(new HttpObjectAggregator(65536));
		PL.addLast(new WebSocketServerProtocolHandler(
			"/",
			null,
			true,
			65536,
			false,
			true,
			10000
		));
		PL.addLast(new WebSocketFrameHandler(S));
		/*PL.addLast(new LoggingHandler(LogLevel.INFO));*/
	}
}

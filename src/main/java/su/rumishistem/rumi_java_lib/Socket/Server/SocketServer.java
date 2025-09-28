package su.rumishistem.rumi_java_lib.Socket.Server;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import io.netty.handler.ssl.SslContext;
import io.netty.handler.ssl.SslContextBuilder;
import su.rumishistem.rumi_java_lib.LOG_PRINT.LOG_TYPE;
import su.rumishistem.rumi_java_lib.Socket.Server.CONNECT_EVENT.CONNECT_EVENT_LISTENER;

import javax.net.ssl.SSLException;
import javax.swing.event.EventListenerList;
import java.io.File;
import java.util.HashMap;
import static su.rumishistem.rumi_java_lib.LOG_PRINT.Main.LOG;

public class SocketServer {
	public EventListenerList CONNECT_EL_LIST = new EventListenerList();
	public EventListenerList EL_LIST = new EventListenerList();
	public HashMap<Integer, String> CEL_LIST = new HashMap<>();		//接続後のイベントリスナーのhashCodeとUUIDを保存
	private SocketServer SS = null;
	protected SslContext SSLC = null;
	protected boolean DefaultTLS = false;
	protected boolean message = true;

	public SocketServer() {
		SS = this;
	}

	public void setEventListener(CONNECT_EVENT_LISTENER EL) {
		//追加
		CONNECT_EL_LIST.add(CONNECT_EVENT_LISTENER.class, EL);
	}

	public void set_message(boolean message) {
		this.message = message;
	}

	public void setSSLSetting(String Cert, String PrivateKey, String[] TLSVersion) throws SSLException {
		File CertFile = new File(Cert);
		File PrivateKeyFile = new File(PrivateKey);

		if ((!CertFile.exists()) || (!PrivateKeyFile.exists())) {
			throw new Error("TLSに必要なファイルが見つかりません");
		}

		SSLC = SslContextBuilder.forServer(CertFile, PrivateKeyFile).protocols(TLSVersion).build();
	}

	public void setDefaultTLS() {
		DefaultTLS = true;
	}

	public void START(int PORT) throws InterruptedException {
		EventLoopGroup BossGroup = new NioEventLoopGroup();
		EventLoopGroup WorkerGroup = new NioEventLoopGroup();

		try {
			//サーバーのメイン部分
			ServerBootstrap B = new ServerBootstrap();
			B.group(BossGroup, WorkerGroup);
			B.channel(NioServerSocketChannel.class);
			B.childHandler(new ChannelInitializer<SocketChannel>() {
				@Override
				protected void initChannel(SocketChannel Ch) {
					if (DefaultTLS) {
						Ch.pipeline().addLast(SSLC.newHandler(Ch.alloc()));
					}

					Ch.pipeline().addLast(new SocketServerHandler(SS));

					Ch.pipeline().addLast(new LoggingHandler(LogLevel.INFO));
				}
			});

			//サーバー起動
			ChannelFuture CF = B.bind(PORT).sync();
			LOG(LOG_TYPE.OK, "Telnet Server Start localhost:" + PORT);

			//シャットダウンまで停止
			CF.channel().closeFuture().sync();
		} finally {
			//リソース開放
			BossGroup.shutdownGracefully();
			WorkerGroup.shutdownGracefully();
		}
	}
}

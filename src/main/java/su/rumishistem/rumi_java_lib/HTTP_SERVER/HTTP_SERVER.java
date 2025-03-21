package su.rumishistem.rumi_java_lib.HTTP_SERVER;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.http.HttpServerCodec;
import su.rumishistem.rumi_java_lib.LOG_PRINT.LOG_TYPE;
import javax.swing.event.EventListenerList;
import static su.rumishistem.rumi_java_lib.LOG_PRINT.Main.LOG;

public class HTTP_SERVER {
	private HTTP_SERVER HS = null;
	private int PORT;
	private int ThreadNum = 1;

	public boolean VERBOSE = false;
	public EventListenerList EL_LIST = new EventListenerList();

	public HTTP_SERVER(int PORT){
		this.PORT = PORT;
		this.HS = this;
	}

	//詳細なログを吐くかを設定する
	public void setVERBOSE(boolean VERBOSE) {
		this.VERBOSE = VERBOSE;
	}

	//HTTPリクエストの受信先を指定する
	public void SET_EVENT_VOID(HTTP_EVENT_LISTENER EVL){
		EL_LIST.add(HTTP_EVENT_LISTENER.class, EVL);
	}

	//スレッド数を指定する
	public void SetThreadNum(int ThreadNum) {
		this.ThreadNum = ThreadNum;
	}

	//HTTPサーバーを実行する
	public void START_HTTPSERVER() throws InterruptedException {
		NioEventLoopGroup BossGroup = new NioEventLoopGroup(ThreadNum);
		NioEventLoopGroup WorkerGroup = new NioEventLoopGroup();

		try {
			ServerBootstrap BootStrap = new ServerBootstrap();
			BootStrap.group(BossGroup, WorkerGroup);
			BootStrap.channel(NioServerSocketChannel.class);
			BootStrap.childHandler(new ChannelInitializer<SocketChannel>() {
				@Override
				protected void initChannel(SocketChannel Ch) throws Exception {
					Ch.pipeline().addLast(new HttpServerCodec());
					Ch.pipeline().addLast(new HTTPHandler(HS));
				}
			});

			//サーバー起動
			ChannelFuture CF = BootStrap.bind(PORT).sync();
			LOG(LOG_TYPE.OK, "Started HTTP Server!");
			LOG(LOG_TYPE.OK, "Port:" + PORT);

			//落ちるまで待つ
			CF.channel().closeFuture().sync();
		} finally {
			BossGroup.shutdownGracefully();
			WorkerGroup.shutdownGracefully();
		}
	}
}

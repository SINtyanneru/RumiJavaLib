package su.rumishistem.rumi_java_lib.HTTP_SERVER;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;
import su.rumishistem.rumi_java_lib.LOG_PRINT.LOG_TYPE;
import javax.swing.event.EventListenerList;
import static su.rumishistem.rumi_java_lib.LOG_PRINT.Main.LOG;

public class HTTP_SERVER {
	private final int MaxContentLength = 65536;

	private HTTP_SERVER HS = null;
	private int PORT;
	private int ThreadNum = 1;

	public boolean VERBOSE = false;
	public EventListenerList EL_LIST = new EventListenerList();

	public HTTP_SERVER(int PORT){
		this.PORT = PORT;
		this.HS = this;
	}

	/**
	 * 詳細なログを吐くかを設定します
	 * @param VERBOSE
	 */
	public void setVERBOSE(boolean VERBOSE) {
		this.VERBOSE = VERBOSE;
	}


	//HTTPリクエストの受信先を指定する
	public void SET_EVENT_VOID(HTTP_EVENT_LISTENER EVL){
		EL_LIST.add(HTTP_EVENT_LISTENER.class, EVL);
	}

	//スレッド数を指定するやつ
	public void SetThreadNum(int ThreadNum) {
		this.ThreadNum = ThreadNum;
	}

	//HTTP鯖を実行する
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
					Ch.pipeline().addLast(new HttpObjectAggregator(MaxContentLength));
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

	/*
	private class HTTPHandler implements HttpHandler {
		@Override
		public void handle(HttpExchange EXCHANGE) {
			try {
				//詳細なログを求めているならログを吐く
				if (VERBOSE) {
					//禁止ワードが含まれていないならログを吐く
					if(!EXCHANGE.getRequestURI().toString().contains("SESSION")){
						LOG(LOG_TYPE.INFO, "HTTP Request:" + EXCHANGE.getRequestMethod() + " " + EXCHANGE.getRequestURI());
					} else {
						LOG(LOG_TYPE.INFO, "HTTP Request:" + EXCHANGE.getRequestMethod() + " ▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓");
					}
				}

				//URIパラメーターを解析するやつ
				HashMap<String, String> URI_PARAM_HM = new HashMap<String, String>();//URIパラメーターを解析した結果のハッシュマップ
				int MARK_INDEX = EXCHANGE.getRequestURI().toString().indexOf("?");
				if (MARK_INDEX != -1) {//?が見つかった場合
					//?以降の文字列を取得
					String[] URI_PARAM_ARRAY = EXCHANGE.getRequestURI().toString().substring(MARK_INDEX + 1).split("&");
					for (String URI_PARAM : URI_PARAM_ARRAY) {
						String[] SPLIT = URI_PARAM.split("=");
						//ヌルチェック
						if (SPLIT.length == 2) {
							URI_PARAM_HM.put(SPLIT[0], SPLIT[1]);
						}
					}
				}

				//POSTされたデータ
				byte[] POST_DATA = null;
				if ("POST".equals(EXCHANGE.getRequestMethod()) || "PATCH".equals(EXCHANGE.getRequestMethod()) || "PUT".equals(EXCHANGE.getRequestMethod())) {
					InputStream IS = EXCHANGE.getRequestBody();
					ByteArrayOutputStream BAOS = new ByteArrayOutputStream();
					byte[] BUFFER = new byte[1024];
					int BYTE_READ;
					while ((BYTE_READ = IS.read(BUFFER, 0, BUFFER.length)) != -1) {
						BAOS.write(BUFFER, 0, BYTE_READ);
					}
					POST_DATA = BAOS.toByteArray();
				}

				//リクエストヘッダーを取得
				Map<String, List<String>> HEADER_TEMP = EXCHANGE.getRequestHeaders();
				HashMap<String, String> HEADER_DATA = new HashMap<>();
				for (Map.Entry<String, List<String>> entry : HEADER_TEMP.entrySet()) {
					String HEADER_KEY = entry.getKey();
					List<String> HEADER_VAL = entry.getValue();
					//データをhashMapに入れるのだちんちんまーんこ
					HEADER_DATA.put(HEADER_KEY.toUpperCase(), String.join(", ", HEADER_VAL));
				}


				//イベントを発火する
				HTTP_EVENT_LISTENER[] LISTENER_LIST = EL_LIST.getListeners(HTTP_EVENT_LISTENER.class);
				for(HTTP_EVENT_LISTENER LISTENER:LISTENER_LIST){
					LISTENER.REQUEST_EVENT(new HTTP_EVENT(this, EXCHANGE, URI_PARAM_HM, POST_DATA, HEADER_DATA));
				}
			} catch (Exception EX) {
				if (VERBOSE) {
					String EX_TEXT = EXCEPTION_READER.READ(EX);
					LOG(LOG_TYPE.FAILED, "----------<HTTP Err!>----------");
					for (String LINE:EX_TEXT.split("\n")) {
						LOG(LOG_TYPE.FAILED, LINE);
					}
					LOG(LOG_TYPE.FAILED, "-------------------------------");
				}
			}
		}
	}*/
}

package su.rumishistem.rumi_java_lib.Socket.Server;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.channel.socket.SocketChannel;
import su.rumishistem.rumi_java_lib.LOG_PRINT.LOG_TYPE;
import su.rumishistem.rumi_java_lib.Socket.Server.CONNECT_EVENT.CONNECT_EVENT_LISTENER;
import javax.swing.event.EventListenerList;
import java.util.HashMap;
import static su.rumishistem.rumi_java_lib.LOG_PRINT.Main.LOG;


public class SocketServer {
	public EventListenerList CONNECT_EL_LIST = new EventListenerList();
	public EventListenerList EL_LIST = new EventListenerList();
	public HashMap<Integer, String> CEL_LIST = new HashMap<>();		//接続後のイベントリスナーのhashCodeとUUIDを保存
	private SocketServer SS = null;

	public SocketServer() {
		SS = this;
	}

	public void setEventListener(CONNECT_EVENT_LISTENER EL) {
		//追加
		CONNECT_EL_LIST.add(CONNECT_EVENT_LISTENER.class, EL);
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
					Ch.pipeline().addLast(new SocketServerHandler(SS));
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

		/*
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
							EVENT_LISTENER[] ELL = EL_LIST.getListeners(EVENT_LISTENER.class);

							//受信イベント発火
							for (EVENT_LISTENER EL:ELL) {
								if (CEL_LIST.get(EL.hashCode()) != null) {
									if (CEL_LIST.get(EL.hashCode()).equals(String.valueOf(SES.hashCode()))) {
										EL.Receive(new ReceiveEvent(DATA));
									}
								}
							}

							//バッファーにばぼーん
							StringBuilder RECEIVE_SB = RECEIVE_BUFFER.getOrDefault(SES, new StringBuilder());
							RECEIVE_SB.append(new String(DATA));

							//最後が改行で終わってるなら、Messageイベントを発火する
							if (RECEIVE_SB.lastIndexOf("\n") != -1) {
								String S = RECEIVE_SB.toString();
								String[] ULINE = null;

								ULINE = S.split("\r?\n");

								for (String LINE : ULINE) {
									byte[] BYTE_DATA = LINE.getBytes();
									for (EVENT_LISTENER EL : ELL) {
										if (CEL_LIST.get(EL.hashCode()) != null) {
											if (CEL_LIST.get(EL.hashCode()).equals(String.valueOf(SES.hashCode()))) {
												EL.Message(new MessageEvent(BYTE_DATA));
											}
										}
									}
								}
								// メッセージ発火後にバッファーをクリア
								RECEIVE_BUFFER.put(SES, new StringBuilder());
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
		*/
	}

	/*
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

		RECEIVE_BUFFER.remove(SES);
	}
	 */
}

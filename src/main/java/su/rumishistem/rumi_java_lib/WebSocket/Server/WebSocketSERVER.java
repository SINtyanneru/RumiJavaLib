package su.rumishistem.rumi_java_lib.WebSocket.Server;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import su.rumishistem.rumi_java_lib.LOG_PRINT.LOG_TYPE;
import su.rumishistem.rumi_java_lib.SnowFlake;
import su.rumishistem.rumi_java_lib.Socket.Server.EVENT.CloseEvent;
import su.rumishistem.rumi_java_lib.Socket.Server.EVENT.EVENT_LISTENER;
import su.rumishistem.rumi_java_lib.Socket.Server.EVENT.MessageEvent;
import su.rumishistem.rumi_java_lib.Socket.Server.EVENT.ReceiveEvent;
import su.rumishistem.rumi_java_lib.Socket.Server.SocketServer;
import su.rumishistem.rumi_java_lib.WebSocket.Client.WebSocketClient;
import su.rumishistem.rumi_java_lib.WebSocket.Server.EVENT.CLOSE_EVENT;
import su.rumishistem.rumi_java_lib.WebSocket.Server.EVENT.MESSAGE_EVENT;
import su.rumishistem.rumi_java_lib.WebSocket.Server.EVENT.WS_EVENT_LISTENER;
import su.rumishistem.rumi_java_lib.WebSocket.Server.CONNECT_EVENT.CONNECT_EVENT;
import su.rumishistem.rumi_java_lib.WebSocket.Server.CONNECT_EVENT.CONNECT_EVENT_LISTENER;

import javax.swing.event.EventListenerList;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Base64;
import java.util.HashMap;

import static su.rumishistem.rumi_java_lib.LOG_PRINT.Main.LOG;

public class WebSocketSERVER {
	public static EventListenerList CONNECT_EL_LIST = new EventListenerList();
	public static EventListenerList EL_LIST = new EventListenerList();

	public static HashMap<Integer, String> CEL_LIST = new HashMap<>();		//接続後のイベントリスナーのhashCodeとUUIDを保存
	public static HashMap<String, ChannelHandlerContext> SESSION_LIST = new HashMap<>();

	public void START(int PORT) throws InterruptedException {
		EventLoopGroup BossGroup = new NioEventLoopGroup(1);
		EventLoopGroup WorkerGroup = new NioEventLoopGroup();
		ServerBootstrap SBS = new ServerBootstrap();
		SBS.group(BossGroup, WorkerGroup);
		SBS.channel(NioServerSocketChannel.class);
		SBS.childHandler(new WebSocketServerInitializer(this));

		Channel Ch = SBS.bind(PORT).sync().channel();

		LOG(LOG_TYPE.OK, "0.0.0.0:" + PORT + " de WebSocketServer kidou");

		Ch.closeFuture().sync();
	}

	//イベントリスナーの受信先を設定するやつ
	public void SET_EVENT_VOID(CONNECT_EVENT_LISTENER EVL){
		CONNECT_EL_LIST.add(CONNECT_EVENT_LISTENER.class, EVL);
	}
}

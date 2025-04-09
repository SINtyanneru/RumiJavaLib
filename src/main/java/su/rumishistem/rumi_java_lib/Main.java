package su.rumishistem.rumi_java_lib;

import su.rumishistem.rumi_java_lib.HTTP_SERVER.HTTP_EVENT;
import su.rumishistem.rumi_java_lib.HTTP_SERVER.HTTP_EVENT_LISTENER;
import su.rumishistem.rumi_java_lib.HTTP_SERVER.HTTP_SERVER;
import su.rumishistem.rumi_java_lib.Loger.LogerSystem;
import su.rumishistem.rumi_java_lib.Misskey.Builder.NoteBuilder;
import su.rumishistem.rumi_java_lib.Misskey.Event.DisconnectEvent;
import su.rumishistem.rumi_java_lib.Misskey.Event.NewFollower;
import su.rumishistem.rumi_java_lib.Misskey.Event.NewNoteEvent;
import su.rumishistem.rumi_java_lib.Misskey.MisskeyClient;
import su.rumishistem.rumi_java_lib.Misskey.RESULT.LOGIN_RESULT;
import su.rumishistem.rumi_java_lib.REON4213.REON4213Parser;
import su.rumishistem.rumi_java_lib.REON4213.Type.VBlock;
import su.rumishistem.rumi_java_lib.SmartHTTP.ERRORCODE;
import su.rumishistem.rumi_java_lib.SmartHTTP.HTTP_REQUEST;
import su.rumishistem.rumi_java_lib.SmartHTTP.HTTP_RESULT;
import su.rumishistem.rumi_java_lib.SmartHTTP.SmartHTTP;
import su.rumishistem.rumi_java_lib.SmartHTTP.Type.EndpointEntrie;
import su.rumishistem.rumi_java_lib.Socket.Server.CONNECT_EVENT.CONNECT_EVENT;
import su.rumishistem.rumi_java_lib.Socket.Server.CONNECT_EVENT.CONNECT_EVENT_LISTENER;
import su.rumishistem.rumi_java_lib.Socket.Server.EVENT.CloseEvent;
import su.rumishistem.rumi_java_lib.Socket.Server.EVENT.EVENT_LISTENER;
import su.rumishistem.rumi_java_lib.Socket.Server.EVENT.MessageEvent;
import su.rumishistem.rumi_java_lib.Socket.Server.EVENT.ReceiveEvent;
import su.rumishistem.rumi_java_lib.Socket.Server.SocketServer;
import su.rumishistem.rumi_java_lib.WebSocket.Server.EVENT.CLOSE_EVENT;
import su.rumishistem.rumi_java_lib.WebSocket.Server.EVENT.MESSAGE_EVENT;
import su.rumishistem.rumi_java_lib.WebSocket.Server.EVENT.WS_EVENT_LISTENER;
import su.rumishistem.rumi_java_lib.WebSocket.Server.WebSocketSERVER;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Base64;
import java.util.HashMap;
import java.util.function.Function;
import java.awt.*;

public class Main {
	public static void main(String[] args) {
		try {
			SmartHTTP SH = new SmartHTTP(3233);
			SH.SetRoute("/", EndpointEntrie.Method.GET, new Function<HTTP_REQUEST, HTTP_RESULT>() {
				@Override
				public HTTP_RESULT apply(HTTP_REQUEST httpRequest) {
					return new HTTP_RESULT(200, "".getBytes(), "text/plain; charset=UTF-8");
				}
			});
			SH.Start();

			/*
			MisskeyClient MC = new MisskeyClient("ussr.rumiserver.com");
			if (MC.TOKEN_LOGIN(args[0]) == LOGIN_RESULT.DONE) {
				MC.SET_EVENT_LISTENER(new EVENT_LISTENER() {
					@Override
					public void onReady() {
						try {
							System.out.println("サーバーに接続した");
							NoteBuilder NB = new NoteBuilder();
							NB.setTEXT("接続しました");
							NB.AddFile(new File("/home/rumisan/Pictures/remmina_クイック接続_192.168.100.16_20250223-131411.png"));
							MC.PostNote(NB.Build());
						} catch (Exception EX) {
							EX.printStackTrace();
						}
					}

					@Override
					public void onNewNote(NewNoteEvent E) {
						try{
							if (!E.getNOTE().isRN()) {
								if (!E.getNOTE().isKaiMention()) {
									System.out.println(E.getUSER().getNAME() + "さんのノート「" + E.getNOTE().getTEXT() + "」");
								} else {
									System.out.println(E.getUSER().getNAME() + "さんにメンションされました「" + E.getNOTE().getTEXT() + "」");
									MC.CreateReaction(E.getNOTE(), ":1039992459209490513:");
									NoteBuilder NB = new NoteBuilder();
									NB.setTEXT("はい！！");
									NB.setREPLY(E.getNOTE());

									MC.PostNote(NB.Build());
								}
							} else {
								System.out.println(E.getUSER().getNAME() + "さんがリノートしました");
							}
						} catch (Exception EX) {
							EX.printStackTrace();
						}
					}

					@Override
					public void onNewFollower(NewFollower E) {
						System.out.println("新しいフォロワー");
						System.out.println(E.getUser().Follow());
					}

					@Override
					public void onDisconnect(DisconnectEvent e) {

					}
				});
			} else {
				System.out.println("ログイン失敗");
			}*/
		} catch (Exception EX) {
			EX.printStackTrace();
		}
	}
}

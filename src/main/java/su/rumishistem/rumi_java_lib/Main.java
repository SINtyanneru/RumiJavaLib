package su.rumishistem.rumi_java_lib;

import su.rumishistem.rumi_java_lib.HTTP_SERVER.HTTP_EVENT;
import su.rumishistem.rumi_java_lib.Misskey.Builder.NoteBuilder;
import su.rumishistem.rumi_java_lib.Misskey.Event.EVENT_LISTENER;
import su.rumishistem.rumi_java_lib.Misskey.Event.NewFollower;
import su.rumishistem.rumi_java_lib.Misskey.Event.NewNoteEvent;
import su.rumishistem.rumi_java_lib.Misskey.MisskeyClient;
import su.rumishistem.rumi_java_lib.Misskey.RESULT.LOGIN_RESULT;
import su.rumishistem.rumi_java_lib.SmartHTTP.ERRORCODE;
import su.rumishistem.rumi_java_lib.SmartHTTP.HTTP_REQUEST;
import su.rumishistem.rumi_java_lib.SmartHTTP.HTTP_RESULT;
import su.rumishistem.rumi_java_lib.SmartHTTP.SmartHTTP;

import java.util.HashMap;
import java.util.function.Function;

public class Main {
	public static void main(String[] args) {
		try {
			SmartHTTP SH = new SmartHTTP(8088);

			SH.SetRoute("/", new Function<HTTP_REQUEST, HTTP_RESULT>() {
				@Override
				public HTTP_RESULT apply(HTTP_REQUEST REQ) {
					return new HTTP_RESULT(200, "Hello World".getBytes(), "text/plain; charset=UTF-8");
				}
			});

			SH.SetRoute("/hoge/:ID", new Function<HTTP_REQUEST, HTTP_RESULT>() {
				@Override
				public HTTP_RESULT apply(HTTP_REQUEST REQ) {
					return new HTTP_RESULT(200, ("ほげほげなIDは" + REQ.GetParam("ID") + "だ！").getBytes(), "text/plain; charset=UTF-8");
				}
			});

			//エラー
			SH.SetError("/", ERRORCODE.PAGE_NOT_FOUND, new Function<HTTP_REQUEST, HTTP_RESULT>() {
				@Override
				public HTTP_RESULT apply(HTTP_REQUEST httpRequest) {
					return new HTTP_RESULT(404, "404だ".getBytes(), "text/plain; charset=UTF-8");
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
				});
			} else {
				System.out.println("ログイン失敗");
			}*/
		} catch (Exception EX) {
			EX.printStackTrace();
		}
	}
}

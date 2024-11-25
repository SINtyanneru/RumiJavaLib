package com.rumisystem.rumi_java_lib;

import com.rumisystem.rumi_java_lib.Misskey.Builder.NoteBuilder;
import com.rumisystem.rumi_java_lib.Misskey.Event.EVENT_LISTENER;
import com.rumisystem.rumi_java_lib.Misskey.Event.NewFollower;
import com.rumisystem.rumi_java_lib.Misskey.Event.NewNoteEvent;
import com.rumisystem.rumi_java_lib.Misskey.MisskeyClient;
import com.rumisystem.rumi_java_lib.Misskey.RESULT.LOGIN_RESULT;
import com.rumisystem.rumi_java_lib.WebSocket.Client.EVENT.CLOSE_EVENT;
import com.rumisystem.rumi_java_lib.WebSocket.Client.EVENT.CONNECT_EVENT;
import com.rumisystem.rumi_java_lib.WebSocket.Client.EVENT.MESSAGE_EVENT;
import com.rumisystem.rumi_java_lib.WebSocket.Client.EVENT.WS_EVENT_LISTENER;
import com.rumisystem.rumi_java_lib.WebSocket.Client.WebSocketClient;

public class Main {
	public static void main(String[] args) {
		try {
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
						if (!E.getNOTE().isRN()) {
							if (!E.getNOTE().isKaiMention()) {
								System.out.println(E.getUSER().getNAME() + "さんのノート「" + E.getNOTE().getTEXT() + "」");
							} else {
								System.out.println(E.getUSER().getNAME() + "さんにメンションされました「" + E.getNOTE().getTEXT() + "」");
							}
						} else {
							System.out.println(E.getUSER().getNAME() + "さんがリノートしました");
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
			}
		} catch (Exception EX) {
			EX.printStackTrace();
		}
	}
}

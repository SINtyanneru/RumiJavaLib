package com.rumisystem.rumi_java_lib;

import com.rumisystem.rumi_java_lib.Misskey.Builder.NoteBuilder;
import com.rumisystem.rumi_java_lib.Misskey.Event.EVENT_LISTENER;
import com.rumisystem.rumi_java_lib.Misskey.Event.NewNoteEvent;
import com.rumisystem.rumi_java_lib.Misskey.MisskeyClient;
import com.rumisystem.rumi_java_lib.Misskey.RESULT.LOGIN_RESULT;
import com.rumisystem.rumi_java_lib.Socket.Server.EVENT.CONNECT_EVENT;
import com.rumisystem.rumi_java_lib.Socket.Server.EVENT.MESSAGE_EVENT;
import com.rumisystem.rumi_java_lib.Socket.Server.SocketSERVER;

import javax.net.ssl.SSLServerSocket;
import javax.net.ssl.SSLSocket;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.ServerSocket;
import java.net.Socket;

public class Main {
	public static void main(String[] args) {
		try {
			TSL SSL = new TSL(
					"/home/rumisan/source/fullchain.pem",
					"/home/rumisan/source/privkey.pem",
					"smtp.rumiserver.com"
			);

			SSLServerSocket SS = SSL.CreateSocket(4545);
			while (true) {
				SSLSocket SOCKET = (SSLSocket) SS.accept();
				System.out.println("NEW Client");

				BufferedReader BR = new BufferedReader(new InputStreamReader(SOCKET.getInputStream()));
				BufferedWriter BW = new BufferedWriter(new OutputStreamWriter(SOCKET.getOutputStream()));

				String LINE;
				while ((LINE = BR.readLine()) != null) {
					System.out.println("←" + LINE);
				}
			}

			/*
			MisskeyClient MC = new MisskeyClient("ussr.rumiserver.com");
			if (MC.TOKEN_LOGIN("4LfqJNO9w8x1x6rD") == LOGIN_RESULT.DONE) {
				MC.SET_EVENT_LISTENER(new EVENT_LISTENER() {
					@Override
					public void onReady() {
						System.out.println("サーバーに接続した");
					}

					@Override
					public void onNewNote(NewNoteEvent E) {
						System.out.println(E.getUSER().getNAME() + "さんのノート「" + E.getNOTE().getTEXT() + "」");
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

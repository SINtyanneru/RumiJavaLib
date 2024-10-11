package com.rumisystem.rumi_java_lib;

import com.rumisystem.rumi_java_lib.Misskey.Builder.NoteBuilder;
import com.rumisystem.rumi_java_lib.Misskey.Event.NewNoteEvent;
import com.rumisystem.rumi_java_lib.Misskey.MisskeyClient;
import com.rumisystem.rumi_java_lib.Misskey.RESULT.LOGIN_RESULT;
import com.rumisystem.rumi_java_lib.SSH.Server.EVENT.Connect.Connect_EVENT_LISTENER;
import com.rumisystem.rumi_java_lib.SSH.Server.EVENT.EVENT_LISTENER;
import com.rumisystem.rumi_java_lib.SSH.Server.EVENT.SendEvent;
import com.rumisystem.rumi_java_lib.SSH.Server.SSHServer;
import com.rumisystem.rumi_java_lib.Socket.Server.SocketSERVER;
import com.rumisystem.rumi_java_lib.WebSocket.Server.CONNECT_EVENT.CONNECT_EVENT;
import com.rumisystem.rumi_java_lib.WebSocket.Server.CONNECT_EVENT.CONNECT_EVENT_LISTENER;
import com.rumisystem.rumi_java_lib.WebSocket.Server.EVENT.CLOSE_EVENT;
import com.rumisystem.rumi_java_lib.WebSocket.Server.EVENT.MESSAGE_EVENT;
import com.rumisystem.rumi_java_lib.WebSocket.Server.EVENT.WS_EVENT_LISTENER;
import com.rumisystem.rumi_java_lib.WebSocket.Server.WebSocketSERVER;
import org.apache.sshd.server.Environment;
import org.apache.sshd.server.ExitCallback;
import org.apache.sshd.server.SshServer;
import org.apache.sshd.server.channel.ChannelSession;
import org.apache.sshd.server.command.AbstractCommandSupport;
import org.apache.sshd.server.command.Command;
import org.apache.sshd.server.keyprovider.SimpleGeneratorHostKeyProvider;
import org.apache.sshd.server.shell.ShellFactory;

import javax.net.ssl.SSLServerSocket;
import javax.net.ssl.SSLSocket;
import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.file.Paths;
import java.util.Base64;

public class Main {
	public static void main(String[] args) {
		try {
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

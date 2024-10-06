package com.rumisystem.rumi_java_lib;

import com.rumisystem.rumi_java_lib.Misskey.Builder.NoteBuilder;
import com.rumisystem.rumi_java_lib.Misskey.Event.NewNoteEvent;
import com.rumisystem.rumi_java_lib.Misskey.MisskeyClient;
import com.rumisystem.rumi_java_lib.Misskey.RESULT.LOGIN_RESULT;
import com.rumisystem.rumi_java_lib.SSH.Server.EVENT.Connect.CONNECT_EVENT;
import com.rumisystem.rumi_java_lib.SSH.Server.EVENT.Connect.Connect_EVENT_LISTENER;
import com.rumisystem.rumi_java_lib.SSH.Server.EVENT.EVENT_LISTENER;
import com.rumisystem.rumi_java_lib.SSH.Server.EVENT.SendEvent;
import com.rumisystem.rumi_java_lib.SSH.Server.SSHServer;
import com.rumisystem.rumi_java_lib.Socket.Server.EVENT.MESSAGE_EVENT;
import com.rumisystem.rumi_java_lib.Socket.Server.SocketSERVER;
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
			SSHServer SSHD = new SSHServer(4545, "/etc/ssh/ssh_host_rsa_key");
			SSHD.SET_EVENT_LISTENER(new Connect_EVENT_LISTENER() {
				@Override
				public void CONNECT(CONNECT_EVENT SESSION) {
					System.out.println("新しい接続");

					SESSION.Send("るみSSHdへようこそ\r\n");
					SESSION.Send(">");

					StringBuilder LINE = new StringBuilder();
					SESSION.SET_EVENT_LISTENER(new EVENT_LISTENER() {
						@Override
						public void Send(SendEvent E) {
							try {
								switch (E.getTEXT()) {
									//改行
									case "\r":
									case "\n": {
										SESSION.Send("\r\n");
										SESSION.Send("Command:" + LINE);
										SESSION.Send("\r\n");
										SESSION.Send(">");

										LINE.delete(0, LINE.length());
										break;
									}

									case "\033": {
										if (E.MoreRead().equals("[")) {
											switch (String.valueOf(E.MoreRead())) {
												case "A":
												case "B": {
													break;
												}
											}
										}
										break;
									}

									case "\b": {
										break;
									}

									default: {
										LINE.append(E.getTEXT());
										SESSION.Send(E.getTEXT());
									}
								}
							} catch (Exception EX) {
								EX.printStackTrace();
							}
						}

						@Override
						public void Close() {
							System.out.println("切断された");
						}
					});
				}
			});

			SSHD.START();

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

package com.rumisystem.rumi_java_lib.SSH.Server;

import com.rumisystem.rumi_java_lib.SSH.Server.EVENT.Connect.CONNECT_EVENT;
import com.rumisystem.rumi_java_lib.SSH.Server.EVENT.Connect.Connect_EVENT_LISTENER;
import com.rumisystem.rumi_java_lib.SSH.Server.EVENT.EVENT_LISTENER;
import com.rumisystem.rumi_java_lib.SSH.Server.EVENT.SendEvent;
import org.apache.sshd.server.Environment;
import org.apache.sshd.server.ExitCallback;
import org.apache.sshd.server.SshServer;
import org.apache.sshd.server.channel.ChannelSession;
import org.apache.sshd.server.command.Command;
import org.apache.sshd.server.keyprovider.SimpleGeneratorHostKeyProvider;
import org.apache.sshd.server.shell.ShellFactory;

import javax.swing.event.EventListenerList;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintStream;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.UUID;

import static com.rumisystem.rumi_java_lib.LOG_PRINT.Main.LOG;
import static com.rumisystem.rumi_java_lib.Misskey.MisskeyClient.EL_LIST;

public class SSHServer {
	private int PORT = 0;
	private String KEY_PATH = "";
	public static HashMap<String, HashMap<String, Object>> SESSION_LIST = new HashMap<>();
	public static EventListenerList CONNECT_EL_LIST = new EventListenerList();
	public static EventListenerList EL_LIST = new EventListenerList();

	public SSHServer(int PORT, String KEY) {
		this.PORT = PORT;
		this.KEY_PATH = KEY;
	}

	public void SET_EVENT_LISTENER(Connect_EVENT_LISTENER EL) {
		CONNECT_EL_LIST.add(Connect_EVENT_LISTENER.class, EL);
	}

	public void START() throws IOException {
		SshServer SSHD = SshServer.setUpDefaultServer();
		SSHD.setPort(PORT);

		SimpleGeneratorHostKeyProvider KEY_PROVIDER = new SimpleGeneratorHostKeyProvider(Paths.get(KEY_PATH));
		SSHD.setKeyPairProvider(KEY_PROVIDER);

		SSHD.setPasswordAuthenticator((USERNAME, PASSWORD, SESSION) -> {
			return true;
		});

		SSHD.start();

		SSHD.setShellFactory(new ShellFactory() {
			@Override
			public Command createShell(ChannelSession channelSession) throws IOException {
				return new Command() {
					private InputStream BR;
					private PrintStream BW;

					@Override
					public void setExitCallback(ExitCallback exitCallback) {

					}

					@Override
					public void setErrorStream(OutputStream outputStream) {

					}

					@Override
					public void setInputStream(InputStream IS) {
						this.BR = IS;
					}

					@Override
					public void setOutputStream(OutputStream OS) {
						this.BW = new PrintStream(OS);
					}

					@Override
					public void start(ChannelSession channelSession, Environment environment) throws IOException {
						//新規接続
						String ID = UUID.randomUUID().toString();

						HashMap<String, Object> INFO = new HashMap<>();
						INFO.put("BR", BR);
						INFO.put("BW", BW);

						SESSION_LIST.put(ID, INFO);

						//イベント着火
						Connect_EVENT_LISTENER[] LISTENER_LIST = CONNECT_EL_LIST.getListeners(Connect_EVENT_LISTENER.class);
						for(Connect_EVENT_LISTENER LISTENER:LISTENER_LIST){
							LISTENER.CONNECT(new CONNECT_EVENT(ID));
						}

						new Thread(new Runnable() {
							@Override
							public void run() {
								try {
									int DATA;
									while ((DATA = BR.read()) != -1) {
										//イベント着火
										EVENT_LISTENER[] LISTENER_LIST = EL_LIST.getListeners(EVENT_LISTENER.class);
										for(EVENT_LISTENER LISTENER:LISTENER_LIST){
											if (SESSION_LIST.get(ID).get("ELL").equals(LISTENER.hashCode())) {
												LISTENER.Send(new SendEvent(ID, String.valueOf((char) DATA)));
											}
										}
									}
								} catch (Exception EX) {
									EX.printStackTrace();
								}
							}
						}).start();
					}

					@Override
					public void destroy(ChannelSession channelSession) throws Exception {

					}
				};
			}
		});

		while (true) {
			//こゃ
		}
	}
}

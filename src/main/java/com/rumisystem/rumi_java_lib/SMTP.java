package com.rumisystem.rumi_java_lib;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.Base64;
import javax.naming.directory.Attributes;
import javax.naming.directory.DirContext;
import javax.naming.directory.InitialDirContext;
import javax.net.ssl.SSLSocket;
import javax.net.ssl.SSLSocketFactory;
import javax.naming.directory.Attribute;

import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.UnknownHostException;
import java.util.Date;
import java.util.Hashtable;
import java.util.Locale;

public class SMTP {
	private String MAIL_DOMAIN = null;
	private String MAIL_FROM = null;
	private String MAIL_TO = null;
	private String MAIL_TEXT = "";
	private String MAIL_SUB = "";

	private BufferedReader BR = null;
	private BufferedWriter BW = null;
	private int PORT = 25;
	private Socket SOCKET;

	public SMTP(String DOMAIN, String FROM, String TO) throws IOException {
		this.MAIL_DOMAIN = DOMAIN;
		this.MAIL_FROM = FROM;
		this.MAIL_TO = TO;
	}

	public void ADD_TEXT(String TEXT) {
		MAIL_TEXT = MAIL_TEXT + TEXT;
	}

	public void SET_SUBJECT(String SUBJECT) {
		MAIL_SUB = SUBJECT;
	}

	public void SEND() throws IOException {
		StringBuilder MAIL_DATA = new StringBuilder();
		MAIL_DATA.append("From: " + MAIL_FROM + "\r\n");
		MAIL_DATA.append("To: " + MAIL_TO + "\r\n");
		MAIL_DATA.append("Date: " + new SimpleDateFormat("EEE, d MMM yyyy HH:mm:ss Z", Locale.ENGLISH).format(new Date()) + "\r\n");
		MAIL_DATA.append("Subject: =?UTF-8?B?" + BASE64(MAIL_SUB) + "?=\r\n");
		MAIL_DATA.append("MIME-Version: 1.0\r\n");
		MAIL_DATA.append("Content-Type: text/plain; charset=UTF-8\r\n");
		MAIL_DATA.append("Content-Transfer-Encoding: base64\r\n");
		MAIL_DATA.append("\r\n");
		MAIL_DATA.append(BASE64(MAIL_TEXT) + "\r\n");

		//SMTP鯖に接続する
		CONNECT_SMTP();

		//繋がったのでメール送信
		if (RUNCMD("MAIL FROM:<" + MAIL_FROM + ">", BR, BW).startsWith("250")) {
			if (RUNCMD("RCPT TO:<" + MAIL_TO + ">", BR, BW).startsWith("250")) {
				if (RUNCMD("DATA", BR, BW).startsWith("354")) {
					//メール本体
					BW.write(MAIL_DATA.toString() + "\r\n");
					BW.flush();

					String RESULT = RUNCMD(".", BR, BW);
					if (RESULT.startsWith("250")) {
						RUNCMD("QUIT", BR, BW);
					} else {
						RUNCMD("QUIT", BR, BW);
						throw new Error("メールを送信を確定できず");
					}
				} else {
					RUNCMD("QUIT", BR, BW);
					throw new Error("DATAでエラー");
				}
			} else {
				RUNCMD("QUIT", BR, BW);
				throw new Error("RCPT TOでエラー");
			}
		} else {
			RUNCMD("QUIT", BR, BW);
			throw new Error("MAIL FROMでエラー");
		}
	}

	//(略略るみSMTPから持ってきたコード)
	private void CONNECT_SMTP() throws UnknownHostException, IOException {
		SOCKET = new Socket(MAIL_DOMAIN, PORT);

		BR = new BufferedReader(new InputStreamReader(SOCKET.getInputStream()));
		BW = new BufferedWriter(new OutputStreamWriter(SOCKET.getOutputStream()));

		String FIRST_MSG = WAIT_MSG(BR, BW);

		if (FIRST_MSG.startsWith("220")) {
			String EHLO_RESULT = RUNCMD("EHLO rumiserver.com", BR, BW);

			//EHLOの返答の中にSTARTTLSがあるか
			if (EHLO_RESULT.contains("STARTTLS")) {
				//STARTTLS対応なのでSTARTTLSする
				if (RUNCMD("STARTTLS", BR, BW).startsWith("220")) {
					SSLSocketFactory SSLFACTORY = (SSLSocketFactory) SSLSocketFactory.getDefault();
					SSLSocket SSLS = (SSLSocket) SSLFACTORY.createSocket(SOCKET, MAIL_DOMAIN, PORT, true);

					//SSL化する
					SSLS.startHandshake();

					BR = new BufferedReader(new InputStreamReader(SSLS.getInputStream()));
					BW = new BufferedWriter(new OutputStreamWriter(SSLS.getOutputStream()));

					RUNCMD("EHLO rumiserver.com", BR, BW);
				} else {
					throw new Error("STARTTLSエラー");
				}
			} else {
				//STARTTLS非対応なのでそのままにする
			}
		} else {
			SOCKET.close();
		}
	}

	private String BASE64(String TEXT) {
		return Base64.getEncoder().encodeToString(TEXT.getBytes(StandardCharsets.UTF_8));
	}

	private String RUNCMD(String TEXT, BufferedReader BR, BufferedWriter BW) throws IOException {
		SEND_MSG(TEXT, BR, BW);
		return WAIT_MSG(BR, BW);
	}

	private void SEND_MSG(String TEXT, BufferedReader BR, BufferedWriter BW) throws IOException {
		BW.write(TEXT + "\r\n");
		BW.flush();
	}

	private String WAIT_MSG(BufferedReader BR, BufferedWriter BW) throws IOException {
		StringBuilder RESULT = new StringBuilder();
		String LINE;

		while ((LINE = BR.readLine()) != null) {
			RESULT.append(LINE);

			//長さは4以上ある
			if (LINE.length() > 4) {
				//ステータスコードの後が-ではない(例：X200-aaaa ○200 aaaa)
				if (!String.valueOf(LINE.charAt(3)).equals("-")) {
					break;
				}
			}
		}

		return RESULT.toString();
	}
}

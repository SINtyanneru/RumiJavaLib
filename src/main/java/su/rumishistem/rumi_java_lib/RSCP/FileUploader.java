package su.rumishistem.rumi_java_lib.RSCP;

import kotlin.text.Charsets;

import java.io.*;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class FileUploader {
	private String host;
	private String bucket;
	private String name;
	private boolean is_public;
	private Socket socket;
	private OutputStream out;
	private InputStream in;
	private MessageDigest md;

	private String version;

	public FileUploader(String host, int port, String bucket, String name, boolean is_public) throws IOException {
		this.host = host;
		this.bucket = bucket;
		this.name = name;
		this.is_public = is_public;

		if (bucket.length() > 254) {
			throw new RuntimeException("バケット名が254文字を超えている");
		}

		if (name.length() > 254) {
			throw new RuntimeException("ファイル名が254文字を超えている");
		}

		this.socket = new Socket(host, port);
		out = socket.getOutputStream();
		in = socket.getInputStream();

		byte[] welcome_message = in.readNBytes(5);
		/*String log = "";
		for (int i = 0; i < welcome_message.length; i++) {
			log += String.format("%02X ", welcome_message[i] & 0xFF);
		}
		System.out.println(log);*/

		if (welcome_message[0] == 0x52 && welcome_message[1] == 0x53 && welcome_message[2] == 0x43 && welcome_message[3] == 0x50) {
			int version_length = welcome_message[4] & 0xFF;
			byte[] version_data = in.readNBytes(version_length);
			version = new String(version_data, Charsets.UTF_8);
		} else {
			throw new HandshakeError();
		}
	}

	private void handshake(long size) throws IOException {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		baos.write(0x01);

		//バケット名
		baos.write((byte)(bucket.length() & 0xFF));
		baos.write(bucket.getBytes());

		//ファイル名
		baos.write((byte)(name.length() & 0xFF));
		baos.write(name.getBytes());

		//公開
		if (is_public) {
			baos.write(0x01);
		} else {
			baos.write(0x00);
		}

		//ファイルサイズ
		ByteBuffer bb = ByteBuffer.allocate(8);
		bb.order(ByteOrder.BIG_ENDIAN);
		bb.putLong(size);
		baos.write(bb.array());

		out.write(baos.toByteArray());
		out.flush();
		baos.close();

		int result = in.read() & 0xFF;
		if (result == 0x20) {
			try {
				md = MessageDigest.getInstance("MD5");
			} catch (NoSuchAlgorithmException ex) {
				//無視
			}
		} else {
			throw new RuntimeException("サーバーがエラーを返しました:" + String.format("%02X", result));
		}
	}

	public String get_version() {
		return version;
	}

	public void end() throws IOException, ChecksumError {
		out.write(md.digest());
		out.flush();

		if ((in.read() & 0xFF) == 0x20) {
			close();
		} else {
			throw new ChecksumError();
		}
	}

	public void close() {
		try {
			out.close();
			in.close();
			socket.close();
		} catch (IOException ex) {
			//？
		}
	}

	public void file(File f) throws IOException, ChecksumError {
		handshake(f.length());

		FileInputStream fis = new FileInputStream(f);
		byte[] buffer = new byte[8024];
		int length;
		while ((length = fis.read(buffer)) != -1) {
			write(buffer, length);
		}
		fis.close();

		end();
	}

	public void write(byte[] data) throws IOException {
		out.write(data);
		out.flush();

		md.update(data);
	}

	public void write(byte[] data, int length) throws IOException {
		out.write(data, 0, length);
		out.flush();

		md.update(data, 0, length);
	}
}

package su.rumishistem.rumi_java_lib.RSCP;

import kotlin.text.Charsets;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

public class RSCP {
	private static final int port = 41029;
	private String host;

	public RSCP(String host) {
		this.host = host;
	}

	public FileUploader upload(String bucket, String name, boolean is_public) throws IOException {
		return new FileUploader(host, port, bucket, name, is_public);
	}

	public void remove(String bucket, String name) throws IOException {
		if (bucket.length() > 254) {
			throw new RuntimeException("バケット名が254文字を超えている");
		}

		if (name.length() > 254) {
			throw new RuntimeException("ファイル名が254文字を超えている");
		}

		Socket socket = new Socket(host, port);
		OutputStream out = socket.getOutputStream();
		InputStream in = socket.getInputStream();

		try {
			//ようこそ
			byte[] welcome_message = in.readNBytes(5);
			if ((welcome_message[0] == 0x52 && welcome_message[1] == 0x53 && welcome_message[2] == 0x43 && welcome_message[3] == 0x50) == false) {
				throw new HandshakeError();
			}

			//バージョンを読み飛ばす
			int version_length = welcome_message[4] & 0xFF;
			in.readNBytes(version_length);

			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			baos.write(0x02);
			//バケット名
			baos.write((byte)(bucket.length() & 0xFF));
			baos.write(bucket.getBytes());
			//ファイル名
			baos.write((byte)(name.length() & 0xFF));
			baos.write(name.getBytes());

			out.write(baos.toByteArray());
			out.flush();

			int result = in.read() & 0xFF;
			if (result != 0x20) {
				throw new RuntimeException("サーバーがエラーを返しました:" + String.format("%02X", result));
			}
		} finally {
			out.close();
			in.close();
			socket.close();
		}
	}
}

package su.rumishistem.rumi_java_lib;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import org.apache.commons.fileupload.MultipartStream;
import java.io.ByteArrayInputStream;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class FormData {
	private String Boundary = "";
	private List<HashMap<String, Object>> PartList = new ArrayList<>();

	public FormData() {
		//FormDataFuckFuckにしたらだめだった
		Boundary = "----WebKitFormBoundary" + System.currentTimeMillis();
	}

	public FormData(byte[] Data, byte[] Boundary) throws IOException {
		//FormDataが意味和から無さすぎて解析だけライブラリ使いました死ね
		ByteArrayInputStream BAIS = new ByteArrayInputStream(Data);
		MultipartStream MS = new MultipartStream(BAIS, Boundary, 4096, null);

		while (MS.skipPreamble()) {
			String Header = MS.readHeaders();
			String PartName = null;
			String FileName = null;
			String MIME = "";

			//ヘッダーを読む
			Matcher FileMTC = Pattern.compile("name=\"(.*)\"; filename=\"(.*)\"").matcher(Header);
			if (FileMTC.find()) {
				//ファイル
				PartName = FileMTC.group(1);
				FileName = FileMTC.group(2);

				Matcher MIMETypeMTC = Pattern.compile("Content-Type: ?(.*)\n?").matcher(Header);
				if (MIMETypeMTC.find()) {
					MIME = MIMETypeMTC.group(1);
				}
			} else {
				//ファイルじゃない
				Matcher NameMTC = Pattern.compile("name=\"(.*)\"").matcher(Header);
				if (NameMTC.find()) {
					PartName = NameMTC.group(1);
					MIME = "text/plain";
				}
			}

			//読み取り
			ByteArrayOutputStream BAOS = new ByteArrayOutputStream();
			MS.readBodyData(BAOS);
			SetValue(PartName, BAOS.toByteArray(), MIME, FileName);
		}
	}

	public void SetValue(String Key, byte[] Value, String MimeType, String FileName) {
		HashMap<String, Object> Content = new HashMap<>();
		Content.put("KEY", Key);
		Content.put("VAL", Value);
		Content.put("MIME", MimeType);
		Content.put("FILE", FileName);

		PartList.add(Content);
	}

	public String GetValue(String Key) {
		for (HashMap<String, Object> Part:PartList) {
			if (Part.get("KEY").equals(Key)) {
				return new String((byte[]) Part.get("VAL"));
			}
		}

		return null;
	}

	public byte[] GetFile(String Key) {
		for (HashMap<String, Object> Part:PartList) {
			if (Part.get("KEY").equals(Key)) {
				return (byte[]) Part.get("VAL");
			}
		}

		return null;
	}

	public String GetBoundary() {
		return Boundary;
	}

	public byte[] Build() throws IOException {
		String CRLF = "\r\n";
		ByteArrayOutputStream RETURN = new ByteArrayOutputStream();

		for (HashMap<String, Object> Part:PartList) {
			//ぼうんだりー
			RETURN.write("--".getBytes());
			RETURN.write(Boundary.getBytes());
			RETURN.write(CRLF.getBytes());
			if (Part.get("FILE") == null) {
				RETURN.write(("Content-Disposition: form-data; name=\"" + Part.get("KEY") + "\"").getBytes());
				RETURN.write(CRLF.getBytes());
				RETURN.write(CRLF.getBytes());
				RETURN.write(new String((byte[])Part.get("VAL")).getBytes());
				RETURN.write(CRLF.getBytes());
			} else {
				RETURN.write(("Content-Disposition: form-data; name=\"" + Part.get("KEY") + "\"; filename=\"" + Part.get("FILE") + "\"").getBytes());
				RETURN.write(CRLF.getBytes());
				RETURN.write("Content-Type: ".getBytes());
				RETURN.write(((String)Part.get("MIME")).getBytes());
				RETURN.write(CRLF.getBytes());
				RETURN.write("Content-Transfer-Encoding: base64".getBytes());
				RETURN.write(CRLF.getBytes());
				RETURN.write(CRLF.getBytes());
				RETURN.write((byte[])Part.get("VAL"));
				RETURN.write(CRLF.getBytes());
			}
		}

		RETURN.write("--".getBytes());
		RETURN.write(Boundary.getBytes());
		RETURN.write("--".getBytes());
		RETURN.write(CRLF.getBytes());

		return RETURN.toByteArray();
	}
}

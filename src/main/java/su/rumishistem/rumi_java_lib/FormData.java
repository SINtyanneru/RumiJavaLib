package su.rumishistem.rumi_java_lib;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Base64;
import java.util.HashMap;
import java.util.List;

public class FormData {
	private String Boundary = "";
	private List<HashMap<String, Object>> PartList = new ArrayList<>();

	public FormData() {
		//FormDataFuckFuckにしたらだめだった
		Boundary = "----WebKitFormBoundary" + System.currentTimeMillis();
	}

	public void SetValue(String Key, byte[] Value, String MimeType, String FileName) {
		HashMap<String, Object> Content = new HashMap<>();
		Content.put("KEY", Key);
		Content.put("VAL", Value);
		Content.put("MIME", MimeType);
		Content.put("FILE", FileName);

		PartList.add(Content);
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

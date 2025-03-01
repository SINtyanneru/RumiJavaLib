package su.rumishistem.rumi_java_lib.Misskey.API;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import su.rumishistem.rumi_java_lib.FETCH;
import su.rumishistem.rumi_java_lib.FETCH_RESULT;
import su.rumishistem.rumi_java_lib.FormData;
import java.io.IOException;

public class DriveFile {
	public static String Upload(String DOMAIN, String TOKEN, String NAME, byte[] Data) throws IOException {
		FormData FD = new FormData();
		FD.SetValue("i", TOKEN.getBytes(), "", null);
		FD.SetValue("name", NAME.getBytes(), "", null);
		FD.SetValue("file", Data, "image/png", NAME);
		FETCH UploadAjax = new FETCH("https://" + DOMAIN + "/api/drive/files/create");
		UploadAjax.SetHEADER("Content-Type", "multipart/form-data; boundary=" + FD.GetBoundary());
		FETCH_RESULT RESULT = UploadAjax.POST(FD.Build());
		JsonNode RESULT_JSON = new ObjectMapper().readTree(RESULT.GetString());
		return RESULT_JSON.get("id").asText();
	}
}

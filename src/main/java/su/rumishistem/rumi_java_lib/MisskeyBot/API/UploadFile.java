package su.rumishistem.rumi_java_lib.MisskeyBot.API;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import su.rumishistem.rumi_java_lib.Ajax.Ajax;
import su.rumishistem.rumi_java_lib.Ajax.AjaxResult;
import su.rumishistem.rumi_java_lib.FormData;
import su.rumishistem.rumi_java_lib.MisskeyBot.Exception.LoginException;
import su.rumishistem.rumi_java_lib.MisskeyBot.MisskeyClient;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.HashMap;

public class UploadFile {
	private MisskeyClient client;

	public UploadFile(MisskeyClient client) {
		this.client = client;
	}

	public JsonNode create_from_file(String name, File file) throws IOException {
		FormData fd = new FormData();
		fd.SetValue("i", client.get_token().getBytes(StandardCharsets.UTF_8), "", null);
		fd.SetValue("name", name.getBytes(StandardCharsets.UTF_8), "image/png", null);
		//Q.メモリ破壊は？→A.FormDataの時点でメモリ破壊もクソもない。
		fd.SetValue("file", Files.readAllBytes(file.toPath()), "", name);

		Ajax ajax = new Ajax("https://"+client.get_host()+"/api/drive/files/create");
		ajax.set_header("Content-Type", "multipart/form-data; boundary=" + fd.GetBoundary());
		AjaxResult result = ajax.POST(fd.Build());
		if (result.get_code() == 200) {
			return new ObjectMapper().readTree(result.get_body_as_string());
		} else {
			throw new RuntimeException("エラー:" + result.get_code());
		}
	}
}

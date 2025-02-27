package su.rumishistem.rumi_java_lib.Misskey.API;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import su.rumishistem.rumi_java_lib.FETCH;
import su.rumishistem.rumi_java_lib.FETCH_RESULT;
import su.rumishistem.rumi_java_lib.HTTP_REQUEST;
import su.rumishistem.rumi_java_lib.Misskey.MODULE.ConvertType;
import su.rumishistem.rumi_java_lib.Misskey.TYPE.Note;
import su.rumishistem.rumi_java_lib.Misskey.TYPE.User;

import java.io.IOException;
import java.util.HashMap;

public class CreateNote {
	public static Note Post(String DOMAIN, String TOKEN, User Kai, Note NOTE) throws IOException {
		FETCH NOTE_AJAX = new FETCH("https://" + DOMAIN + "/api/notes/create");
		NOTE_AJAX.SetHEADER("Content-Type", "application/json; charset=utf-8");

		ObjectMapper OM = new ObjectMapper();

		HashMap<String, Object> POST_DATA = new HashMap<>();
		POST_DATA.put("i", TOKEN);
		POST_DATA.put("text", NOTE.getTEXT());
		POST_DATA.put("poll", null);
		POST_DATA.put("cw", null);
		POST_DATA.put("localOnly", false);
		POST_DATA.put("visibility", "public");
		POST_DATA.put("reactionAcceptance", null);

		//リプライ
		if (NOTE.isREPLY()) {
			POST_DATA.put("replyId", NOTE.getReply().getID());
		}

		FETCH_RESULT NoteAjaxResult = NOTE_AJAX.POST(OM.writeValueAsString(POST_DATA).getBytes());
		JsonNode RESULT = new ObjectMapper().readTree(NoteAjaxResult.GetString());
		if (RESULT.get("error") == null) {
			return ConvertType.ConvertNote(RESULT.get("createdNote"), Kai, DOMAIN, TOKEN);
		} else {
			throw new Error("ノート投稿エラー" + RESULT.get("error").get("code").asText() + "\n" + RESULT.get("error").get("message").asText());
		}
	}
}

package su.rumishistem.rumi_java_lib.Misskey.MODULE;

import com.fasterxml.jackson.databind.JsonNode;
import su.rumishistem.rumi_java_lib.Misskey.API.UserSHOW;
import su.rumishistem.rumi_java_lib.Misskey.TYPE.Note;
import su.rumishistem.rumi_java_lib.Misskey.TYPE.NoteVis;
import su.rumishistem.rumi_java_lib.Misskey.TYPE.User;

import java.time.OffsetDateTime;

public class ConvertType {
	public static User ConvertUser(JsonNode UserData) {
		boolean NiFA = false;
		if (UserData.get("twoFactorEnabled") != null) {
			NiFA = UserData.get("twoFactorEnabled").asBoolean();
		}

		//フォローしている
		boolean isFollowing = false;
		if (UserData.get("isFollowing") != null) {
			isFollowing = UserData.get("isFollowing").asBoolean();
		}

		//フォローされてる
		boolean isFollowed = false;
		if (UserData.get("isFollowed") != null) {
			isFollowed = UserData.get("isFollowed").asBoolean();
		}

		return new User(
			UserData.get("id").asText(),
			UserData.get("username").asText(),
			UserData.get("name").asText(),
			UserData.get("avatarUrl").asText(),
			UserData.get("host").asText(),
			null,
			NiFA,
			isFollowing,
			isFollowed
		);
	}

	public static Note ConvertNote(JsonNode NoteData, User Kai, String DOMAIN, String TOKEN) {
		String RenoteID = null;
		if (!NoteData.get("renoteId").isNull()) {
			RenoteID = NoteData.get("renoteId").asText();
		}

		String ReplyID = null;
		if (!NoteData.get("replyId").isNull()) {
			ReplyID = NoteData.get("replyId").asText();
		}

		String CW = null;
		if (NoteData.get("cw") != null) {
			CW = NoteData.get("cw").asText();
		}

		boolean KaiMention = false;
		if (NoteData.get("mentions") != null) {
			for (int I = 0; I < NoteData.get("mentions").size(); I++) {
				if (NoteData.get("mentions").get(I).asText().equals(Kai.getID())) {
					KaiMention = true;
					break;
				}
			}
		}

		NoteVis VIS = null;
		//公開範囲
		switch (NoteData.get("visibility").asText()) {
			case "public": {
				VIS = NoteVis.PUBLIC;
				break;
			}

			case "home": {
				VIS = NoteVis.HOME;
				break;
			}

			case "followers": {
				VIS = NoteVis.FOLLOWER;
				break;
			}

			case "specified": {
				VIS = NoteVis.DM;
				break;
			}
		}

		return new Note(
			false,
			Kai,
			DOMAIN,
			TOKEN,
			ConvertUser(NoteData.get("user")),
			NoteData.get("id").asText(),
			NoteData.get("text").asText(),
			OffsetDateTime.parse(NoteData.get("createdAt").asText()),
			VIS,
			RenoteID,
			ReplyID,
			CW,
			KaiMention
		);
	}
}

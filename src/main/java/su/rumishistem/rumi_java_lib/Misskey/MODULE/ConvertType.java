package su.rumishistem.rumi_java_lib.Misskey.MODULE;

import com.fasterxml.jackson.databind.JsonNode;
import su.rumishistem.rumi_java_lib.Misskey.API.UserSHOW;
import su.rumishistem.rumi_java_lib.Misskey.TYPE.Note;
import su.rumishistem.rumi_java_lib.Misskey.TYPE.NoteVis;
import su.rumishistem.rumi_java_lib.Misskey.TYPE.User;

import java.time.OffsetDateTime;
import java.util.ArrayList;

public class ConvertType {
	public static User ConvertUser(JsonNode UserData, User Kai, String DOMAIN, String TOKEN) {
		boolean NiFA = false;
		JsonNode TwoFactorEnabledNode = UserData.get("twoFactorEnabled");
		if (TwoFactorEnabledNode != null && !TwoFactorEnabledNode.isNull()) {
			NiFA = TwoFactorEnabledNode.asBoolean();
		}

		//フォローしている
		boolean isFollowing = false;
		JsonNode IsFollowingNode = UserData.get("isFollowing");
		if (IsFollowingNode != null && !IsFollowingNode.isNull()) {
			isFollowing = IsFollowingNode.asBoolean();
		}

		//フォローされてる
		boolean isFollowed = false;
		JsonNode IsFollowedNode = UserData.get("isFollowed");
		if (IsFollowedNode != null && !IsFollowedNode.isNull()) {
			isFollowed = IsFollowedNode.asBoolean();
		}

		String Host = "";
		if (UserData.get("host").isNull()) {
			Host = Kai.getUID();
		} else {
			Host = UserData.get("host").asText();
		}

		return new User(
			UserData.get("id").asText(),
			UserData.get("username").asText(),
			Host,
			UserData.get("name").asText(),
			UserData.get("avatarUrl").asText(),
			DOMAIN,
			TOKEN,
			NiFA,
			isFollowing,
			isFollowed
		);
	}

	public static Note ConvertNote(JsonNode NoteData, User Kai, String DOMAIN, String TOKEN) {
		String RenoteID = null;
		JsonNode RenoteNode = NoteData.get("renoteId");
		if (RenoteNode != null && !RenoteNode.isNull()) {
			RenoteID = RenoteNode.asText();
		}

		String ReplyID = null;
		JsonNode ReplyNode = NoteData.get("replyId");
		if (ReplyNode != null && !ReplyNode.isNull()) {
			ReplyID = ReplyNode.asText();
		}

		String CW = null;
		JsonNode CWNode = NoteData.get("cw");
		if (CWNode != null && !CWNode.isNull()) {
			CW = CWNode.asText();
		}

		boolean KaiMention = false;
		JsonNode MentionNode = NoteData.get("mentions");
		if (MentionNode != null && !MentionNode.isNull()) {
			for (int I = 0; I < MentionNode.size(); I++) {
				if (MentionNode.get(I).asText().equals(Kai.getID())) {
					KaiMention = true;
					break;
				}
			}
		}

		NoteVis VIS = NoteVis.PUBLIC;
		JsonNode VisibilityNode = NoteData.get("visibility");
		if (VisibilityNode != null && !VisibilityNode.isNull()) {
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
		}

		return new Note(
			false,
			Kai,
			DOMAIN,
			TOKEN,
			ConvertUser(NoteData.get("user"), Kai, DOMAIN, TOKEN),
			NoteData.get("id").asText(),
			NoteData.get("text").asText(),
			OffsetDateTime.parse(NoteData.get("createdAt").asText()),
			VIS,
			RenoteID,
			ReplyID,
			CW,
			KaiMention,
			new ArrayList<>()
		);
	}
}

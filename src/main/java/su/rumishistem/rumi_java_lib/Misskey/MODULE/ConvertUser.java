package su.rumishistem.rumi_java_lib.Misskey.MODULE;

import com.fasterxml.jackson.databind.JsonNode;
import su.rumishistem.rumi_java_lib.Misskey.TYPE.User;

public class ConvertUser {
	public static User Convert(JsonNode UserData) {
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
}

package su.rumishistem.rumi_java_lib.MisskeyBot.Event;

import su.rumishistem.rumi_java_lib.MisskeyBot.Type.User;

public class NewFollowEvent {
	private User user;

	public NewFollowEvent(User user) {
		this.user = user;
	}

	public User get_user() {
		return user;
	}
}

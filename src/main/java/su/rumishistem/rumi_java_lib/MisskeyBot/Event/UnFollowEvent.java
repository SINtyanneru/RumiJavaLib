package su.rumishistem.rumi_java_lib.MisskeyBot.Event;

import su.rumishistem.rumi_java_lib.MisskeyBot.Type.User;

public class UnFollowEvent {
	private User user;

	public UnFollowEvent(User user) {
		this.user = user;
	}

	public User get_user() {
		return user;
	}
}

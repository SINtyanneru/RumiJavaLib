package su.rumishistem.rumi_java_lib.MisskeyBot.Event;

import su.rumishistem.rumi_java_lib.MisskeyBot.Type.User;

public class UnBlockEvent {
	private User user;

	public UnBlockEvent(User user) {
		this.user = user;
	}

	public User get_user() {
		return user;
	}
}

package com.rumisystem.rumi_java_lib.Misskey.Event;

import com.rumisystem.rumi_java_lib.Misskey.TYPE.User;

public class NewFollower {
	private User FROM;

	public NewFollower(User FROM) {
		this.FROM = FROM;
	}

	public User getUser() {
		return FROM;
	}
}

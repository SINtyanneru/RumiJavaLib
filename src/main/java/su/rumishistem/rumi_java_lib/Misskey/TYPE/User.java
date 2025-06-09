package su.rumishistem.rumi_java_lib.Misskey.TYPE;

import su.rumishistem.rumi_java_lib.Misskey.API.Follow;

public class User {
	private String ID;
	private String UID;
	private String Host;
	private String NAME;
	private String ICON_URL;

	private String DOMAIN;
	private String TOKEN;
	private boolean NiFA = false;

	private boolean isFollowing;
	private boolean isFollowed;

	public User(String ID, String UID, String NAME, String Host, String ICON_URL, String DOMAIN, String TOKEN, boolean NiFA, boolean isFollowing, boolean isFollowed) {
		this.ID = ID;
		this.UID = UID;
		this.NAME = NAME;
		this.Host = Host;
		this.ICON_URL = ICON_URL;

		this.DOMAIN = DOMAIN;
		this.TOKEN = TOKEN;
		this.NiFA = NiFA;

		this.isFollowing = isFollowing;
		this.isFollowed = isFollowed;
	}

	public String getID() {
		return ID;
	}

	public String getUID() {
		return UID;
	}

	public String getHost() {
		return Host;
	}

	public String getNAME() {
		return NAME;
	}

	public String getICON_URL() {
		return ICON_URL;
	}

	public boolean isTwoFactorEnabled() {
		return NiFA;
	}

	public boolean Follow() {
		try {
			Follow.Create(ID, TOKEN, DOMAIN);
			return true;
		} catch (Exception EX) {
			EX.printStackTrace();
			return false;
		}
	}
}

package su.rumishistem.rumi_java_lib.Misskey.TYPE;

public class AttachFile {
	private String URL;
	private String NAME;
	private boolean NSFW;

	public AttachFile(String URL, String NAME, boolean NSFW) {
		this.URL = URL;
		this.NAME = NAME;
		this.NSFW = NSFW;
	}

	public String GetURL() {
		return URL;
	}

	public String GetNAME() {
		return NAME;
	}

	public boolean isNSFW() {
		return NSFW;
	}
}

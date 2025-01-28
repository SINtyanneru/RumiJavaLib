package su.rumishistem.rumi_java_lib.RESOURCE;

public class RESOURCE_ENTRIE {
	private String PATH;
	private String NAME;
	private TYPE FILE_TYPE;

	public enum TYPE {
		FILE,
		DIR
	}

	public RESOURCE_ENTRIE(String PATH, String NAME, TYPE FILE_TYPE) {
		this.PATH = PATH;
		this.NAME = NAME;
		this.FILE_TYPE = FILE_TYPE;
	}

	public String getPATH() {
		return PATH;
	}

	public String getNAME() {
		return NAME;
	}

	public TYPE getTYPE() {
		return FILE_TYPE;
	}
}

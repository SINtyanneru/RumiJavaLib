package su.rumishistem.rumi_java_lib.FileDetect;

public class FileDic {
	private FileType Type = FileType.NONE;
	private byte[] MagicNumber = null;

	public FileDic(FileType Type, byte[] MagicNumber) {
		this.Type = Type;
		this.MagicNumber = MagicNumber;
	}

	public FileType GetType() {
		return Type;
	}

	public byte[] GetMagicNumber() {
		return MagicNumber;
	}
}

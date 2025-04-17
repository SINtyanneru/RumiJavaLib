package su.rumishistem.rumi_java_lib.FileDetect;

import su.rumishistem.rumi_java_lib.RangeReader;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class FileDetect {
	private final List<FileDic> MagicNumberTable = new ArrayList<>(){
		{
			add(new FileDic(FileType.PNG, new byte[]{(byte) 0x89, 'P', 'N', 'G', '\r', '\n', 0x1A, '\n'}));
			add(new FileDic(FileType.JPG, new byte[]{(byte) 0xFF, (byte) 0xD8, (byte) 0xFF}));
			add(new FileDic(FileType.GIF, new byte[]{'G', 'I', 'F', '8', '7', 'a'}));
			add(new FileDic(FileType.GIF, new byte[]{'G', 'I', 'F', '8', '9', 'a'}));
		}
	};

	private byte[] FileData = null;

	public FileDetect(byte[] FileData) {
		this.FileData = FileData;
	}

	public FileDetect(File F) throws IOException {
		FileData = RangeReader.readBytesInRange(F.toPath(), 0, 1000);
	}

	//TODO:これAPNGとかの識別が出来ないからどうにかしよう
	public FileType Detect() {
		for (FileDic D:MagicNumberTable) {
			if (ByteStartsWith(FileData, D.GetMagicNumber())) {
				return D.GetType();
			}
		}

		return FileType.NONE;
	}

	private static boolean ByteStartsWith(byte[] DATA, byte[] SI) {
		//比較対象が比較データより長さが短いならfalse
		if (DATA.length < SI.length) {
			return false;
		}

		//比較
		for (int I = 0; I < SI.length; I++) {
			if (DATA[I] != SI[I]) {
				return false;
			}
		}

		return true;
	}
}

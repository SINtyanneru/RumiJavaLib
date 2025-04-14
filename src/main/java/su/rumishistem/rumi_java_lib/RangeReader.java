package su.rumishistem.rumi_java_lib;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;

public class RangeReader {
	public static byte[] readBytesInRange(Path P, int Start, int Length) throws IOException {
		FileChannel C = FileChannel.open(P, StandardOpenOption.READ);
		ByteBuffer Buffer = ByteBuffer.allocate(Length);

		//読込位置
		C.position(Start);

		int BytesRead = C.read(Buffer);
		if (BytesRead == -1) {
			return new byte[0];
		}

		byte[] Result = new byte[BytesRead];
		Buffer.flip();
		Buffer.get(Result);
		return Result;
	}
}

package su.rumishistem.rumi_java_lib;

import java.time.Instant;

public class SnowFlake {
	private static int I = 0;

	public static long GEN() {
		long TIME = getUNIX();

		I++;
		int X = I & 0x3FFFFF;
		if (I > 0x3FFFFF) {
			I = 0;
		}

		return ((TIME << 22) | X) & 0x7FFFFFFFFFFFFFFFL;
	}

	private static long getUNIX() {
		//時刻を取得
		Instant NOW = Instant.now();

		//秒とナノ秒をマイクロ秒に変換
		long MS = NOW.getEpochSecond() * 1_000_000 + NOW.getNano() / 1_000;

		return MS;
	}
}

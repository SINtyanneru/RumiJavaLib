package su.rumishistem.rumi_java_lib;

import java.time.Instant;
import java.util.Random;

public class RID {
	public static String GEN() {
		String MS = String.valueOf(getUNIX());
		int RAND1 = GEN_RAND();
		int RAND2 = GEN_RAND();

		return RAND1 + MS + RAND2;
	}

	private static int GEN_RAND() {
		Random RND = new Random();
		return 10000 + RND.nextInt(90000);
	}

	private static long getUNIX() {
		//時刻を取得
		Instant NOW = Instant.now();

		//秒とナノ秒をマイクロ秒に変換
		long MS = NOW.getEpochSecond() * 1_000_000 + NOW.getNano() / 1_000;

		return MS;
	}
}

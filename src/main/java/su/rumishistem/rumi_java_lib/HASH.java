package su.rumishistem.rumi_java_lib;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class HASH {
	public enum HASH_TYPE {
		MD5,
		SHA1,
		SHA256,
		SHA512,
		SHA3_256
	}

	public static String Gen(HASH_TYPE TYPE, byte[] INPUT) throws NoSuchAlgorithmException {
		MessageDigest MD = MessageDigest.getInstance(TYPE_TO_STRING(TYPE));
		byte[] HASH_BYTES = MD.digest(INPUT);

		StringBuilder SB = new StringBuilder();
		for (byte B:HASH_BYTES) {
			SB.append(String.format("%02x", B));
		}

		return SB.toString();
	}

	private static String TYPE_TO_STRING(HASH_TYPE IN) {
		switch (IN) {
			case MD5: {
				return "MD5";
			}

			case SHA1: {
				return "SHA-1";
			}

			case SHA256: {
				return "SHA-256";
			}

			case SHA512: {
				return "SHA-512";
			}

			case SHA3_256:{
				return "SHA3-256";
			}

			default: {
				throw new Error("ハッシュタイプが不正です");
			}
		}
	}
}

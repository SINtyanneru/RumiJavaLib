package su.rumishistem.rumi_java_lib.RSDF;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;

public class RSDFDecoder {
	public static DecodedRSDF decode(byte[] input) throws IOException {
		ByteArrayInputStream in = new ByteArrayInputStream(input);
		DecodedRSDF decoded;

		//マジックナンバー
		if (!Arrays.equals(in.readNBytes(5), new byte[]{'R', 'S', 'D', 'F', 0x00})) {
			throw new IllegalArgumentException("RSDFではないデータが入力されました");
		}

		int type = in.read() & 0xFF;
		if (type == 0x00) {
			//辞書
			decoded = new DecodedRSDF(parse_dict(in));
		} else if (type == 0x01) {
			//配列
			decoded = new DecodedRSDF(parse_array(in));
		} else {
			throw new RuntimeException("ルートには配列か辞書しか入りません。");
		}

		in.close();

		return decoded;
	}

	private static List<Object> parse_array(ByteArrayInputStream in) throws IOException {
		List<Object> list = new ArrayList<>();
		int length = bytes_to_int(in.readNBytes(4));

		for (int i = 0; i < length; i++) {
			int type = in.read() & 0xFF;
			if (type == 0x00) {
				//辞書
				list.add(parse_dict(in));
			} else if (type == 0x01) {
				//配列
				list.add(parse_array(in));
			} else if (type == 0xFF) {
				list.add(null);
			} else {
				list.add(parse_value(type, in));
			}
		}

		return list;
	}

	private static LinkedHashMap<String, Object> parse_dict(ByteArrayInputStream in) throws IOException {
		LinkedHashMap<String, Object> dict = new LinkedHashMap<>();
		int length = bytes_to_int(in.readNBytes(4));

		for (int i = 0; i < length; i++) {
			int key_length = bytes_to_int(in.readNBytes(4));
			byte[] key_byte = in.readNBytes(key_length);
			String key = new String(key_byte, StandardCharsets.UTF_8);
			int type = in.read() & 0xFF;
			Object value;

			if (type == 0x00) {
				value = parse_dict(in);
			} else if (type == 0x01) {
				value = parse_array(in);
			} else if (type == 0xFF) {
				value = null;
			} else {
				value = parse_value(type, in);
			}
			dict.put(key, value);
		}

		return dict;
	}

	private static Object parse_value(int type, ByteArrayInputStream in) throws IOException {
		if (type == 0x02) {
			//文字列
			int str_byte_length = bytes_to_int(in.readNBytes(4));
			byte[] str_byte = in.readNBytes(str_byte_length);
			String str = new String(str_byte, StandardCharsets.UTF_8);
			return str;
		} else if (type == 0x03) {
			//Bool
			int true_or_false = in.read() & 0xFF;
			if (true_or_false == 1) {
				return true;
			} else {
				return false;
			}
		} else if (type == 0x04) {
			//i8
			int i8 = in.read() & 0xFF;
			return i8;
		} else if (type == 0x05) {
			//i16
			byte[] b = in.readNBytes(2);
			int i16 = ((b[0] & 0xFF) << 8) | (b[1] & 0xFF);
			return i16;
		} else if (type == 0x06) {
			//i32
			byte[] b = in.readNBytes(4);
			int i32 = bytes_to_int(b);
			return i32;
		} else if (type == 0x07) {
			//i64
			byte[] b = in.readNBytes(8);
			long i64 = ((long) (b[0] & 0xFF) << 56) | ((long) (b[1] & 0xFF) << 48) | ((long) (b[2] & 0xFF) << 40) | ((long) (b[3] & 0xFF) << 32) | ((long) (b[4] & 0xFF) << 24) | ((long) (b[5] & 0xFF) << 16) | ((long) (b[6] & 0xFF) << 8) | (long) (b[7] & 0xFF);
			return i64;
		} else {
			throw new RuntimeException("非対応の型");
		}
	}

	private static int bytes_to_int(byte[] input) {
		return ((input[0] & 0xFF) << 24) | ((input[1] & 0xFF) << 16) | ((input[2] & 0xFF) << 8) | (input[3] & 0xFF);
	}
}

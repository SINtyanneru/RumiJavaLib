package su.rumishistem.rumi_java_lib.RSDF;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.lang.reflect.Array;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;

public class RSDFEncoder {
	private static final byte[] MAGIC_NUMBER = new byte[]{'R', 'S', 'D', 'F', 0x00};

	public static byte[] encode(Object input) throws IOException {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		baos.write(MAGIC_NUMBER);

		if (input == null) {
			throw new IllegalArgumentException("Nullは殺すぞ");
		}

		if (input.getClass().isArray()) {
			baos.write(encode_array((Object[]) input));
		} else if (input instanceof Map) {
			baos.write(encode_dict((Map<String , ?>) input));
		} else {
			throw new RuntimeException("ルートに入れれる型は配列か辞書のみです。");
		}

		StringBuilder sb = new StringBuilder();
		for (byte b:baos.toByteArray()) {
			sb.append(String.format("%02X ", b));
		}
		System.out.println(sb.toString());

		return baos.toByteArray();
	}

	private static byte[] encode_array(Object[] array) throws IOException {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		baos.write(0x01);
		baos.write(int_to_byte(array.length));

		for (Object value:array) {
			if (value instanceof Map) {
				//辞書
				baos.write(encode_dict((Map<String, ?>) value));
			} else if (value.getClass().isArray()) {
				//配列
				baos.write(encode_array((Object[]) value));
			} else if (value instanceof List<?>) {
				//配列
				baos.write(encode_array(((List<?>)value).toArray()));
			} else {
				baos.write(encode_value(value));
			}
		}

		return baos.toByteArray();
	}

	private static byte[] encode_dict(Map<String, ?> input) throws IOException {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		baos.write(0x00);
		baos.write(int_to_byte(input.keySet().size()));

		for (String key:input.keySet()) {
			byte[] key_name = key.getBytes(StandardCharsets.UTF_8);
			Object value = input.get(key);

			baos.write(int_to_byte(key_name.length));
			baos.write(key_name);

			if (value == null) {
				baos.write(0xFF);
				continue;
			}

			if (value.getClass().isArray()) {
				baos.write(encode_array((Object[]) value));
			} else if (value instanceof List<?>) {
				baos.write(encode_array(((List<?>)value).toArray()));
			} else if (value instanceof Map) {
				baos.write(encode_dict((Map<String, ?>) value));
			} else {
				baos.write(encode_value(value));
			}
		}

		return baos.toByteArray();
	}

	private static byte[] encode_value(Object value) throws IOException {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();

		if (value instanceof String string) {
			//文字列
			byte[] str_byte = string.getBytes(StandardCharsets.UTF_8);
			baos.write(0x02);
			baos.write(int_to_byte(str_byte.length));
			baos.write(str_byte);
		} else if (value instanceof Boolean bool) {
			//Bool
			baos.write(0x03);
			if (bool) {
				baos.write(0x01);
			} else {
				baos.write(0x00);
			}
		} else if (value instanceof Integer number) {
			//32bit数値
			baos.write(0x06);
			baos.write(int_to_byte(number));
		} else if (value instanceof Long number) {
			//64bit数値
			baos.write(0x07);
			baos.write(long_to_byte(number));
		} else {
			throw new RuntimeException("非対応の型:" + value.getClass().getTypeName());
		}

		return baos.toByteArray();
	}

	private static byte[] int_to_byte(int input) {
		ByteBuffer buffer = ByteBuffer.allocate(4);
		buffer.order(ByteOrder.BIG_ENDIAN);
		buffer.putInt(input);
		return buffer.array();
	}

	private static byte[] long_to_byte(long input) {
		ByteBuffer buffer = ByteBuffer.allocate(8);
		buffer.order(ByteOrder.BIG_ENDIAN);
		buffer.putLong(input);
		return buffer.array();
	}
}

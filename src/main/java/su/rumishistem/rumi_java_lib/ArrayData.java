package su.rumishistem.rumi_java_lib;

import java.sql.Blob;

/**
 * JavaでJSのオブジェクトみたいなことをしたーい
 * の、データ部分
 */
public class ArrayData {
	private Object DATA = null;

	/**
	 * データを渡してください！
	 * @param DATA 値
	 */
	public ArrayData(Object DATA) {
		this.DATA = DATA;

		if (DATA instanceof ArrayData) {
			throw new Error("ArrayDataにArrayDataを入れ子にすることはできません");
		}
	}

	/**
	 * 指定したキーがNullかを取得します
	 * @return Nullならtrue
	 */
	public boolean isNull() {
		if (DATA == null) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * Object型として受取
	 * @return 値
	 */
	public Object asObject() {
		return DATA;
	}

	/**
	 * String型として受取
	 * @return 値
	 */
	public String asString() {
		if (DATA instanceof String) {
			return (String) DATA;
		} else {
			return String.valueOf(DATA);
		}
	}

	/**
	 * int型として受取
	 * @return 値
	 */
	public int asInt() {
		return (int) DATA;
	}

	/**
	 * long型として受取
	 * @return 値
	 */
	public long asLong() {
		return (long) DATA;
	}

	/**
	 * booleanとして受取
	 * @return 値
	 */
	public boolean asBool() {
		return (boolean) DATA;
	}

	/**
	 * Blobとして受取
	 * @return 値
	 */
	public Blob asBlob() {
		return (Blob) DATA;
	}
}

package su.rumishistem.rumi_java_lib;

public class ArrayData {
	private Object DATA = null;

	public ArrayData(Object DATA) {
		this.DATA = DATA;
	}

	public Object asObject() {
		return DATA;
	}

	public String asString() {
		if (DATA instanceof String) {
			return (String) DATA;
		} else {
			return String.valueOf(DATA);
		}
	}

	public int asInt() {
		return (int) DATA;
	}

	public long asLong() {
		return (long) DATA;
	}

	public boolean asBool() {
		return (boolean) DATA;
	}
}

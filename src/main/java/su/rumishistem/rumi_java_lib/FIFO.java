package su.rumishistem.rumi_java_lib;

import java.util.ArrayList;
import java.util.List;

public class FIFO<T> {
	private List<T> array = new ArrayList<>();

	public void add(T value) {
		array.add(value);
	}

	public T get() {
		T value = array.getFirst();
		array.removeFirst();
		return value;
	}
}

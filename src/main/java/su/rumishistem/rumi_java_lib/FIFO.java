package su.rumishistem.rumi_java_lib;

import java.util.ArrayList;
import java.util.List;

public class FIFO<T> {
	private int limit = 0;
	private List<T> array = new ArrayList<>();

	public FIFO() {}
	public FIFO(int limit) {
		this.limit = limit;
	}

	public void add(T value) {
		array.add(value);

		if (limit == 0) return;
		if (array.size() > limit) {
			array.removeFirst();
		}
	}

	public T get() {
		T value = array.getFirst();
		array.removeFirst();
		return value;
	}
}

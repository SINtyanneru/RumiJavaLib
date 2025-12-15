package su.rumishistem.rumi_java_lib.RSDF;

import java.lang.reflect.Field;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class DecodedRSDF {
	private boolean root_is_dict = false;
	private List<Object> root_array = null;
	private LinkedHashMap<String, Object> root_dict = null;

	protected DecodedRSDF(Object input) {
		if (input instanceof Map<?,?>) {
			root_is_dict = true;
			root_dict = (LinkedHashMap<String, Object>) input;
		} else {
			root_array = (List<Object>) input;
		}
	}

	public boolean is_dict() {
		return root_is_dict;
	}

	public List<Object> get_array() {
		return root_array;
	}

	public LinkedHashMap<String, Object> get_dict() {
		return root_dict;
	}

	public <T> T cast(Class<T> c) throws ReflectiveOperationException{
		T instance = c.getDeclaredConstructor().newInstance();

		for (Field f:c.getDeclaredFields()) {
			f.setAccessible(true);
			String name = f.getName();
			boolean allow_null = f.isAnnotationPresent(Nullable.class);
			boolean is_custom_name = f.isAnnotationPresent(CustomFieldName.class);

			//カスタム名指定があるか？
			if (is_custom_name) {
				name = f.getAnnotation(CustomFieldName.class).value();
			}

			Object value = root_dict.get(name);
			if (value == null) {
				if (allow_null == false) {
					throw new RuntimeException(name + "で許可されていないNullが入りました。");
				}
				continue;
			}

			//セットする部分
			f.set(instance, value);
		}

		return instance;
	}
}

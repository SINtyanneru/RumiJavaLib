package su.rumishistem.rumi_java_lib;

import java.util.HashMap;

public class URLParam {
	public static HashMap<String, String> Parse(String IN) {
		HashMap<String, String> DATA = new HashMap<>();

		IN = IN.replace("\\?", "");
		for (String PARAM:IN.split("&")) {
			String KEY = PARAM.split("=")[0];
			String VAL = PARAM.split("=")[1];
			DATA.put(KEY, VAL);
		}

		return DATA;
	}
}

package su.rumishistem.rumi_java_lib.SmartHTTP.Type;

public class MethodStringToEnum {
	public static EndpointEntrie.Method Convert(String S) {
		for (EndpointEntrie.Method M: EndpointEntrie.Method.values()) {
			if (M.name().equals(S.toUpperCase())) {
				return M;
			}
		}

		return null;
	}
}

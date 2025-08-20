package su.rumishistem.rumi_java_lib.SmartHTTP.Type;

public class MethodStringToEnum {
	public static EndpointEntrie.Method Convert(String S) {
		//HTTP標準メソッド
		for (EndpointEntrie.Method M: EndpointEntrie.Method.values()) {
			if (M.name().equals(S.toUpperCase())) {
				return M;
			}
		}

		EndpointEntrie.Method wevdav = WebDAV(S);
		if (wevdav != null) {
			return wevdav;
		}

		return null;
	}

	private static EndpointEntrie.Method WebDAV(String s) {
		for (EndpointEntrie.Method M: EndpointEntrie.Method.values()) {
			String method_name = M.name().replaceFirst("WEBDAV_", "");
			if (method_name.equals(s.toUpperCase())) {
				return M;
			}
		}

		return null;
	}
}

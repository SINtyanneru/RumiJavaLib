package su.rumishistem.rumi_java_lib.SmartHTTP;

import su.rumishistem.rumi_java_lib.HTTP_SERVER.HTTP_EVENT;

import java.util.HashMap;

public class HTTP_REQUEST {
	private HTTP_EVENT HE;
	private HashMap<String, String> PARAM_LIST;

	public HTTP_REQUEST(HTTP_EVENT HE, HashMap<String, String> PARAM_LIST) {
		this.HE = HE;
		this.PARAM_LIST = PARAM_LIST;
	}

	public HTTP_EVENT GetEVENT() {
		return HE;
	}

	public String GetParam(String KEY) {
		return PARAM_LIST.get(KEY);
	}
}

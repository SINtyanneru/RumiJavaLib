package su.rumishistem.rumi_java_lib.SmartHTTP.Toolkit;

import com.fasterxml.jackson.databind.ObjectMapper;
import su.rumishistem.rumi_java_lib.SmartHTTP.HTTP_REQUEST;
import su.rumishistem.rumi_java_lib.SmartHTTP.HTTP_RESULT;
import su.rumishistem.rumi_java_lib.SmartHTTP.Type.EndpointFunction;

import java.util.LinkedHashMap;

public class RSVAPIERR implements EndpointFunction {
	@Override
	public HTTP_RESULT Run(HTTP_REQUEST r) throws Exception {
		try {
			LinkedHashMap<String, Object> RETURN = new LinkedHashMap<String, Object>();
			RETURN.put("STATUS", false);
			RETURN.put("ERR", "SYSTEM_ERR");
			RETURN.put("EX", r.GetParam("EX"));
			return new HTTP_RESULT(500, new ObjectMapper().writeValueAsString(RETURN).getBytes(), "application/json; charset=UTF-8");
		} catch (Exception EX) {
			//EX.printStackTrace();
			//もみ消す
			return null;
		}
	}
}

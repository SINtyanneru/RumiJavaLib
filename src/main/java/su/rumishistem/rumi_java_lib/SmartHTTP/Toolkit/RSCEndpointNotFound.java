package su.rumishistem.rumi_java_lib.SmartHTTP.Toolkit;

import su.rumishistem.rumi_java_lib.EXCEPTION_READER;
import su.rumishistem.rumi_java_lib.SmartHTTP.HTTP_REQUEST;
import su.rumishistem.rumi_java_lib.SmartHTTP.HTTP_RESULT;
import su.rumishistem.rumi_java_lib.SmartHTTP.Type.ErrorEndpointFunction;

public class RSCEndpointNotFound implements ErrorEndpointFunction {
	@Override
	public HTTP_RESULT Run(HTTP_REQUEST r, Exception ex) throws Exception {
		return RSVErrorCode.endpoint_not_found_request("こん");
	}
}

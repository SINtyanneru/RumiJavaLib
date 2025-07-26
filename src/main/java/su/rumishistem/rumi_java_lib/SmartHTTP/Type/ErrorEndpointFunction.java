package su.rumishistem.rumi_java_lib.SmartHTTP.Type;

import su.rumishistem.rumi_java_lib.SmartHTTP.HTTP_REQUEST;
import su.rumishistem.rumi_java_lib.SmartHTTP.HTTP_RESULT;

public interface ErrorEndpointFunction {
	HTTP_RESULT Run(HTTP_REQUEST r, Exception ex) throws Exception;
}

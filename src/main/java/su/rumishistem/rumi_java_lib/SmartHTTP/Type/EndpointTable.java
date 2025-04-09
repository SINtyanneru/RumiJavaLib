package su.rumishistem.rumi_java_lib.SmartHTTP.Type;

import su.rumishistem.rumi_java_lib.SmartHTTP.HTTP_REQUEST;
import su.rumishistem.rumi_java_lib.SmartHTTP.HTTP_RESULT;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.function.Function;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class EndpointTable {
	private List<EndpointEntrie> EP_LIST = new ArrayList<>();

	public void Set(EndpointEntrie EE) {
		EP_LIST.add(EE);
	}

	public EndpointEntrie Get(String PATH, EndpointEntrie.Method M) {
		for (EndpointEntrie E:EP_LIST) {
			Pattern PATTERN = Pattern.compile(E.GetPath());
			Matcher MATCHER = PATTERN.matcher(PATH);

			//見つけろ(ちなみに最初にfindを実行しないとgroupでエラー出るからな)
			if (MATCHER.find()) {
				if (E.GetMethod() == M || E.GetMethod() == EndpointEntrie.Method.ALL) {
					//パラメーターを取得する
					for (String GROUP_NAME:PATTERN.namedGroups().keySet()) {
						E.SetParam(GROUP_NAME, MATCHER.group(GROUP_NAME));
					}

					//関数を実行
					if (MATCHER.matches()) {
						return E;
					}
				}
			}
		}

		return null;
	}
}

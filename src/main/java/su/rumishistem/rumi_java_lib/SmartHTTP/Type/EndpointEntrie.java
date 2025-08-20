package su.rumishistem.rumi_java_lib.SmartHTTP.Type;

import su.rumishistem.rumi_java_lib.HTTP_SERVER.HTTP_EVENT;
import su.rumishistem.rumi_java_lib.SmartHTTP.HTTP_REQUEST;
import su.rumishistem.rumi_java_lib.SmartHTTP.HTTP_RESULT;

import java.util.HashMap;
import java.util.function.Function;

public class EndpointEntrie {
	private String P;
	private Method M;
	private EndpointFunction F;
	private HashMap<String, String> PARAM_LIST = new HashMap<>();

	public enum Method{
		ALL,
		GET, POST, DELETE, PATCH, PUT, HEAD, OPTION,

		//WebDAV
		WEBDAV_PROPFIND,			//プロパティ取得
		WEBDAV_PROPPATCH,			//プロパティ変更
		WEBDAV_MKCOL,				//コレクション(ディレクトリ)作成
		WEBDAV_COPY,				//リソースをコピー
		WEBDAV_MOVE,				//リソースを移動
		WEBDAV_LOCK,				//リソースをロック
		WEBDAV_UNLOCK,				//リソースのロックを解除
		//WebDAV拡張
		WEBDAV_EXT_MKCALENDAR,		//カレンダーコレクション作成
		WEBDAV_EXT_SEARCH,			//検索
		WEBDAV_EXT_BIND,
		WEBDAV_EXT_UNBIND,
		WEBDAV_EXT_REBIND,
		WEBDAV_EXT_REPORT,
	}

	public EndpointEntrie(String P, Method M, EndpointFunction F) {
		this.P = P;
		this.M = M;
		this.F = F;
	}

	public void SetParam(String K, String V) {
		PARAM_LIST.put(K, V);
	}

	public String GetPath() {
		return P;
	}

	public Method GetMethod() {
		return M;
	}

	public EndpointFunction GetFunction() {
		return F;
	}

	public HTTP_RESULT RunFunction(HTTP_EVENT E) throws Exception {
		return F.Run(new HTTP_REQUEST(E, PARAM_LIST));
	}
}

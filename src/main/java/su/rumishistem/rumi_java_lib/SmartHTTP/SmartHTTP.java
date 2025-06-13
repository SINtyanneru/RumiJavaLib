package su.rumishistem.rumi_java_lib.SmartHTTP;

import su.rumishistem.rumi_java_lib.EXCEPTION_READER;
import su.rumishistem.rumi_java_lib.HTTP_SERVER.HTTP_EVENT;
import su.rumishistem.rumi_java_lib.HTTP_SERVER.HTTP_EVENT_LISTENER;
import su.rumishistem.rumi_java_lib.HTTP_SERVER.HTTP_SERVER;
import su.rumishistem.rumi_java_lib.RESOURCE.RESOURCE_MANAGER;
import su.rumishistem.rumi_java_lib.SmartHTTP.Type.EndpointEntrie;
import su.rumishistem.rumi_java_lib.SmartHTTP.Type.EndpointFunction;
import su.rumishistem.rumi_java_lib.SmartHTTP.Type.EndpointTable;
import su.rumishistem.rumi_java_lib.SmartHTTP.Type.MethodStringToEnum;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.function.Function;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SmartHTTP {
	private int PORT;
	private HTTP_SERVER HS;
	private EndpointTable ET = new EndpointTable();
	private LinkedHashMap<String, Function<HTTP_REQUEST, HTTP_RESULT>> ERROR_EP_LIST = new LinkedHashMap<>();
	private final HashMap<String, String> ExtMimeList = new HashMap<>() {{
		put("txt", "text/plain; charset=UTF-8");
		put("html", "text/html; charset=UTF-8");
		put("htm", "text/html; charset=UTF-8");
		put("xhtml", "application/xhtml+xml");
		put("css", "text/css; charset=UTF-8");
		put("js", "text/javascript; charset=UTF-8");
		put("svg", "image/svg+xml; charset=UTF-8");
		put("png", "image/png");
		put("gif", "image/gif");
		put("bmp", "image/bmp");
		put("jpeg", "image/jpeg");
		put("jpg", "image/jpeg");
		put("mp3", "audio/mpeg");
		put("ico", "image/vnd.microsoft.icon");
		put("weba", "audio/webm");
		put("webm", "video/webm");
		put("webp", "image/webp");
		put("xml", "application/xml");
		put("zip", "application/zip");
		put("pdf", "application/pdf");
	}};

	public SmartHTTP(int PORT) {
		//
		this.PORT = PORT;
	}

	/**
	 * エンドポイントを設定します
	 * @param PATH パス(*と:が使えます)
	 * @param RESULT エンドポイントとなる関数を設定して
	 */
	public void SetRoute(String PATH, EndpointFunction RESULT) {
		PATH = SlasshFucker(PATH);

		//正規表現に変換する
		String REGEX = "^";
		REGEX += PATH.replaceAll("\\*", ".*").replaceAll(":(\\w+)", "(?<$1>[^/]+)");
		REGEX += "$";

		ET.Set(new EndpointEntrie(REGEX, EndpointEntrie.Method.ALL, RESULT));
	}

	/**
	 * エンドポイントを設定します
	 * @param PATH パス(*と:が使えます)
	 * @param RESULT エンドポイントとなる関数を設定して
	 * @param M メソッドを設定
	 */
	public void SetRoute(String PATH, EndpointEntrie.Method M, EndpointFunction RESULT) {
		PATH = SlasshFucker(PATH);

		//正規表現に変換する
		String REGEX = "^";
		REGEX += PATH.replaceAll("\\*", ".*").replaceAll(":(\\w+)", "(?<$1>[^/]+)");
		REGEX += "$";

		ET.Set(new EndpointEntrie(REGEX, M, RESULT));
	}

	/**
	 * パスとリソースフォルダを関連付けます
	 * @param PATH リクエストのパス(StartWithです)
	 * @param ResourcePath リソースフォルダのパス
	 */
	public void SetResourceDir(String PATH, String ResourcePath, Class ResourceClass) {
		//語尾に/が無いなら追加する
		if (!PATH.endsWith("/")) {
			PATH = PATH + "/";
		}
		if (!ResourcePath.endsWith("/")) {
			ResourcePath = ResourcePath + "/";
		}

		String PATHPATH = PATH;//←Javaのクソ仕様
		String ResourcePathPath = ResourcePath;
		SetRoute(PATH + "*", new EndpointFunction() {
			@Override
			public HTTP_RESULT Run(HTTP_REQUEST e) {
				try {
					RESOURCE_MANAGER RM = new RESOURCE_MANAGER(ResourceClass);
					String REQUEST_FILE = e.GetEVENT().getURI().getPath().replaceFirst(PATHPATH, "");

					//リクエストが無なら/を追記する
					if (REQUEST_FILE.equals("")) {
						REQUEST_FILE += "/";
					}

					//リクエストの最後が/なら/index.htmlを追記
					if (REQUEST_FILE.endsWith("/")) {
						REQUEST_FILE += "index.html";
					}

					String RESOURCE_FILE = ResourcePathPath + REQUEST_FILE;

					//リソースファイルがあるか
					if (RM.Exists(RESOURCE_FILE)) {
						String EXT = RESOURCE_FILE.split("\\.")[RESOURCE_FILE.split("\\.").length - 1];
						String MIME = "application/octet-stream";

						if (ExtMimeList.get(EXT) != null) {
							MIME = ExtMimeList.get(EXT);
						}

						//ファイルを返す
						return new HTTP_RESULT(200, RM.getResourceData(RESOURCE_FILE), MIME);
					} else {
						//ファイルならないなら404
						HashMap<String, String> PARAM_LIST = new HashMap<>();
						PARAM_LIST.put("EX", "404");
						ReturnErrorPage(e.GetEVENT(), PARAM_LIST, e.GetEVENT().getURI().getPath(), ERRORCODE.PAGE_NOT_FOUND, 404);

						return null;
					}
				} catch (Exception EX) {
					//エラー
					HashMap<String, String> PARAM_LIST = new HashMap<>();
					PARAM_LIST.put("EX", EXCEPTION_READER.READ(EX));
					ReturnErrorPage(e.GetEVENT(), PARAM_LIST, e.GetEVENT().getURI().getPath(), ERRORCODE.INTERNAL_SERVER_ERROR, 500);
					return null;
				}
			}
		});
	}

	/**
	 * エラー画面を設定します
	 * @param PATH パス(StartWithでチェックしてる)
	 * @param CODE エラーコード
	 * @param RESULT エンドポイントとなる関数を設定して
	 */
	public void SetError(String PATH, ERRORCODE CODE, Function<HTTP_REQUEST, HTTP_RESULT> RESULT) {
		String NAME = SlasshFucker(PATH) + ":" + CODE.name();
		ERROR_EP_LIST.put(NAME, RESULT);
	}

	public void Start() throws InterruptedException {
		HS = new HTTP_SERVER(PORT);
		HS.setVERBOSE(true);
		HS.SET_EVENT_VOID(new HTTP_EVENT_LISTENER() {
			@Override
			public void REQUEST_EVENT(HTTP_EVENT E) {
				try {
					String REQUEST_PATH = E.getURI().getPath();
					REQUEST_PATH = SlasshFucker(REQUEST_PATH);
					EndpointEntrie Entrie = ET.Get(REQUEST_PATH, MethodStringToEnum.Convert(E.getMethod()));

					if (Entrie != null) {
						HTTP_RESULT RESULT = Entrie.RunFunction(E);
						if (RESULT != null) {
							if (RESULT.MIME != null) {
								E.setHEADER("Content-Type", RESULT.MIME);
							}

							E.REPLY_BYTE(RESULT.STATUS, RESULT.DATA);
						}
					} else {
						HashMap<String, String> PARAM_LIST = new HashMap<>();
						PARAM_LIST.put("EX", "404");
						ReturnErrorPage(E, PARAM_LIST, E.getURI().getPath(), ERRORCODE.PAGE_NOT_FOUND, 404);
					}
				} catch (Exception EX) {
					//500エラー
					HashMap<String, String> PARAM_LIST = new HashMap<>();
					PARAM_LIST.put("EX", EXCEPTION_READER.READ(EX));
					ReturnErrorPage(E, PARAM_LIST, E.getURI().getPath(), ERRORCODE.INTERNAL_SERVER_ERROR, 500);
				}
			}
		});
		HS.START_HTTPSERVER();
	}

	private void ReturnErrorPage(HTTP_EVENT E, HashMap<String, String> PARAM_LIST, String PATH, ERRORCODE ERRCODE, int CODE) {
		try {
			Function<HTTP_REQUEST, HTTP_RESULT> ERROR_EP = GetErrorEP(PATH, ERRCODE);
			if (ERROR_EP != null) {
				HTTP_RESULT RESULT = ERROR_EP.apply(new HTTP_REQUEST(E, PARAM_LIST));

				if (RESULT.MIME != null) {
					E.setHEADER("Content-Type", RESULT.MIME);
				}

				E.REPLY_BYTE(CODE, RESULT.DATA);
			} else {
				E.setHEADER("Content-Type", "text/plain; charset=UTF-8");
				E.REPLY_BYTE(CODE, ERRCODE.name().getBytes());
			}
		} catch (Exception EX2) {
			//もみ消す
		}
	}

	private Function<HTTP_REQUEST, HTTP_RESULT> GetErrorEP(String PATH, ERRORCODE CODE) {
		for (String ROW:ERROR_EP_LIST.keySet()) {
			String ROW_PATH = "/" + ROW.split(":")[0];
			String ROW_ERR = ROW.split(":")[1];
			if (PATH.startsWith(ROW_PATH) && ROW_ERR.equals(CODE.name())) {
				return ERROR_EP_LIST.get(ROW);
			}
		}
		return null;
	}

	//先頭が/なら破壊
	private String SlasshFucker(String PATH) {
		if (PATH.startsWith("/")) {
			PATH = PATH.replaceFirst("/", "");
		}
		return PATH;
	}
}

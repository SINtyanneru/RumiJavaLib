package su.rumishistem.rumi_java_lib.SmartHTTP;

import su.rumishistem.rumi_java_lib.EXCEPTION_READER;
import su.rumishistem.rumi_java_lib.HTTP_SERVER.HTTP_EVENT;
import su.rumishistem.rumi_java_lib.HTTP_SERVER.HTTP_EVENT_LISTENER;
import su.rumishistem.rumi_java_lib.HTTP_SERVER.HTTP_SERVER;
import su.rumishistem.rumi_java_lib.RESOURCE.RESOURCE_MANAGER;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.function.Function;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SmartHTTP {
	private int PORT;
	private HTTP_SERVER HS;
	private LinkedHashMap<String, Function<HTTP_REQUEST, HTTP_RESULT>> EP_LIST = new LinkedHashMap<>();
	private LinkedHashMap<String, Function<HTTP_REQUEST, HTTP_RESULT>> ERROR_EP_LIST = new LinkedHashMap<>();

	public SmartHTTP(int PORT) {
		//
		this.PORT = PORT;
	}

	/**
	 * エンドポイントを設定します
	 * @param PATH パス(*と:が使えます)
	 * @param RESULT エンドポイントとなる関数を設定して
	 */
	public void SetRoute(String PATH, Function<HTTP_REQUEST, HTTP_RESULT> RESULT) {
		PATH = SlasshFucker(PATH);

		//正規表現に変換する
		String REGEX = "^";
		REGEX += PATH.replaceAll("\\*", ".*").replaceAll(":(\\w+)", "(?<$1>[^/]+)");
		REGEX += "$";

		EP_LIST.put(REGEX, RESULT);
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
		SetRoute(PATH + "*", new Function<HTTP_REQUEST, HTTP_RESULT>() {
			@Override
			public HTTP_RESULT apply(HTTP_REQUEST e) {
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
						String MIME = "application/octet-stream";
						if (RESOURCE_FILE.endsWith(".html") || RESOURCE_FILE.endsWith(".htm")) {
							MIME = "text/html; charset=UTF-8";
						} else if (RESOURCE_FILE.endsWith(".css")) {
							MIME = "text/css; charset=UTF-8";
						} else if (RESOURCE_FILE.endsWith(".js")) {
							MIME = "text/javascript; charset=UTF-8";
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

	public void Start() throws IOException {
		HS = new HTTP_SERVER(PORT);
		HS.setVERBOSE(true);
		HS.SET_EVENT_VOID(new HTTP_EVENT_LISTENER() {
			@Override
			public void REQUEST_EVENT(HTTP_EVENT E) {
				try {
					String REQUEST_PATH = E.getEXCHANGE().getRequestURI().getPath();
					REQUEST_PATH = SlasshFucker(REQUEST_PATH);

					//エンドポイント一覧を回す
					for (String PATH:EP_LIST.keySet()) {
						Pattern PATTERN = Pattern.compile(PATH);
						Matcher MATCHER = PATTERN.matcher(REQUEST_PATH);
						//見つけろ(ちなみに最初にfindを実行しないとgroupでエラー出るからな)
						if (MATCHER.find()) {
							HashMap<String, String> PARAM_LIST = new HashMap<>();

							//パラメーターを取得する
							for (String GROUP_NAME:PATTERN.namedGroups().keySet()) {
								PARAM_LIST.put(GROUP_NAME, MATCHER.group(GROUP_NAME));
							}

							//関数を実行
							if (MATCHER.matches()) {
								Function<HTTP_REQUEST, HTTP_RESULT> VOID = EP_LIST.get(PATH);
								HTTP_RESULT RESULT = VOID.apply(new HTTP_REQUEST(E, PARAM_LIST));

								if (RESULT != null) {
									if (RESULT.MIME != null) {
										E.setHEADER("Content-Type", RESULT.MIME);
									}

									E.REPLY_BYTE(RESULT.STATUS, RESULT.DATA);
								} else {
									//Nullなら切断
									E.getEXCHANGE().close();
								}
								return;
							}
						}
					}

					HashMap<String, String> PARAM_LIST = new HashMap<>();
					PARAM_LIST.put("EX", "404");
					ReturnErrorPage(E, PARAM_LIST, E.getEXCHANGE().getRequestURI().getPath(), ERRORCODE.PAGE_NOT_FOUND, 404);
				} catch (Exception EX) {
					//500エラー
					HashMap<String, String> PARAM_LIST = new HashMap<>();
					PARAM_LIST.put("EX", EXCEPTION_READER.READ(EX));
					ReturnErrorPage(E, PARAM_LIST, E.getEXCHANGE().getRequestURI().getPath(), ERRORCODE.INTERNAL_SERVER_ERROR, 500);
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
			String ROW_PATH = ROW.split(":")[0];
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

package su.rumishistem.rumi_java_lib.RSV;

import java.util.LinkedHashMap;
import java.util.Map;

public class RSVErrorCode {
	public static Map<String, Object> bad_request(String trace) {
		return  gen("0x4000", "仕様通りではないリクエスト", trace);
	}

	public static Map<String, Object> husei_request(String trace) {
		return gen("0x4001", "不正なリクエスト", trace);
	}

	public static Map<String, Object> unauthorized_request(String trace) {
		return gen("0x4002", "認証エラー", trace);
	}

	public static Map<String, Object> permission_request(String trace) {
		return gen("0x4006", "権限エラー", trace);
	}

	public static Map<String, Object> conflict_request(String trace) {
		return gen("0x4003", "リクエストが衝突した", trace);
	}

	public static Map<String, Object> contents_not_found_request(String trace) {
		return gen("0x4004", " リクエストに応じれるコンテンツがない", trace);
	}

	public static Map<String, Object> endpoint_not_found_request(String trace) {
		return gen("0x4005", " エンドポイントが存在しない", trace);
	}

	public static Map<String, Object> overflow(String trace) {
		return gen("0x4007", "  オーバーフロー", trace);
	}

	public static Map<String, Object> system_error(String trace) {
		return gen("0x5000", "システムエラー", trace);
	}

	public static Map<String, Object> smtp_server_error(String trace) {
		return gen("0x6001", "SMTP間通信エラー", trace);
	}

	public static Map<String, Object> activitypub_server_error(String trace) {
		return gen("0x6002", "ActivityPub間通信エラー", trace);
	}

	public static Map<String, Object> renkei_server_error(String trace) {
		return gen("0x6003", "連携通信エラー", trace);
	}

	private static Map<String, Object> gen(String error_code, String message, String trace) {
		Map<String, Object> r = new LinkedHashMap<String, Object>();
		r.put("STATUS", false);
		r.put("ERROR", new LinkedHashMap<String, String>(){
			{
				put("CODE", error_code);
				put("MESSAGE", message);
				put("TRACE", trace);
			}
		});
		return r;
	}
}

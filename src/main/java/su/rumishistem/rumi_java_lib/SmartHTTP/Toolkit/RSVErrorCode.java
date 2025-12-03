package su.rumishistem.rumi_java_lib.SmartHTTP.Toolkit;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import su.rumishistem.rumi_java_lib.EXCEPTION_READER;
import su.rumishistem.rumi_java_lib.SmartHTTP.HTTP_RESULT;

import java.nio.charset.StandardCharsets;
import java.util.LinkedHashMap;

public class RSVErrorCode {
	public static HTTP_RESULT bad_request(String trace) {
		return new HTTP_RESULT(400, gen("0x4000", "仕様通りではないリクエスト", trace), "application/json");
	}

	public static HTTP_RESULT husei_request(String trace) {
		return new HTTP_RESULT(401, gen("0x4001", "不正なリクエスト", trace), "application/json");
	}

	public static HTTP_RESULT unauthorized_request(String trace) {
		return new HTTP_RESULT(401, gen("0x4002", "認証エラー", trace), "application/json");
	}

	public static HTTP_RESULT permission_request(String trace) {
		return new HTTP_RESULT(401, gen("0x4006", "権限エラー", trace), "application/json");
	}

	public static HTTP_RESULT conflict_request(String trace) {
		return new HTTP_RESULT(409, gen("0x4003", "リクエストが衝突した", trace), "application/json");
	}

	public static HTTP_RESULT contents_not_found_request(String trace) {
		return new HTTP_RESULT(404, gen("0x4004", " リクエストに応じれるコンテンツがない", trace), "application/json");
	}

	public static HTTP_RESULT endpoint_not_found_request(String trace) {
		return new HTTP_RESULT(404, gen("0x4005", " エンドポイントが存在しない", trace), "application/json");
	}

	public static HTTP_RESULT overflow(String trace) {
		return new HTTP_RESULT(413, gen("0x4007", "  オーバーフロー", trace), "application/json");
	}

	public static HTTP_RESULT system_error(String trace) {
		return new HTTP_RESULT(500, gen("0x5000", "システムエラー", trace), "application/json");
	}

	public static HTTP_RESULT smtp_server_error(String trace) {
		return new HTTP_RESULT(521, gen("0x6001", "SMTP間通信エラー", trace), "application/json");
	}

	public static HTTP_RESULT activitypub_server_error(String trace) {
		return new HTTP_RESULT(521, gen("0x6002", "ActivityPub間通信エラー", trace), "application/json");
	}

	public static HTTP_RESULT renkei_server_error(String trace) {
		return new HTTP_RESULT(521, gen("0x6003", "連携通信エラー", trace), "application/json");
	}

	private static byte[] gen(String error_code, String message, String trace) {
		LinkedHashMap<String, Object> RETURN = new LinkedHashMap<String, Object>();
		RETURN.put("STATUS", false);
		RETURN.put("ERROR", new LinkedHashMap<String, String>(){
			{
				put("CODE", error_code);
				put("MESSAGE", message);
				put("TRACE", trace);
			}
		});

		try {
			return new ObjectMapper().writeValueAsString(RETURN).getBytes(StandardCharsets.UTF_8);
		} catch (JsonProcessingException e) {
			//おこるわけないよ...
			throw new RuntimeException(e);
		}
	}
}

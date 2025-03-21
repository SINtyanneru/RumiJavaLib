package su.rumishistem.rumi_java_lib.HTTP_SERVER;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.HttpMethod;
import io.netty.handler.codec.http.QueryStringDecoder;
import su.rumishistem.rumi_java_lib.LOG_PRINT.LOG_TYPE;

import java.util.HashMap;
import java.util.Map;

import static su.rumishistem.rumi_java_lib.LOG_PRINT.Main.LOG;

public class HTTPHandler extends SimpleChannelInboundHandler<FullHttpRequest> {
	private HTTP_SERVER HS  = null;

	public HTTPHandler(HTTP_SERVER HS) {
		this.HS = HS;
	}

	@Override
	protected void channelRead0(ChannelHandlerContext CTX, FullHttpRequest r) throws Exception {
		String URI = r.uri();
		HttpMethod Method = r.method();

		//ログ
		if (HS.VERBOSE) {
			//禁止ワードが含まれていないならログを吐く
			if(!URI.contains("SESSION")){
				LOG(LOG_TYPE.INFO, "HTTP Request:" + Method + " " + URI);
			} else {
				LOG(LOG_TYPE.INFO, "HTTP Request:" + Method + " ▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓");
			}
		}

		//URIパラメーター
		HashMap<String, String> URIParamMap = new HashMap<String, String>();//URIパラメーターを解析した結果のハッシュマップ
		int MarkIndex = URI.indexOf("?");
		if (MarkIndex != -1) {//?が見つかった場合
			//?以降の文字列を取得
			String[] URIParamArray = URI.substring(MarkIndex + 1).split("&");
			for (String URIParam : URIParamArray) {
				String[] SPLIT = URIParam.split("=");
				//ヌルチェック
				if (SPLIT.length == 2) {
					URIParamMap.put(SPLIT[0], SPLIT[1]);
				}
			}
		}

		//リクエストボディー
		byte[] RequestBody = new byte[0];
		if (r.content().isReadable()) {
			ByteBuf Content = r.content();
			RequestBody = new byte[Content.readableBytes()];
			Content.readBytes(RequestBody);
		}

		//リクエストヘッダー
		HashMap<String, String> RequestHeader = new HashMap<>();
		for (Map.Entry<String, String> H:r.headers()) {
			RequestHeader.put(H.getKey().toUpperCase(), H.getValue());
		}

		//イベントを発火する
		HTTP_EVENT_LISTENER[] LISTENER_LIST = HS.EL_LIST.getListeners(HTTP_EVENT_LISTENER.class);
		for(HTTP_EVENT_LISTENER LISTENER:LISTENER_LIST){
			LISTENER.REQUEST_EVENT(new HTTP_EVENT(this, CTX, r, URIParamMap, RequestBody, RequestHeader));
		}
	}
}

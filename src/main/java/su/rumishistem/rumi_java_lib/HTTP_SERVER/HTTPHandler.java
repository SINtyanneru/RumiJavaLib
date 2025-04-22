package su.rumishistem.rumi_java_lib.HTTP_SERVER;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.*;
import su.rumishistem.rumi_java_lib.LOG_PRINT.LOG_TYPE;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import static su.rumishistem.rumi_java_lib.LOG_PRINT.Main.LOG;

public class HTTPHandler extends SimpleChannelInboundHandler<HttpObject> {
	private HTTP_SERVER HS = null;
	private ByteArrayOutputStream RequestBodyStream = new ByteArrayOutputStream();
	private HashMap<String, String> URIParamMap = new HashMap<>();
	private HashMap<String, String> RequestHeader = new HashMap<>();
	private String URI;
	private HttpMethod Method;
	private HttpRequest request;

	public HTTPHandler(HTTP_SERVER HS) {
		this.HS = HS;
	}

	@Override
	protected void channelRead0(ChannelHandlerContext CTX, HttpObject msg) throws Exception {
		if (msg instanceof HttpRequest) {
			request = (HttpRequest) msg;
			URI = request.uri();
			Method = request.method();

			// ログ
			if (HS.VERBOSE) {
				if (!URI.contains("SESSION")) {
					LOG(LOG_TYPE.INFO, "HTTP Request:" + Method + " " + URI);
				} else {
					LOG(LOG_TYPE.INFO, "HTTP Request:" + Method + " ▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓");
				}
			}

			// URI パラメータ解析
			URIParamMap.clear();
			int MarkIndex = URI.indexOf("?");
			if (MarkIndex != -1) {
				String[] URIParamArray = URI.substring(MarkIndex + 1).split("&");
				for (String URIParam : URIParamArray) {
					String[] SPLIT = URIParam.split("=");
					if (SPLIT.length == 2) {
						URIParamMap.put(SPLIT[0], SPLIT[1]);
					}
				}
			}

			// ヘッダー解析
			RequestHeader.clear();
			for (Map.Entry<String, String> H : request.headers()) {
				RequestHeader.put(H.getKey().toUpperCase(), H.getValue());
			}

			// ボディの初期化
			RequestBodyStream.reset();
		}

		if (msg instanceof HttpContent) {
			HttpContent content = (HttpContent) msg;
			ByteBuf buf = content.content();
			byte[] chunk = new byte[buf.readableBytes()];
			buf.readBytes(chunk);

			//送られたデータのサイズが最大値を超えていないことを確認する(超えてたら無視する)
			if (RequestBodyStream.size() < HS.MaxBodySize) {
				RequestBodyStream.write(chunk); // 受信データを蓄積
			}

			// 最後のチャンクを受信したらリクエスト処理を実行
			if (msg instanceof LastHttpContent) {
				byte[] RequestBodyBytes = RequestBodyStream.toByteArray();

				// イベントを発火
				HTTP_EVENT_LISTENER[] LISTENER_LIST = HS.EL_LIST.getListeners(HTTP_EVENT_LISTENER.class);
				for (HTTP_EVENT_LISTENER LISTENER : LISTENER_LIST) {
					LISTENER.REQUEST_EVENT(new HTTP_EVENT(this, CTX, request, URIParamMap, RequestBodyBytes, RequestHeader));
				}
			}
		}
	}

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
		LOG(LOG_TYPE.FAILED, "HTTPHandler Error: " + cause.getMessage());
		ctx.close();
	}
}

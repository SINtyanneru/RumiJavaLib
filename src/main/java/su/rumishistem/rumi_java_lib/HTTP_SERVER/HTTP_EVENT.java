package su.rumishistem.rumi_java_lib.HTTP_SERVER;

import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.*;

import javax.swing.event.EventListenerList;
import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.charset.StandardCharsets;
import java.util.EventObject;
import java.util.HashMap;
import java.util.Map;

public class HTTP_EVENT extends EventObject {
	private ChannelHandlerContext CTX;
	private HttpRequest r;
	private HashMap<String, String> URI_PARAM;
	private byte[] POST_DATA;
	private HashMap<String, String> HEADER_DATA;
	private HttpHeaders RES_HEADER;
	private HashMap<String, String> ResponseHeader = new HashMap<>();

	public HTTP_EVENT(Object source, ChannelHandlerContext CTX, HttpRequest r, HashMap<String, String> URI_PARAM, byte[] POST_DATA, HashMap<String, String> HEADER_DATA) {
		super(source);

		this.CTX = CTX;
		this.r = r;
		this.URI_PARAM = URI_PARAM;
		this.POST_DATA = POST_DATA;
		this.HEADER_DATA = HEADER_DATA;

		RES_HEADER = r.headers();
	}

	public HttpRequest getEXCHANGE(){
		return r;
	}

	public HashMap<String, String> getURI_PARAM(){
		return URI_PARAM;
	}

	public String getPOST_DATA(){
		return new String(POST_DATA, StandardCharsets.UTF_8);
	}

	public byte[] getPOST_DATA_BIN(){
		return POST_DATA;
	}

	public HashMap<String, String> getHEADER_DATA(){
		return HEADER_DATA;
	}

	public URI getURI() {
		try {
			return new URI(r.uri());
		} catch (Exception EX) {
			return null;
		}
	}

	public String getMethod() {
		return r.method().name().toUpperCase();
	}

	public HashMap<String, String> getCookie() {
		HashMap<String, String> CookieList = new HashMap<>();

		if (HEADER_DATA.get("COOKIE") != null) {
			for (String Cookie:HEADER_DATA.get("COOKIE").split(";")) {
				String KEY = Cookie.split("=")[0];
				String VAL = Cookie.split("=")[1];
				CookieList.put(KEY, VAL);
			}
		}

		return CookieList;
	}

	/**
	 * クッキーをセットします
	 * @param NAME
	 * @param VAL
	 */
	public void setCookie(String NAME, String VAL, long Sec, String DOMAIN, String PATH, boolean SSL, boolean HTTPOnly) {
		ResponseHeader.put("Set-Cookie", NAME+"="+VAL+";Max-Age="+Sec+";Path="+PATH);
	}

	/**
	 * ヘッダーをセットします
	 * @param KEY キー
	 * @param VAL 内容
	 */
	public void setHEADER(String KEY, String VAL){
		ResponseHeader.put(KEY, VAL);
	}

	/**
	 * バイトの配列で応答を返します
	 * @throws IOException 例外
	 */
	public void REPLY_BYTE(int STATUS, byte[] BODY) throws IOException {
		FullHttpResponse Response = new DefaultFullHttpResponse(
			HttpVersion.HTTP_1_1,
			HttpResponseStatus.valueOf(STATUS),
			Unpooled.wrappedBuffer(BODY)
		);

		//ヘッダー
		for (Map.Entry<String, String> Entry:ResponseHeader.entrySet()) {
			Response.headers().set(Entry.getKey(), Entry.getValue());
		}
		Response.headers().set(HttpHeaderNames.CONTENT_LENGTH, BODY.length);

		//HEADなら本文を破壊する
		if (r.method().equals(HttpMethod.HEAD)) {
			Response.content().clear();
		}

		//送信
		CTX.writeAndFlush(Response).addListener(ChannelFutureListener.CLOSE);
	}

	/**
	 * 文字列型で応答を返します
	 * @throws IOException 例外
	 */
	public void REPLY_String(int STATUS, String BODY) throws IOException {
		byte[] BS = BODY.getBytes(StandardCharsets.UTF_8);
		REPLY_BYTE(STATUS, BS);
	}
}

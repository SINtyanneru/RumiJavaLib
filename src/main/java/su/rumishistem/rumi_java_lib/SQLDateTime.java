package su.rumishistem.rumi_java_lib;

import java.sql.Timestamp;
import java.time.Instant;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;

public class SQLDateTime {
	private static Instant convert(ArrayData d) {
		Timestamp ts = (Timestamp)d.asObject();
		return ts.toInstant();
	}

	public static String format_12h(ArrayData d, ZoneOffset tz) {
		Instant instant = convert(d);
		return instant.atOffset(tz).format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
	}

	public static String format_12h(ArrayData d, int tz) {
		Instant instant = convert(d);
		return instant.atOffset(ZoneOffset.ofHours(tz)).format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
	}

	public static String format_iso(ArrayData d) {
		Instant instant = convert(d);
		return instant.atOffset(ZoneOffset.ofHours(9)).format(DateTimeFormatter.ISO_OFFSET_DATE_TIME);
	}
}

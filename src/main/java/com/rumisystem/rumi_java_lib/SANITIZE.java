package com.rumisystem.rumi_java_lib;

public class SANITIZE {
	public static String CONSOLE_SANITIZE(String TEXT) {
		TEXT = TEXT.replaceAll("\u0000", "[NULL]");
		TEXT = TEXT.replaceAll("\u0001", "[SOH]");
		TEXT = TEXT.replaceAll("\u0002", "[STX]");
		TEXT = TEXT.replaceAll("\u0003", "[ETX]");
		TEXT = TEXT.replaceAll("\u0004", "[EOT]");
		TEXT = TEXT.replaceAll("\u0005", "[ENQ]");
		TEXT = TEXT.replaceAll("\u0006", "[ACK]");
		TEXT = TEXT.replaceAll("\u0007", "[BEL]");
		TEXT = TEXT.replaceAll("\u0008", "[BS]");
		TEXT = TEXT.replaceAll("\u0009", "[HT]");
		//TEXT = TEXT.replaceAll("\n", "[LF]");
		TEXT = TEXT.replaceAll("\u000b", "[VT]");
		TEXT = TEXT.replaceAll("\u000c", "[FF]");
		//TEXT = TEXT.replaceAll("\r", "[CR]");
		TEXT = TEXT.replaceAll("\u000e", "[SO]");
		TEXT = TEXT.replaceAll("\u000f", "[SI]");
		TEXT = TEXT.replaceAll("\u0010", "[DLE]");
		TEXT = TEXT.replaceAll("\u0011", "[DC1]");
		TEXT = TEXT.replaceAll("\u0012", "[DC2]");
		TEXT = TEXT.replaceAll("\u0013", "[DC3]");
		TEXT = TEXT.replaceAll("\u0014", "[DC4]");
		TEXT = TEXT.replaceAll("\u0015", "[NAK]");
		TEXT = TEXT.replaceAll("\u0016", "[SYN]");
		TEXT = TEXT.replaceAll("\u0017", "[ETB]");
		TEXT = TEXT.replaceAll("\u0018", "[CAN]");
		TEXT = TEXT.replaceAll("\u0019", "[EM]");
		TEXT = TEXT.replaceAll("\u001a", "[SUB]");
		TEXT = TEXT.replaceAll("\u001b", "[EXT/ANSI]");
		TEXT = TEXT.replaceAll("\u001c", "[FS]");
		TEXT = TEXT.replaceAll("\u001d", "[GS]");
		TEXT = TEXT.replaceAll("\u001e", "[RS]");
		TEXT = TEXT.replaceAll("\u001f", "[US]");
		TEXT = TEXT.replaceAll("\u007f", "[DEL]");


		return TEXT;
	}
}

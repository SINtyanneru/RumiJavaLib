package com.rumisystem.rumi_java_lib;

public class SANITIZE {
	public static String CONSOLE_SANITIZE(String TEXT) {
		TEXT = TEXT.replace("\u0000", "[NULL]");
		TEXT = TEXT.replace("\u0001", "[SOH]");
		TEXT = TEXT.replace("\u0002", "[STX]");
		TEXT = TEXT.replace("\u0003", "[ETX]");
		TEXT = TEXT.replace("\u0004", "[EOT]");
		TEXT = TEXT.replace("\u0005", "[ENQ]");
		TEXT = TEXT.replace("\u0006", "[ACK]");
		TEXT = TEXT.replace("\u0007", "[BEL]");
		TEXT = TEXT.replace("\u0008", "[BS]");
		TEXT = TEXT.replace("\u0009", "[HT]");
		//TEXT = TEXT.replace("\n", "[LF]");
		TEXT = TEXT.replace("\u000b", "[VT]");
		TEXT = TEXT.replace("\u000c", "[FF]");
		//TEXT = TEXT.replace("\r", "[CR]");
		TEXT = TEXT.replace("\u000e", "[SO]");
		TEXT = TEXT.replace("\u000f", "[SI]");
		TEXT = TEXT.replace("\u0010", "[DLE]");
		TEXT = TEXT.replace("\u0011", "[DC1]");
		TEXT = TEXT.replace("\u0012", "[DC2]");
		TEXT = TEXT.replace("\u0013", "[DC3]");
		TEXT = TEXT.replace("\u0014", "[DC4]");
		TEXT = TEXT.replace("\u0015", "[NAK]");
		TEXT = TEXT.replace("\u0016", "[SYN]");
		TEXT = TEXT.replace("\u0017", "[ETB]");
		TEXT = TEXT.replace("\u0018", "[CAN]");
		TEXT = TEXT.replace("\u0019", "[EM]");
		TEXT = TEXT.replace("\u001a", "[SUB]");
		TEXT = TEXT.replace("\u001b", "[EXT/ANSI]");
		TEXT = TEXT.replace("\u001c", "[FS]");
		TEXT = TEXT.replace("\u001d", "[GS]");
		TEXT = TEXT.replace("\u001e", "[RS]");
		TEXT = TEXT.replace("\u001f", "[US]");
		TEXT = TEXT.replace("\u007f", "[DEL]");

		return TEXT;
	}

}

package com.rumisystem.rumi_java_lib;

public class SANITIZE {
	public static String CONSOLE_SANITIZE(String TEXT) {
		TEXT = TEXT.replaceAll("\b", "[BEL]");
		TEXT = TEXT.replaceAll("\u001b", "[ANSI]");


		return TEXT;
	}
}

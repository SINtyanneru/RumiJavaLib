package com.rumisystem.rumi_java_lib;

import java.io.PrintWriter;
import java.io.StringWriter;

public class EXCEPTION_READER {
	public static String READ(Exception EX) {
		StringWriter SW = new StringWriter();
		PrintWriter PW = new PrintWriter(SW);

		//エラーを焼く
		EX.printStackTrace(PW);
		PW.flush();

		return SW.toString();
	}
}

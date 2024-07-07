package com.rumisystem.rumi_java_lib;

public class Main {
	public static void main(String[] args) {
		SQL.CONNECT("192.168.0.130", "3306", "ACCOUNT", "rumiabot", "sin1234zxntv");

		ArrayNode RESULT = SQL.RUN("SELECT * FROM `ACCOUNT` LIMIT 10; ", new Object[]{});

		for(Object ROW:RESULT.asArrayList()){
			ArrayNode ROOW = (ArrayNode) ROW;
			System.out.println(ROOW.asObject("UID"));
		}
	}
}

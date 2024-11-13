package com.rumisystem.rumi_java_lib;

import java.util.ArrayList;
import java.util.List;

public class SEARCH_ENGINE {
	private String BASE_SQL_SCRIPT;

	/**
	 * SQLから検索を行います
	 * @param SQL_SCRIPT 「{WHERE}」に検索用の文が挿入されます
	 */
	public SEARCH_ENGINE(String SQL_SCRIPT) {
		//a
		this.BASE_SQL_SCRIPT = SQL_SCRIPT;
	}

	public ArrayNode SEARCH(String[] QUELI_LIST, String KARAM) {
		String SQL_SCRIPT = BASE_SQL_SCRIPT + "\n";

		//タグ検索
		String SS = "";
		List<String> PARAM = new ArrayList<String>();

		for (int I = 0; I < QUELI_LIST.length; I++) {
			String QUELI = QUELI_LIST[I];
			String PREFIX = "AND";

			if (I == 0) {
				PREFIX = "WHERE";
			}

			if (QUELI.startsWith("-")) {
				//マイナス検索
				SS += PREFIX + " " + KARAM + " NOT LIKE ?\n";
				PARAM.add(QUELI.replaceFirst("-", ""));
			} else if (QUELI.startsWith("\"") && QUELI.endsWith("\"")) {
				//絶対検索
				SS += PREFIX + " " + KARAM + " LIKE ?\n";
				PARAM.add(QUELI.replaceAll("\"", ""));
			} else {
				SS += PREFIX + " " + KARAM + " LIKE ?\n";
				PARAM.add("%" + QUELI + "%");
			}
		}

		SQL_SCRIPT = SQL_SCRIPT.replace("{WHERE}", SS);

		return SQL.RUN(SQL_SCRIPT, PARAM.toArray());
	}
}

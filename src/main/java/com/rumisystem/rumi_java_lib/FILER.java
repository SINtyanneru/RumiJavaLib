package com.rumisystem.rumi_java_lib;

import org.checkerframework.checker.units.qual.C;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.Collections;

public class FILER {
	private File FILE = null;
	private InputStream ISTREAM = null;

	public FILER(File FILE) throws FileNotFoundException {
		//引数をグローバル変数に代入
		this.FILE = FILE;
	}

	public FILER(InputStream STREAM){
		//引数をグローバル変数に代入
		this.ISTREAM = STREAM;
	}

	/**
	 * ファイルを文字列型で読み込みます
	 * @return ファイルの内容
	 * @throws IOException エラー
	 */
	public String OPEN_STRING() throws IOException {
		BufferedReader BR = null;

		//InputStreamとFileのどちらに代入されているかチェックして、BRを作る
		if(ISTREAM != null){
			BR = new BufferedReader(new InputStreamReader(ISTREAM));
		} else {
			BR = new BufferedReader(new FileReader(FILE));
		}

		//読み込む
		String DATA = new String(Files.readAllBytes(FILE.toPath()));

		//開放
		BR.close();

		//結果を返す
		return DATA;
	}

	/**
	 * ファイルを文字列型で書き込みます
	 * @param CONTENTS ファイルの内容
	 * @throws IOException エラー
	 */
	public void WRITE_STRING(String CONTENTS) throws IOException{
		//ファイルがなければ作る
		if(!FILE.exists()){
			FILE.createNewFile();
		}

		//書き込み用意
		BufferedWriter BW = new BufferedWriter(new FileWriter(FILE));

		//書き込み
		BW.write(CONTENTS);

		//開放
		BW.close();
	}
}

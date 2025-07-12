package su.rumishistem.rumi_java_lib;

import java.io.File;
import java.io.IOException;

public class ConfigLoader {
	public ArrayNode DATA = new ArrayNode();

	//設定ファイルの中身をかいせきする
	public ConfigLoader(File ConfigFile) throws IOException {
		String[] CONFIG_CONTENTS = new FILER(ConfigFile).OPEN_STRING().split("\n");

		String SEKSHON = "";

		for(String S:CONFIG_CONTENTS){
			//コメントは除外
			if (!S.startsWith("#")) {
				//セクションか否か
				if(S.startsWith("[") && S.endsWith("]")){
					SEKSHON = S.replace("[", "").replace("]", "");
					DATA.setDATA(SEKSHON, new ArrayNode());
				} else {
					//値
					if(!S.equals("")){
						String KEY = S.split("=")[0];
						String VAL = S.split("=")[1];

						if(VAL.startsWith("\"") && VAL.endsWith("\"")){
							StringBuilder TEXT = new StringBuilder();

							String[] SPLITERO = VAL.split("");
							for(int I = 1; I < SPLITERO.length; I++){
								String C = SPLITERO[I];
								//ダブルコートが来たら死ぬ
								if(C.equals("\"")){
									//その前の文字がバックスラッシュなら、虫する
									if(!SPLITERO[I - 1].equals("\\")){
										break;
									}
								}

								TEXT.append(C);
							}

							DATA.get(SEKSHON).setDATA(KEY, new ArrayData(TEXT.toString()));
						} else if(VAL.equals("true") || VAL.equals("false")) {
							if(VAL.equals("true")){
								DATA.get(SEKSHON).setDATA(KEY, new ArrayData(true));
							} else {
								DATA.get(SEKSHON).setDATA(KEY, new ArrayData(false));
							}
						} else if(VAL.matches("-?\\d+(\\.\\d+)?")) {
							DATA.get(SEKSHON).setDATA(KEY, new ArrayData(Integer.parseInt(VAL)));
						} else {
							DATA.get(SEKSHON).setDATA(KEY, new ArrayData(VAL));
						}
					}
				}
			}
		}
	}
}

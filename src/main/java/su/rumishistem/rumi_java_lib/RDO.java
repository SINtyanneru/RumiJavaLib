package su.rumishistem.rumi_java_lib;

public class RDO {
	public static ArrayNode PARSE(String TEXT){
		ArrayNode RESULT = new ArrayNode();
		String[] LINE_LIST = TEXT.split(";");

		//;区切りで読む(;は行終わりを意味する)
		for (int I = 0; I < LINE_LIST.length; I++) {
			String LINE = LINE_LIST[I];
			ArrayNode DATA = new ArrayNode();

			//&区切りで読む
			for (String S:LINE.split("&")) {
				String KEY = S.split("=")[0];
				String VAL = S.split("=")[1];

				DATA.setDATA(KEY, new ArrayData(VAL));
			}

			RESULT.setDATA(I, DATA);
		}

		return RESULT;
	}
}

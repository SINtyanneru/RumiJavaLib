package su.rumishistem.rumi_java_lib.REON4213;

import su.rumishistem.rumi_java_lib.REON4213.Type.VBlock;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class REON4213Parser {
	private String HacudouShi = null;
	private HashMap<String, List<VBlock>> ClsList = new HashMap<>();

	public REON4213Parser(String Text) {
		Matcher MTC = Pattern.compile("(?i)(?:QuelI)->\\{([\\s\\S]*)\\}->(?i)(?:ExeC)->\\{(.*)}").matcher(Text);
		if (MTC.find()) {
			String Content = MTC.group(1).replace("\t", "").replace("\n", "").replace("　", "");
			HacudouShi = MTC.group(2);

			Matcher CLSMTC = Pattern.compile("(?i)(?:Cls)\\((.*)\\)\\{(.*)\\};").matcher(Content);
			if (CLSMTC.find()) {
				String[] ClsContent = CLSMTC.group(2).split("\\} am \\{");
				List<VBlock> VList = new ArrayList<>();

				for (int I = 0; I < ClsContent.length; I++) {
					String S = ClsContent[I];
					Matcher VMTC = Pattern.compile("EX\\[(.*)\\]->\\{(.*)\\}").matcher(S);
					if (VMTC.find()) {
						VList.add(new VBlock(VMTC.group(2), VMTC.group(1)));
					}
				}

				ClsList.put(CLSMTC.group(1), VList);
			}
		}
	}

	public String GetHacudouShi() {
		return HacudouShi;
	}

	public HashMap<String, List<VBlock>> GetCls() {
		return ClsList;
	}
}

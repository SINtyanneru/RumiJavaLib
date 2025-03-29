package su.rumishistem.rumi_java_lib.REON4213;

import su.rumishistem.rumi_java_lib.REON4213.Type.VBlock;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class REON4213Parser {
	private String HacudouShi = null;
	private List<VBlock> VList = new ArrayList<>();

	public REON4213Parser(String Text) {
		Matcher MTC = Pattern.compile("(?i)(?:QuelI)->\\{([\\s\\S]*)\\}->(?i)(?:ExeC)->\\{(.*)}").matcher(Text);
		if (MTC.find()) {
			String Content = MTC.group(1).replace("\t", "").replace("\n", "").replace("　", "");
			HacudouShi = MTC.group(2);

			for (int I = 0; I < Content.split(";").length; I++) {
				String S = Content.split(";")[I];
				Matcher VMTC = Pattern.compile("EX\\[(.*)\\]->\\{(.*)\\}").matcher(S);
				if (VMTC.find()) {
					VList.add(new VBlock(VMTC.group(2), VMTC.group(1)));
				}
			}
		}
	}

	public String GetHacudouShi() {
		return HacudouShi;
	}

	public List<VBlock> GetVList() {
		return VList;
	}
}

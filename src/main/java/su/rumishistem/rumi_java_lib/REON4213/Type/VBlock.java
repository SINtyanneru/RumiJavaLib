package su.rumishistem.rumi_java_lib.REON4213.Type;

public class VBlock {
	private String O;
	private String V;

	//TODO:動詞のタイプも取得できたら良いよね。

	public VBlock(String O, String V) {
		this.O = O;
		this.V = V;
	}

	//目的語
	public String GetObject() {
		return O;
	}

	public String GetVerb() {
		return V;
	}
}

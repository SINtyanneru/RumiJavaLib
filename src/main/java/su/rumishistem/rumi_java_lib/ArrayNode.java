package su.rumishistem.rumi_java_lib;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

import org.checkerframework.checker.units.qual.K;

/**
 * `ArrayNode`はJacksonのJsonNodeを丸パクリしたやつです
 */
public class ArrayNode {
	private HashMap<Object, Object> NODE_DATA = new HashMap<>();

	private void KeyCheck(Object KEY) {
		if(!(KEY instanceof String || KEY instanceof Integer)) {
			throw new RuntimeException("キーはStringかiniしか使えません");
		}
	}

	/**
	 * 値をセットします
	 * @param KEY String int
	 * @param DATA 内容
	 */
	public void setDATA(Object KEY, ArrayNode DATA){
		KeyCheck(KEY);
		NODE_DATA.put(KEY, DATA);
		//System.out.println("SetArray: " + KEY + " -> " + NODE_DATA.get(KEY));
	}

	/**
	 * 値をセットします
	 * @param KEY String int
	 * @param DATA 内容
	 */
	public void setDATA(Object KEY, ArrayData DATA){
		KeyCheck(KEY);
		NODE_DATA.put(KEY, DATA);
		//System.out.println("SetDATA: " + KEY + " -> " + NODE_DATA.get(KEY));
	}

	/**
	 * 配列を取得します
	 * @param KEY String int
	 * @return 値
	 */
	public ArrayNode get(Object KEY){
		KeyCheck(KEY);
		if(NODE_DATA.get(KEY) instanceof ArrayNode){
			return (ArrayNode) NODE_DATA.get(KEY);
		} else {
			return null;
		}
	}

	public ArrayData getData(Object KEY) {
		KeyCheck(KEY);
		if(NODE_DATA.get(KEY) instanceof ArrayData){
			return (ArrayData) NODE_DATA.get(KEY);
		} else {
			return new ArrayData(null);
		}
	}

	/**
	 * ArrayListで取得します
	 * @return ObjectのArrayList
	 */
	public List<Object> asArrayList(){
		int I = 0;
		List<Object> ARRAYLIST = new ArrayList<>();

		//値が無くなるまで繰り返す
		while (true){
			if(NODE_DATA.get(I) != null){
				//配列に入れる
				ARRAYLIST.add(NODE_DATA.get(I));

				I++;
			} else {
				break;
			}
		}

		//結果を返す
		return ARRAYLIST;
	}

	public Object[] getKeyList() {
		List<Object> RESULT = new ArrayList<>();
		Set<Object> KEY_LIST = NODE_DATA.keySet();
		for (Object KEY:KEY_LIST) {
			RESULT.add(KEY);
		}
		return RESULT.toArray();
	}

	public int length() {
		return NODE_DATA.size();
	}
}

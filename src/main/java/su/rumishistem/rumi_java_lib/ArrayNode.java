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
	public void setDATA(Object KEY, Object DATA){
		KeyCheck(KEY);
		if(DATA instanceof ArrayNode){
			NODE_DATA.put(KEY, (ArrayNode) DATA);
		} else {
			NODE_DATA.put(KEY, (ArrayData) DATA);
		}
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
			return null;
		}
	}

	/**
	 * 指定したキーがNullかを取得します
 	 * @param KEY キー
	 * @return Nullならtrue
	 */
	public boolean isNull(Object KEY) {
		if(KEY instanceof String || KEY instanceof Integer){
			if (NODE_DATA.get(KEY) == null) {
				return true;
			} else {
				return false;
			}
		} else {
			throw new RuntimeException("キーはStringかiniしか使えません");
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
}

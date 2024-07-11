package com.rumisystem.rumi_java_lib;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.checkerframework.checker.units.qual.K;

public class ArrayNode {
	private HashMap<Object, Object> NODE_DATA = new HashMap<>();

	/**
	 * 値をセットします
	 * @param KEY String int
	 * @param DATA 内容
	 */
	public void setDATA(Object KEY, Object DATA){
		if(KEY instanceof String || KEY instanceof Integer){
			NODE_DATA.put(KEY, DATA);
		} else {
			throw new RuntimeException("キーはStringかiniしか使えません");
		}
	}

	/**
	 * 値を取得します
	 * @param KEY String int
	 * @return
	 */
	public ArrayNode get(Object KEY){
		if(KEY instanceof String || KEY instanceof Integer){
			if(NODE_DATA.get(KEY) instanceof ArrayNode){
				return (ArrayNode) NODE_DATA.get(KEY);
			} else {
				throw new RuntimeException("getは、型がArrayNodeの場合のみに使えます");
			}
		} else {
			throw new RuntimeException("キーはStringかiniしか使えません");
		}
	}

	public String asString(Object KEY){
		//キーはStringもしくはintか？
		if(KEY instanceof String || KEY instanceof Integer){
			//指定されたキーの値はStringかIntなら値を返す
			if(NODE_DATA.get(KEY) instanceof String || NODE_DATA.get(KEY) instanceof Integer){
				return NODE_DATA.get(KEY).toString();
			} else {
				throw new RuntimeException("型がStringもしくはIntではありません");
			}
		} else {
			throw new RuntimeException("キーはStringかiniしか使えません");
		}
	}

	public int asInt(Object KEY){
		//キーはStringもしくはintか？
		if(KEY instanceof String || KEY instanceof Integer){
			//指定されたキーの値はIntなら値を返す
			if(NODE_DATA.get(KEY) instanceof Integer){
				return (int) NODE_DATA.get(KEY);
			} else {
				//Intじゃない
				throw new RuntimeException("型がはIntではありません");
			}
		} else {
			throw new RuntimeException("キーはStringかiniしか使えません");
		}
	}

	public boolean asBool(Object KEY){
		//キーはStringもしくはintか？
		if(KEY instanceof String || KEY instanceof Integer){
			//指定されたキーの値はIntなら値を返す
			if(NODE_DATA.get(KEY) instanceof Boolean){
				return (boolean) NODE_DATA.get(KEY);
			} else {
				//Boolじゃない
				throw new RuntimeException("型がはBoolではありません");
			}
		} else {
			throw new RuntimeException("キーはStringかiniしか使えません");
		}
	}

	public Object asObject(Object KEY){
		//キーはStringもしくはintか？
		if(KEY instanceof String || KEY instanceof Integer){
			//そのまま返す
			return NODE_DATA.get(KEY);
		} else {
			throw new RuntimeException("キーはStringかiniしか使えません");
		}
	}

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
}

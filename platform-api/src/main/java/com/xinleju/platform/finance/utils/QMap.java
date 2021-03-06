package com.xinleju.platform.finance.utils;

import java.util.HashMap;

/**
 * QuickMap快速的Map;用来封装参数，返回JSON，方便操作
 * <p>
 * Map<String,Object>兼容的对象，主要字段 success-是否成功,message-提示信息,data-返回数据;
 * 
 * @author walden
 */
public class QMap extends HashMap<String, Object> {

	private static final long serialVersionUID = 8939360842599450277L;

	public static final String SUCCESS = "success";

	public static final String MESSAGE = "message";

	public static final String DATA = "data";

	public QMap() {
	}

	/**
	 * new QMap("key1",Obj1,"key2",Obj2,"key3",Obj3...)
	 * 
	 * @param params
	 */
	public QMap(Object... params) {
		settleParams(params);
	}

	/**
	 * 将变参以key-value的方式封装到map中
	 * 
	 * @param params
	 */
	private void settleParams(Object... params) {
		int length = params.length;
		if (length % 2 != 0) {
			--length;
		}
		for (int i = 0; i < length; i += 2) {
			this.put((String) params[i], params[i + 1]);
		}
	}

	/**
	 * <pre>
	 * QMap map = new QMap(&quot;key1&quot;, Obj1);
	 * map.add(&quot;key2&quot;, Obj2).add(&quot;key3&quot;, Obj4);
	 * </pre>
	 * 
	 * @param key
	 * @param value
	 * @return this
	 */
	public QMap add(String key, Object value) {
		this.put(key, value);
		return this;
	}

	/**
	 * <pre>
	 * QMap map = new QMap(&quot;key1&quot;, Obj1);
	 * map.add(&quot;key2&quot;, Obj2, &quot;key3&quot;, Obj4);
	 * </pre>
	 * 
	 * @param params
	 * @return
	 */
	public QMap add(Object... params) {
		settleParams(params);
		return this;
	}
}

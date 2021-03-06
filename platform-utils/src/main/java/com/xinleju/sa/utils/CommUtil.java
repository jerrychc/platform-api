/**   
 *
 * @version V1.0   
 */
package com.xinleju.sa.utils;

import java.io.UnsupportedEncodingException;
import java.util.Map;

/**
 * @author liuzy
 * 
 */
public class CommUtil {
	
	/**
	 * 判断参数为null/""/"null"/"  " [],
	 * 
	 * @param obj
	 * @return
	 */
	public static boolean isNull(Object obj) {
		boolean flag = true;
		if (obj != null) {
			String objs = obj.toString();
			if (!"null".equals(objs) && !"".equals(objs.trim())&&!"[]".equals(objs)) {
				flag = false;
			}
		}
		return flag;
	}

	/**
	 * 判断参数为null/""/"null"/"  "
	 * 
	 * @param obj
	 * @return
	 */
	public static boolean isNullOrEm(Object obj) {
		boolean flag = true;
		if (obj != null) {
			String objs = obj.toString();
			if (!"null".equals(objs) && !"".equals(objs.trim())) {
				flag = false;
			}
		}
		return flag;
	}
	
	
	/**
	 * 数组中是否含有指定元素
	 * @param arr
	 * @param targetValue
	 * @return
	 */
	public static boolean isContains(String[] arr, String targetValue) {
	    for (String s : arr) {
	        if (s.equals(targetValue)) {
	            return true;
	        }
	    }
	    return false;
	}
	
	/**
	 * 用于解决乱码问题
	 * 转换编码
	 * 
	 * @param str
	 * @param encode "UTF-8"
	 * @return
	 */
	public static String toCharacterEncoding(String str,String encode){
		if(str == null) return null;
		String retStr = str;
		byte b[];
		try{ 
			b = str.getBytes("ISO8859_1");
			for(int i = 0; i < b.length; i++) {
				byte b1 = b[i];
				if (b1 == 63)
					break;//1
				else if(b1 > 0)
					continue;//2
				else if (b1 < 0) { //不可能为0，0为字符串结束符 
					 //小于0乱码
					retStr = new String(b, encode); 
					break; 
					}
				}
			} catch (UnsupportedEncodingException e) {
				//  e.printStackTrace();
				}
			return retStr;
	}
	
	/**
	 * 根据销售状态和销售岗位返回颜色值
	 * 
	 * @param pk_sellstate
	 *            销售状态主键
	 * @param sellpost
	 *            销售岗位
	 * @param sellStatePkVOMap
	 *            销售状态vo map
	 * @return 颜色值
	 */
	public static String getColorValue(String pk_sellstate, String sellpost,
			Map<String, String> sellStatePkVOMap) {
		if (SafeObject.ifnull(pk_sellstate)) {
			return "#ffffff";
		}
		if (!SafeObject.ifnull(pk_sellstate) && "初始".equals(pk_sellstate)) {
			return "#c0c0c0";
		}
		if (!SafeObject.ifnull(pk_sellstate) && "未售".equals(pk_sellstate)) {
			return "#ffffff";
		}
		if (!SafeObject.ifnull(pk_sellstate) && "认购".equals(pk_sellstate)) {
			return "#ff0000";
		}
		if (!SafeObject.ifnull(pk_sellstate) && "签约".equals(pk_sellstate)) {
			return "#ff00ff";
		}
		if (!SafeObject.ifnull(pk_sellstate) && "保留".equals(pk_sellstate)) {
			return "#808080";
		}		
		return "#808080";
	}
}

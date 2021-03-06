package com.xinleju.platform.sys.org.utils;

import org.apache.commons.codec.digest.DigestUtils;



/**
 * 
 * 
 * @author yzp
 *
 */
public class EncryptionUtils {
	
	private static final String   PREFIX="XINJUKEJI";
	/**
	 * 获取盐值加密信息
	 * @param salt
	 * @param password
	 * @return
	 */
	public static String getEncryptInfo(String salt,String context){
		String result=DigestUtils.md5Hex(PREFIX+context+salt);
		return result;
	}
	public static void main(String[] args) {
		   try {
			   EncryptionUtils encryptionUtils = new EncryptionUtils();
			   String newPassword = encryptionUtils.getEncryptInfo("sys", "super123");
			   System.out.println(newPassword);
				} catch (Exception e) {
					//e.printStackTrace();
				} 

		   }
	
}

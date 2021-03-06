package com.xinleju.cloud.oa.util;

import org.bouncycastle.util.Strings;

public class Usual {
	/**
	 * 静态空字段,""
	 */
	public final static String mEmpty = "";

	public static Boolean f_isNullOrZeroBytes(byte[] bts) {
		if (bts == null || bts.length == 0) {
			return true;
		}
		return false;
	}

	public static String f_toBase64String(byte[] bts) {
		String str = Usual.mEmpty;
		if (Usual.f_isNullOrZeroBytes(bts)) {
			return str;
		}
		// BASE64Encoder encoder = new BASE64Encoder();
		try {
			byte[] mBytes_Base64 = org.bouncycastle.util.encoders.Base64
					.encode(bts);
			// str =Usual.f_fromBytes(bts);
			str = Strings.fromUTF8ByteArray(mBytes_Base64);
			// str = encoder.encode(bts);
		} catch (Exception e) {
			str = Usual.mEmpty;
		}
		// finally
		// {
		// encoder = null;
		// }
		return str;
	}

	/**
	 * Base64String数据格式String转化为byte[]数据
	 * 
	 * @param base64str
	 *            Base64String数据格式String
	 * @return byte[]数据
	 */
	public static byte[] f_fromBase64String(String base64str) {
		byte[] bts = new byte[0];
		if (Usual.f_isNullOrWhiteSpace(base64str)) {
			return bts;
		}
		// BASE64Decoder decoder = new BASE64Decoder();
		try {
			// bts = decoder.decodeBuffer(base64str);
			bts = org.bouncycastle.util.encoders.Base64.decode(base64str);
		} catch (Exception e) {
			bts = new byte[0];
		}
		// finally
		// {
		// decoder = null;
		// }
		return bts;
	}

	public static Boolean f_isNullOrWhiteSpace(String instr) {
		if (instr == null || instr.trim().length() == 0) {
			return true;
		}
		return false;
	}
}

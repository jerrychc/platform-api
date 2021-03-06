package com.xinleju.platform.utils;

import com.xinleju.platform.base.utils.MessageResult;
import org.apache.http.HttpStatus;

public class HttpCheckUtils {
	private HttpCheckUtils() {
	}

	public static boolean checkStatus(int httpState) {
		return httpState >= HttpStatus.SC_OK && httpState < HttpStatus.SC_MULTIPLE_CHOICES;
	}

	public static boolean checkDataSuccess(MessageResult result) {
		return result.getCode() == SyncErrorInfo.OK.getCode();
	}
}

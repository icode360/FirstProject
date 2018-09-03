package com.icode.firstproject.utils;

import android.util.Log;

/**
 * log信息输出工具
 */
public class LogUtils {
	
	private static final String TAG = "LogUtils";

	private static boolean sisOpenLogSwitch;

	public static void setOpenLogSwitch(boolean logSwitch) {
		sisOpenLogSwitch = logSwitch;
	}

	public static void d(String paramString) {
		d(TAG, paramString);
	}

	public static void d(Exception e) {
		d(TAG, e);
	}

	public static void d(String paramString1, Exception e) {
		d(paramString1, e.getMessage(), e);
	}

	public static void d(String paramString1, String paramString2) {
		d(paramString1, paramString2, null);
	}

	public static void d(String paramString1, String paramString2, Exception e) {
		if (sisOpenLogSwitch) {
			if (null != e) {
				Log.d(paramString1, paramString2, e);
			} else {
				Log.d(paramString1, paramString2);
			}
		}
	}

	public static void e(String paramString) {
		e(TAG, paramString);
	}

	public static void e(String paramString1, String paramString2) {
		if (sisOpenLogSwitch) {
			Log.e(paramString1, paramString2);
		}
	}

	public static void i(String paramString) {
		i(TAG, paramString);
	}

	public static void i(String paramString1, String paramString2) {
		if (sisOpenLogSwitch) {
			Log.i(paramString1, paramString2);
		}
	}

	public static void v(String paramString) {
		v(TAG, paramString);
	}

	public static void v(String paramString1, String paramString2) {
		if (sisOpenLogSwitch) {
			Log.v(paramString1, paramString2);
		}
	}

	public static void w(String paramString) {
		w(TAG, paramString);
	}

	public static void w(String paramString1, String paramString2) {
		if (sisOpenLogSwitch) {
			Log.w(paramString1, paramString2);
		}
		
	}
}

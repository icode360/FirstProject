package com.icode.firstproject.utils;

import android.content.res.Resources;

import java.util.regex.Pattern;

/**
 */
public class TextUtils {
    private static final String EMAIL_PATTERN = "^\\w+([-|\\.]\\w+)*@\\w+(\\.\\w+)+$";

    public static boolean isEmail(CharSequence text) {
        return text != null && Pattern.matches(EMAIL_PATTERN, text);
    }

    public static String getFormatString(Resources resources, int res, int value) {
        String result = "";
        try {
            result = resources.getString(res, value);
        } catch (Exception e) {
            e.printStackTrace();
            return getFormatString(resources, res, value + "");
        }
        return result;
    }

    public static String getFormatString(Resources resources, int res, String value) {
        String result = "";
        try {
            result = String.format(resources.getString(res), value + "");
        } catch (Exception e1) {
            e1.printStackTrace();
            result = resources.getString(res) + value;
            LogUtils.e("getFormatString : " + result);
        }
        return result;
    }
}

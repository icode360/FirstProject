package com.icode.firstproject.common.userstart;

import android.content.Context;
import android.content.SharedPreferences;

import com.icode.firstproject.MainApp;
import com.icode.firstproject.common.constant.ISharedPreferences;
import com.icode.firstproject.utils.SpUtils;


/**
 *
 */
public class UserStartManager {
	
	private static int sLastVersionCode;
	private static boolean sNewVersionFirstRun; // 新版本的第一次启动
	private static int sFirstRun; // 全新安装，第一次启动
	
	private static final int NEVER_RUN = 0; // 从来没运行过
	private static final int FIRST_RUN = 1; // 本次是第一次运行
	private static final int REPEATEDLY_RUN = 2; // 本次不是第一次运行

	/**
	 *
	 */
	private static void doSomeThingFirstRun(final Context context) {
        sFirstRun = getFirstRunFlagSharedPreferences(context); // 默认是NEVER_RUN
        if (sFirstRun == FIRST_RUN) { // 如果上一次保存的是首次运行
            saveFirstRunFlagSharedPreferences(context, REPEATEDLY_RUN); // 保存为这次不是首次运行
            sFirstRun = REPEATEDLY_RUN;
            return;
        } else if (sFirstRun == NEVER_RUN) {
            saveFirstRunFlagSharedPreferences(context, FIRST_RUN); // 这次是首次运行
            sFirstRun = FIRST_RUN;
            return;
        } // REPEATEDLY_RUN
    }

    static boolean isFirstRun() {
		return sFirstRun == FIRST_RUN;
	}
	
    static boolean isNewVersionFirstRun() {
		return sNewVersionFirstRun;
	}
	
    private static void setNewVersionFirstRun(boolean firstRun) {
		sNewVersionFirstRun = firstRun;
	}
	
	private static int getFirstRunFlagSharedPreferences(Context context) {
		SharedPreferences prefs = SpUtils.getSharedPreferences(ISharedPreferences.SP_USER_START);
		int fristRunFlag = prefs.getInt(ISharedPreferences.IS_FIRST_RUN, NEVER_RUN);
		return fristRunFlag;
	}
	
	private static void saveFirstRunFlagSharedPreferences(Context context, int firstRunFlag) {
		SharedPreferences prefs = SpUtils.getSharedPreferences(ISharedPreferences.SP_USER_START);
		prefs.edit().putInt(ISharedPreferences.IS_FIRST_RUN, firstRunFlag).commit();
		sFirstRun = firstRunFlag;
	}
	
    static void doSomeThing(Context context) {
		doSomeThingFirstRun(context);
		doSomeThingNewVersionFirstRun(context);
	}

	private static void doSomeThingNewVersionFirstRun(final Context context) {
        sLastVersionCode = getLastVersionCodeSharedPreferences(context); // 上一次运行的版本
        final int curVersionCode = MainApp.getVersionCode();
        if (sLastVersionCode > 0 && curVersionCode > sLastVersionCode) { // 如果不是首次运行, 并且这次运行的版本比上次运行的版本高
            setNewVersionFirstRun(true);
        } else {
            setNewVersionFirstRun(false);
        }

        saveLastVersionCodeSharedPreferences(context, curVersionCode); // 保存本次运行的版本
        if (curVersionCode > sLastVersionCode) { // 保存升级首次运行时间
            SharedPreferences prefs = SpUtils.getSharedPreferences(ISharedPreferences.SP_USER_START);
            prefs.edit().putLong(ISharedPreferences.APP_FRIST_RUN_AND_UPGRADE_TIME, System.currentTimeMillis()).commit();
        }
    }

    static long getFirstRunAndUpgradeTime(Context context) {
		SharedPreferences prefs = SpUtils.getSharedPreferences(ISharedPreferences.SP_USER_START);
		return prefs.getLong(ISharedPreferences.APP_FRIST_RUN_AND_UPGRADE_TIME, System.currentTimeMillis());
	}
 	
	private static int getLastVersionCodeSharedPreferences(Context context) {
		SharedPreferences prefs = SpUtils.getSharedPreferences(ISharedPreferences.SP_USER_START);
		int ver = prefs.getInt(ISharedPreferences.LAST_VERSION_CODE, 0);
		return ver;
	}
	
	private static void saveLastVersionCodeSharedPreferences(Context context, int versionCode) {
		SharedPreferences prefs = SpUtils.getSharedPreferences(ISharedPreferences.SP_USER_START);
		prefs.edit().putInt(ISharedPreferences.LAST_VERSION_CODE, versionCode).commit();
	}
	
    static int getLastVersionCode() {
		return sLastVersionCode;
	}
}

package com.icode.firstproject.common.userstart;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.icode.firstproject.utils.MachineUtils;


/**
 *
 */
public class UserStartManagerCompat {
    private static final boolean DEBUG = false;

    /**
     * 初始化数据, 每次启动的时候调用
     * 注意: 不要在Application类的onCreate中调用, 因为它无法准确认定进入情况, 要在所有的入口Activity中调用
     * TODO 由于StartHelperActivity被放在了服务进程, 暂时还没有想到对它的处理方法
     * @param context
     */
    public synchronized static void refreshState(Context context) {
        debug("refreshState " + MachineUtils.getCurrProcessName(context));
        SharedPreferences prefs = context.getSharedPreferences(
                UserStartManager2.INSTALL_VERSION_SP, Context.MODE_PRIVATE | Context.MODE_MULTI_PROCESS);

        if (!prefs.contains(UserStartManager2.INSTALL_VERSION_KEY)) { // 首次运行
            debug("2 is first run");
            UserStartManager.doSomeThing(context);
            if (!UserStartManager.isFirstRun()) { // 如果实际上不是首次运行, 是误判, 那我们认为这次是升级后的首次运行
                debug("1 is not first run");
                int lastVer = UserStartManager2.getCurrVersion(context) - 1; // 认为安装版本, 上一个版本, 上次运行的版本都是当前版本减一
                prefs.edit()
                        .putInt(UserStartManager2.INSTALL_VERSION_KEY, lastVer)
                        .putInt(UserStartManager2.LAST_VERSION_KEY, lastVer)
                        .putInt(UserStartManager2.LAST_RUN_VERSION_KEY + MachineUtils.getCurrProcessName(context), lastVer)
                        .commit();
            } else {
                debug("1 is first run");
            }
        } else {
            debug("2 is not first run");
        }

        UserStartManager2.refreshState(context);
        debug("is first run: " + UserStartManager2.isNewUserFirstRun(context));
        debug("is new version first run: " + UserStartManager2.isUpgradeFirstRun(context));

    }

    /**
     * 是否是新安装首次启动
     * @param context
     * @return
     */
    public static boolean isNewUserFirstRun(Context context) {
        return UserStartManager2.isNewUserFirstRun(context);
    }

    /**
     * 是否是升级后首次启动
     * @param context
     * @return
     */
    public static boolean isUpgradeFirstRun(Context context) {
        return UserStartManager2.isUpgradeFirstRun(context);
    }

    /**
     * 是否是升级用户
     * @param context
     * @return
     */
    public static boolean isUpgrade(Context context) {
        return UserStartManager2.isUpgrade(context);
    }

    /**
     * 是否是降级后首次启动
     * @param context
     * @return
     */
    public static boolean isDowngradeFirstRun(Context context) {
        return UserStartManager2.isDowngradeFirstRun(context);
    }

    /**
     * 获取上一个安装版本
     * @param context
     * @return 上一个安装版本号, 如果是首次安装返回0, 无法判断也返回0
     */
    public static int getLastVersionCode(Context context) {
        return UserStartManager2.getLastVersionCode(context);
    }

    /**
     * 获取安装或者升级后第一次运行的时间
     * @param context
     * @return
     */
    public static long getFirstRunAndUpgradeTime(Context context) {
        return UserStartManager2.getFirstRunAndUpgradeTime(context);
    }

    public static long getFirstRunTime(Context context) {
        return UserStartManager2.getFirstRunTime(context);
    }

    public static void debug(String log) {
        if (DEBUG) {
            Log.e("august", log);
        }
    }
}

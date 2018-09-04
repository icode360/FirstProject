package com.icode.firstproject.common.userstart;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;

import com.icode.firstproject.utils.MachineUtils;


/**
 * 启动管理
 */
public class UserStartManager {

    static final String INSTALL_VERSION_SP = "install_version_sp"; // sp名字
    static final String INSTALL_VERSION_KEY = "install_version_key"; // 安装版本号
    static final String LAST_VERSION_KEY = "last_version_key"; // 如果是升级用户, 上一个版本号
    static final String LAST_RUN_VERSION_KEY = "last_run_version_key"; // 上一次运行版本号, 用进程名做后缀
    static final String INSTALL_AND_UPGRADE_TIME = "install_and_upgrade_time_key"; // 安装后者升级后第一次运行的时间

    static final String FIRST_INSTALL_TIME = "first_install_time"; // 应用首次安装时间

    static int sInstallVersion; // 安装版本
    static int sLastVersion; // 如果是升级的, 上一个版本
    static int sLastRunVersion; // 上一次启动时的运行版本
    static long sInstallAndUpgradeTime; // 安装或升级后首次运行时间
    static long sFirstRunTime; // 第一次安装
    static int sCurrVersion; // 当前版本
    static boolean sHasRefresh; // 是否已经初始化过

    /**
     * 初始化数据
     * @param context
     */
    public synchronized static void refreshState(Context context) {
        SharedPreferences sp = context.getSharedPreferences(INSTALL_VERSION_SP, Context.MODE_MULTI_PROCESS);
        sInstallVersion = sp.getInt(INSTALL_VERSION_KEY, 0);
        sLastVersion = sp.getInt(LAST_VERSION_KEY, 0);
        sLastRunVersion = sp.getInt(LAST_RUN_VERSION_KEY + MachineUtils.getCurrProcessName(context), 0);
        sInstallAndUpgradeTime = sp.getLong(INSTALL_AND_UPGRADE_TIME, 0);

        sFirstRunTime = sp.getLong(FIRST_INSTALL_TIME, 0);

        sCurrVersion = getCurrVersion(context);
        if (sCurrVersion > 0) {
            SharedPreferences.Editor editor = sp.edit();
            if (sInstallVersion == 0) { // 首次安装
                sInstallVersion = sCurrVersion;
                sLastRunVersion = sLastVersion = 0;
                editor.putInt(INSTALL_VERSION_KEY, sInstallVersion)
                        .putLong(INSTALL_AND_UPGRADE_TIME, sInstallAndUpgradeTime = System.currentTimeMillis())
                        .putLong(FIRST_INSTALL_TIME, sFirstRunTime = System.currentTimeMillis());
            } else if (sLastRunVersion != sCurrVersion) { // 升级或降级
                sLastVersion = sLastRunVersion;
                editor.putInt(LAST_VERSION_KEY, sLastVersion)
                        .putLong(INSTALL_AND_UPGRADE_TIME, sInstallAndUpgradeTime = System.currentTimeMillis());
            }

            editor.putInt(LAST_RUN_VERSION_KEY + MachineUtils.getCurrProcessName(context), sCurrVersion).apply();

            sHasRefresh = true;
        }
    }

    /**
     * 是否是新安装首次启动
     * @param context
     * @return
     */
    public static boolean isNewUserFirstRun(Context context) {
        if (!sHasRefresh) {
            refreshState(context);
        }
        return sLastRunVersion == 0;
    }

    /**
     * 是否是升级用户
     * @param context
     * @return
     */
    public static boolean isUpgrade(Context context) {
        if (!sHasRefresh) {
            refreshState(context);
        }
        return sInstallVersion < sCurrVersion;
    }

    /**
     * 是否是升级后首次启动
     * @param context
     * @return
     */
    public static boolean isUpgradeFirstRun(Context context) {
        if (!sHasRefresh) {
            refreshState(context);
        }
        return sLastRunVersion > 0 && sLastRunVersion < sCurrVersion;
    }

    /**
     * 是否是降级后首次启动
     * @param context
     * @return
     */
    public static boolean isDowngradeFirstRun(Context context) {
        if (!sHasRefresh) {
            refreshState(context);
        }
        return sLastRunVersion > 0 && sLastRunVersion > sCurrVersion;
    }

    /**
     * 获取上一个安装版本
     * @param context
     * @return 上一个安装版本号, 如果是首次安装返回0
     */
    public static int getLastVersionCode(Context context) {
        if (!sHasRefresh) {
            refreshState(context);
        }
        return sLastVersion;
    }

    /**
     * 获取升级后第一次运行的时间, 不包括安装后第一次运行的时间
     * @param context
     * @return
     */
    public static long getFirstRunAndUpgradeTime(Context context) {
        if (!sHasRefresh) {
            refreshState(context);
        }
        return sInstallAndUpgradeTime;
    }

    /**
     * 获取首次安装运行时间
     * @param context
     * @return
     */
    public static long getFirstRunTime(Context context) {
        if (!sHasRefresh) {
            refreshState(context);
        }
        return sFirstRunTime;
    }

    /**
     * 获取当前版本
     * @param context
     * @return
     */
    static int getCurrVersion(Context context) {
        try {
            return context.getPackageManager().getPackageInfo(context.getPackageName(), 0).versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return 0;
    }
}

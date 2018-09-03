package com.icode.firstproject;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;

import com.icode.firstproject.base.BaseApp;
import com.icode.firstproject.common.userstart.UserStartManagerCompat;
import com.icode.firstproject.utils.AppUtils;


/**
 * 应用锁Application
 */
public class MainApp extends BaseApp {

    private static MainApp sInstance;

    public MainApp() {
        sInstance = this;
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
//        MultiDex.install(this);
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
        sInstance = null;
    }

    @Override
    public void startActivity(Intent intent) {
        try {
            super.startActivity(intent);
        } catch (Throwable e) {
            if (ActivityNotFoundException.class.isInstance(e)) {
                throw e;
            } else {
                e.printStackTrace();
            }
        }
    }

    /**
     * 获取Application实例
     * @return app 实例
     */
    public static MainApp getInstance() {
        return sInstance;
    }

    /**
     * 版本号
     * @return 版本号
     */
    public static int getVersionCode() {
        return AppUtils.getVersionCodeByPkgName(getInstance(), getInstance().getPackageName());
    }

    /**
     * 版本名
     * @return 版本名
     */
    public static String getVersionName() {
        return AppUtils.getVersionNameByPkgName(getInstance(), getInstance().getPackageName());
    }

    public static boolean isFirstRun() {
        return UserStartManagerCompat.isNewUserFirstRun(MainApp.getInstance());
    }

    public static boolean isNewVersionFirstRun() {
        return UserStartManagerCompat.isUpgradeFirstRun(MainApp.getInstance());
    }

    public static boolean isUpgrade() {
        return UserStartManagerCompat.isUpgrade(MainApp.getInstance());
    }
}

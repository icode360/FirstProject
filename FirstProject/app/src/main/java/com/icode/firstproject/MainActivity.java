package com.icode.firstproject;

import android.os.Bundle;

import com.icode.firstproject.base.BaseActivity;
import com.icode.firstproject.common.userstart.UserStartManager;
import com.icode.firstproject.utils.AppUtils;
import com.icode.firstproject.utils.LogUtils;

public class MainActivity extends BaseActivity {

    public static final String TAG = "icode";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setMainActivity(true);
        LogUtils.d(TAG, "上一个版本号= " + UserStartManager.getLastVersionCode(this));
        LogUtils.d(TAG, "当前版本号= " + AppUtils.getVersionCodeByPkgName(this, getApplicationContext().getPackageName()));
        LogUtils.d(TAG, "是否升级版本= " + UserStartManager.isUpgrade(this));
        LogUtils.d(TAG, "是否新安装版本首次运行= " + UserStartManager.isNewUserFirstRun(this));
        LogUtils.d(TAG, "是否升级版本首次运行= " + UserStartManager.isUpgradeFirstRun(this));
    }
}

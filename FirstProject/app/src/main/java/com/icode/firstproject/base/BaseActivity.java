package com.icode.firstproject.base;

import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.v4.view.ViewCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * 基础Activity
 */
public class BaseActivity extends AppCompatActivity {

    private boolean mIsMainActivity = false;

    private static List<BaseActivity> sList;

    static {
        if (sList == null) {
            sList = new ArrayList<BaseActivity>();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sList.add(this);
    }

    /**
     * 设置主Activity
     *
     * @param mIsMainActivity
     */
    public void setMainActivity(boolean mIsMainActivity) {
        this.mIsMainActivity = mIsMainActivity;
    }

    @Override
    protected void onDestroy() {
        sList.remove(this);
        if (mIsMainActivity) {
            if (null != sList && !sList.isEmpty()) {
                for (BaseActivity baseActivity : sList) {
                    baseActivity.finish();
                }
            }
            android.os.Process.killProcess(android.os.Process.myPid());
        }
        super.onDestroy();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void setContentView(@IdRes int layoutResID) {
        super.setContentView(layoutResID);
    }

    @Override
    public void setContentView(View view) {
        super.setContentView(view);
    }

    @Override
    public void setContentView(View view, ViewGroup.LayoutParams params) {
        super.setContentView(view, params);
    }


    /**
     * 设置状态栏为全透明
     */
    protected void setStatusBarTranparent() {
        setStatusBarColor(1);
    }

    /**
     * 设置状态栏为半透明
     */
    protected void setStatusBarTranslucent() {
        setStatusBarColor(2);
    }

    /**
     * 设置状态栏颜色
     *
     * @param type 1代表全透明，2代表半透明
     */
    private void setStatusBarColor(int type) {
        if (type != 1 && type != 2) {
            return;
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            //添加Flag把状态栏设为可绘制模式
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            if (type == 1) {
                //如果为全透明模式，取消设置Window半透明的Flag
                window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
                //设置状态栏为透明
                window.setStatusBarColor(Color.TRANSPARENT);
                //设置window的状态栏不可见
                window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
            } else {
                //如果为半透明模式，添加设置Window半透明的Flag
                window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
                //设置系统状态栏处于可见状态
                window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_VISIBLE);
            }
            //view不根据系统窗口来调整自己的布局
            ViewGroup mContentView = (ViewGroup) window.findViewById(Window.ID_ANDROID_CONTENT);
            View mChildView = mContentView.getChildAt(0);
            if (mChildView != null) {
                ViewCompat.setFitsSystemWindows(mChildView, false);
                ViewCompat.requestApplyInsets(mChildView);
            }
        }
    }

    /**
     * 弹出输入法
     *
     * @param textView
     */
    public void showInputMethod(TextView textView) {
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.showSoftInput(textView, InputMethodManager.SHOW_IMPLICIT);
    }

    /**
     * 隐藏输入法
     *
     * @param textView
     */
    public void hideInputMethod(TextView textView) {
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm.isActive()) {
            imm.hideSoftInputFromWindow(textView.getApplicationWindowToken(), 0);
        }
    }
}

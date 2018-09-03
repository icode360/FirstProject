package com.icode.firstproject.utils;


import android.accounts.Account;
import android.accounts.AccountManager;
import android.app.ActivityManager;
import android.bluetooth.BluetoothAdapter;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.content.res.Resources;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.PowerManager;
import android.telephony.TelephonyManager;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.WindowManager;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import static android.os.Build.VERSION_CODES.FROYO;
import static android.os.Build.VERSION_CODES.GINGERBREAD;
import static android.os.Build.VERSION_CODES.HONEYCOMB;
import static android.os.Build.VERSION_CODES.ICE_CREAM_SANDWICH;
import static android.os.Build.VERSION_CODES.JELLY_BEAN;
import static android.os.Build.VERSION_CODES.JELLY_BEAN_MR2;
import static android.os.Build.VERSION_CODES.KITKAT;
import static android.os.Build.VERSION_CODES.LOLLIPOP;

/**
 * 和手机信息相关的接口
 */
public class MachineUtils {

    private static int sScreenWidth = 0;
    private static int sScreenHeight = 0;

    public static final int SDK_VERSION_CODE_5_1_1 = 22;
    // 6.0
    public static final int SDK_VERSION_CODE_6 = 23;

    // SDK 版本判断
    public static final int SDK_VERSION = Build.VERSION.SDK_INT;
    /**
     * SDK >= 8
     */
    public static final boolean HAS_SDK_FROYO = SDK_VERSION >= FROYO;
    /**
     * SDK >= 9
     */
    public static final boolean HAS_SDK_GINGERBREAD = SDK_VERSION >= GINGERBREAD;
    /**
     * SDK >= 11
     */
    public static final boolean HAS_SDK_HONEYCOMB = SDK_VERSION >= HONEYCOMB;
    /**
     * SDK >= 14
     */
    public static final boolean HAS_SDK_ICS = SDK_VERSION >= ICE_CREAM_SANDWICH;
    /**
     * SDK >= 16
     */
    public static final boolean HAS_SDK_JELLY_BEAN = SDK_VERSION >= JELLY_BEAN;
    /**
     * SDK >= 18
     */
    public static final boolean HAS_SDK_JELLY_BEAN_MR2 = SDK_VERSION >= JELLY_BEAN_MR2;

    /**
     * SDK >= 19
     */
    public static final boolean HAS_SDK_KITKAT = SDK_VERSION >= KITKAT;
    /**
     * SDK >= 21 android版本是否为5.0以上
     */
    public static final boolean HAS_SDK_LOLLIPOP = SDK_VERSION >= LOLLIPOP;
    /**
     * SDK >= 22 android版本是否为5.1(5.1.1)以上
     */
    public static final boolean HAS_SDK_5_1_1 = SDK_VERSION >= SDK_VERSION_CODE_5_1_1;
    /**
     * SDK >= 23 android版本是否为6.0以上
     */
    public static final boolean HAS_SDK_6 = SDK_VERSION >= SDK_VERSION_CODE_6;
    /**
     * SDK < 19
     */
    public static boolean sSDK_UNDER_KITKAT = SDK_VERSION < KITKAT;
    /**
     * SDK < 20
     */
    public static boolean sSDK_UNDER_KITKAT_WATCH = SDK_VERSION < Build.VERSION_CODES.KITKAT_WATCH;

    /**
     * SDK < 21
     */
    public static boolean sSDK_UNDER_LOLIP = SDK_VERSION < 21;

    // 红米2
    private final static String[] HONGMI_2 = {"2014811"};

    /**
     * 是否屏亮
     *
     * @param context
     * @return
     */
    public static boolean isScreenOn(Context context) {
        PowerManager pm = (PowerManager) context.getSystemService(Context.POWER_SERVICE);
        return pm.isScreenOn();
    }

    /**
     * 获取当前的语言
     *
     * @param context
     * @return
     * @author zhoujun
     */
    public static String getLanguage(Context context) {
        String language = Locale.getDefault().getLanguage().toLowerCase();
        // 标准返回是2位
        if (language.length() != 2) {
            language = context.getResources().getConfiguration().locale.getLanguage().toLowerCase();
        }
        if (language.length() > 2) {
            language = language.substring(0, 2);
        }
        return language;
    }

    /**
     * 获取当前的国家
     *
     * @param context
     * @return
     * @author zhoujun
     */
    public static String getCountry(Context context) {
        String country = Locale.getDefault().getCountry();
        // 标准返回是2位
        if (country.length() != 2) {
            country = context.getResources().getConfiguration().locale.getCountry().toUpperCase();
        }
        if (country.length() > 2) {
            country = country.substring(0, 2);
        }
        return country;
    }

    /**
     * 获取当前的网络类型
     *
     * @param context
     * @return
     */
    public static String getNetWorkType(Context context) {
        ConnectivityManager manager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = manager.getActiveNetworkInfo();
        String netkWorkType = "unknown";
        if (networkInfo != null && networkInfo.isConnected()) {
            String type = networkInfo.getTypeName();
            if (type.equalsIgnoreCase("WIFI")) {
                netkWorkType = "wifi";
            } else if (type.equalsIgnoreCase("MOBILE")) {
                TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
                switch (telephonyManager.getNetworkType()) {
                    case TelephonyManager.NETWORK_TYPE_1xRTT:
                        netkWorkType = "2g";
                        break;
                    case TelephonyManager.NETWORK_TYPE_CDMA:
                        netkWorkType = "2g";
                        break;
                    case TelephonyManager.NETWORK_TYPE_EDGE:
                        netkWorkType = "2g";
                        break;
                    case TelephonyManager.NETWORK_TYPE_EVDO_0:
                        netkWorkType = "3g";
                        break;
                    case TelephonyManager.NETWORK_TYPE_EVDO_A:
                        netkWorkType = "3g";
                        break;
                    case TelephonyManager.NETWORK_TYPE_GPRS:
                        netkWorkType = "gprs";
                        break;
                    case TelephonyManager.NETWORK_TYPE_HSDPA:
                        netkWorkType = "3g";
                        break;
                    case TelephonyManager.NETWORK_TYPE_HSPA:
                        netkWorkType = "3g";
                        break;
                    case TelephonyManager.NETWORK_TYPE_HSUPA:
                        netkWorkType = "3g";
                        break;
                    case TelephonyManager.NETWORK_TYPE_UMTS:
                        netkWorkType = "3g";
                        break;
                    case TelephonyManager.NETWORK_TYPE_EHRPD:
                        netkWorkType = "3g";
                        break;
                    case TelephonyManager.NETWORK_TYPE_EVDO_B:
                        netkWorkType = "3g";
                        break;
                    case TelephonyManager.NETWORK_TYPE_HSPAP:
                        netkWorkType = "3g";
                        break;
                    case TelephonyManager.NETWORK_TYPE_IDEN:
                        netkWorkType = "2g";
                        break;
                    case TelephonyManager.NETWORK_TYPE_LTE:
                        netkWorkType = "4g";
                        break;
                    case TelephonyManager.NETWORK_TYPE_UNKNOWN:
                        netkWorkType = "2g";
                        break;
                    default:
                        netkWorkType = "unknown";
                        break;
                }
            }
        }

        return netkWorkType;
    }

    /**
     * 获取当前进程名
     *
     * @param context
     * @return
     */
    public static String getCurrProcessName(Context context) {
        try {
            final int currProcessId = android.os.Process.myPid();
            final ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
            List<ActivityManager.RunningAppProcessInfo> processInfos = am.getRunningAppProcesses();
            if (processInfos != null) {
                for (ActivityManager.RunningAppProcessInfo info : processInfos) {
                    if (info.pid == currProcessId) {
                        return info.processName;
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    //sim卡是否可读
    public static boolean isCanUseSim(Context context) {
        try {
            TelephonyManager mgr = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
            return TelephonyManager.SIM_STATE_READY == mgr.getSimState();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }


    /**
     * 获取gmail
     *
     * @param context
     * @return
     */
    public static String getGmail(Context context) {
        AccountManager accountManager = AccountManager.get(context);
        Account[] accounts = accountManager.getAccountsByType("com.google"); //获取google账户
        Account account = accounts.length > 0 ? accounts[0] : null; //取第一个账户
        return account == null ? null : account.name;
    }

    public static List<String> getRecommendMail(Context context) {
        List<String> list = new ArrayList<String>();
        AccountManager accountManager = AccountManager.get(context);
        Account[] accounts = accountManager.getAccountsByType("com.google"); //获取google账户
        if (accounts.length > 0) {
            for (Account account : accounts) {
                if (null != account && TextUtils.isEmail(account.name)) {
                    list.add(account.name);
                }
            }
        }
        Account[] accountsAll = accountManager.getAccounts();
        if (accountsAll.length > 0) {
            for (Account account : accountsAll) {
                if (null != account && !list.contains(account.name) && TextUtils.isEmail(account.name)) {
                    list.add(account.name);
                }
            }
        }
        return list;
    }

    /**
     * 展开通知栏
     *
     * @param context
     */
    @SuppressWarnings("ResourceType")
    public static void expandNotificationsPanel(Context context) {
        try {
            Object statusbar = context.getSystemService("statusbar");
            Class statusBarManager = Class.forName("android.app.StatusBarManager");
            Method expandMethod = statusBarManager.getMethod("expandNotificationsPanel");
            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN_MR1) {        //4.2以下
                expandMethod = statusBarManager.getMethod("expand");
            }
            expandMethod.invoke(statusbar);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 合上通知栏
     *
     * @param context
     */
    @SuppressWarnings("ResourceType")
    public static void collapsePanels(Context context) {
        try {
            Object statusbar = context.getSystemService("statusbar");
            Class statusBarManager = Class.forName("android.app.StatusBarManager");
            Method collapseMethod = statusBarManager.getMethod("collapsePanels");
            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN_MR1) {        //4.2以下
                collapseMethod = statusBarManager.getMethod("collapse");
            }
            collapseMethod.invoke(statusbar);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * @param context
     * @return
     */
    public static String getUserLocation(Context context) {
        String result = getLocation(context);

        if (result.contains("CN") || result.contains("cn")) {
            result = "cn";
        } else if (result.contains("IN") || result.contains("in")) {
            result = "in";
        } else if (result.contains("US") || result.contains("us")) {
            result = "us";
        } else if (result.contains("PH") || result.contains("ph")) {
            result = "ph";
        } else if (result.contains("ID") || result.contains("id")) {
            result = "id";
        } else if (result.contains("BR") || result.contains("br")) {
            result = "br";
        } else if (result.contains("RU") || result.contains("ru")) {
            result = "ru";
        } else if (result.contains("MX") || result.contains("mx")) {
            result = "mx";
        } else if (result.contains("TR") || result.contains("tr")) {
            result = "tr";
        } else if (result.contains("IR") || result.contains("ir")) {
            result = "ir";
        } else if (result.contains("MY") || result.contains("my")) {
            result = "my";
        } else if (result.contains("PK") || result.contains("pk")) {
            result = "pk";
        } else if (result.contains("EG") || result.contains("eg")) {
            result = "eg";
        } else if (result.equalsIgnoreCase("es-AR")
                || result.equalsIgnoreCase("ar")) {
            result = "ar";
        } else if (result.equalsIgnoreCase("es")) {
            result = "es";
        } else if (result.contains("MA") || result.contains("ma")) {
            result = "ma";
        } else if (result.contains("th") || result.contains("TH")) {
            result = "th";
        } else if (result.contains("GB") || result.contains("gb")) {
            result = "gb";
        } else if (result.contains("ro") || result.contains("RO")) {
            result = "ro";
        } else if (result.contains("ng") || result.contains("NG")) {
            result = "ng";
        } else if (result.contains("bd") || result.contains("BD")) {
            result = "bd";
        } else {
            result = "cn";
        }

        return result;
    }

    /**
     * @param context
     * @return
     */
    public static String getLocation(Context context) {
        String result = null;
        if (context != null) {
            TelephonyManager manager = (TelephonyManager) context
                    .getSystemService(Context.TELEPHONY_SERVICE);
            if (manager.getSimState() == TelephonyManager.SIM_STATE_READY) {
                String simOperator = manager.getSimOperator();
                String simCountry = manager.getSimCountryIso();
                if (!android.text.TextUtils.isEmpty(simOperator)) {
                    if (simOperator.startsWith("404")
                            || simOperator.startsWith("405")) {
                        result = "in";
                    } else if (simOperator.startsWith("310")) {
                        result = "us";
                    } else if (simOperator.startsWith("4600")) {
                        result = "cn";
                    } else if (simOperator.startsWith("515")) {
                        result = "ph";
                    } else if (simOperator.startsWith("510")) {
                        result = "id";
                    } else if (simOperator.startsWith("724")) {
                        result = "br";
                    } else if (simOperator.startsWith("250")) {
                        result = "ru";
                    } else if (simOperator.startsWith("334")) {
                        result = "mx";
                    } else if (simOperator.startsWith("286")) {
                        result = "tr";
                    } else if (simOperator.startsWith("432")) {
                        result = "ir";
                    } else if (simOperator.startsWith("502")) {
                        result = "my";
                    } else if (simOperator.startsWith("410")) {
                        result = "pk";
                    } else if (simOperator.startsWith("602")) {
                        result = "eg";
                    } else if (simOperator.startsWith("470")) {
                        result = "bd";
                    } else if (simOperator.startsWith("722")) {
                        result = "ar";
                    } else if (simOperator.startsWith("214")) {
                        result = "es";
                    } else if (simOperator.startsWith("604")) {
                        result = "ma";
                    } else if (simOperator.startsWith("520")) {
                        result = "th";
                    } else if (simOperator.startsWith("621")) {
                        result = "ng";
                    } else if (simOperator.startsWith("234")) {
                        result = "gb";
                    } else if (simOperator.startsWith("226")) {
                        result = "ro";
                    }
                }

                if (android.text.TextUtils.isEmpty(result) && !android.text.TextUtils.isEmpty(simCountry)) {
                    result = simCountry;
                }
            }

            if (android.text.TextUtils.isEmpty(result)) {
                String curCountry = Locale.getDefault().getCountry();
                if (!android.text.TextUtils.isEmpty(curCountry)) {
                    result = curCountry;
                }
            }
        }
        if (android.text.TextUtils.isEmpty(result)) {
            return "unknow";
        }
        return result;
    }

    /**
     * 隐藏应用程序图标
     *
     * @param context
     * @return
     */
    public static boolean hideLauncherIcon(Context context) {
        ComponentName component = null;
        PackageManager packageManager = context.getPackageManager();
        Intent intent = packageManager.getLaunchIntentForPackage(context.getPackageName());
        if (intent != null) {
            component = intent.getComponent();
        }

        if (null == component) {
            if (null == intent) {
                intent = new Intent(Intent.ACTION_MAIN);
                intent.setPackage(context.getPackageName());
                intent.addCategory(Intent.CATEGORY_LAUNCHER);
            }
            List<ResolveInfo> resolveInfos = packageManager.queryIntentActivities(intent, 0);
            if (null != resolveInfos && !resolveInfos.isEmpty()) {
                ResolveInfo resolveInfo = resolveInfos.get(0);
                if (null != resolveInfo && resolveInfo.activityInfo != null) {
                    component = new ComponentName(resolveInfo.activityInfo.packageName, resolveInfo.activityInfo.name);
                }
            }
        }


        if (null != component) {
            packageManager.setComponentEnabledSetting(component, PackageManager.COMPONENT_ENABLED_STATE_DISABLED, PackageManager.DONT_KILL_APP);
            return true;
        }
        return false;
    }

    public static boolean showLauncherIcon(Context context, Class<?> defaultActivity) {
        ComponentName component = null;
        PackageManager packageManager = context.getPackageManager();
        Intent intent = packageManager.getLaunchIntentForPackage(context.getPackageName());
        if (intent != null) {
            component = intent.getComponent();
        }

        if (null == component) {
            if (null == intent) {
                intent = new Intent(Intent.ACTION_MAIN);
                intent.setPackage(context.getPackageName());
                intent.addCategory(Intent.CATEGORY_LAUNCHER);
            }
            List<ResolveInfo> resolveInfos = packageManager.queryIntentActivities(intent, 0);
            if (null != resolveInfos && !resolveInfos.isEmpty()) {
                ResolveInfo resolveInfo = resolveInfos.get(0);
                if (null != resolveInfo && resolveInfo.activityInfo != null) {
                    component = new ComponentName(resolveInfo.activityInfo.packageName, resolveInfo.activityInfo.name);
                }
            }
        }

        if (null == component) {
            component = new ComponentName(context, defaultActivity);
            Log.e("august", "can not find component, set it as StartActivity");
        }

        if (null != component) {
            packageManager.setComponentEnabledSetting(component, PackageManager.COMPONENT_ENABLED_STATE_ENABLED, PackageManager.DONT_KILL_APP);
            return true;
        }
        return false;
    }

    public static boolean isApkDebugable(Context context) {
        try {
            ApplicationInfo info = context.getApplicationInfo();
            return (info.flags & ApplicationInfo.FLAG_DEBUGGABLE) != 0;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    // 获得总内存
    public static long getTotalMem() {
        long mTotal;
        // /proc/meminfo读出的内核信息进行解释
        String path = "/proc/meminfo";
        String content = null;
        BufferedReader br = null;
        try {
            br = new BufferedReader(new FileReader(path), 8);
            String line;
            if ((line = br.readLine()) != null) {
                content = line;
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (br != null) {
                try {
                    br.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        // beginIndex
        int begin = content.indexOf(':');
        // endIndex
        int end = content.indexOf('k');
        // 截取字符串信息

        content = content.substring(begin + 1, end).trim();
        mTotal = Integer.parseInt(content);
        return mTotal;
    }

    // 获得可用的内存
    public static long getAvailableMemory(Context mContext) {
        long usedMemory;
        ActivityManager am = (ActivityManager) mContext.getSystemService(Context.ACTIVITY_SERVICE);
        ActivityManager.MemoryInfo mi = new ActivityManager.MemoryInfo();
        am.getMemoryInfo(mi);

        // 取得剩余的内存空间
        usedMemory = mi.availMem / 1024;
        return usedMemory;
    }

    // 获得总内存
    public static long getTotalMem(Context context) {
        long mTotal = 0;
        // /proc/meminfo读出的内核信息进行解释
        String path = "/proc/meminfo";
        String content = null;
        BufferedReader br = null;
        try {
            br = new BufferedReader(new FileReader(path), 8);
            String line;
            if ((line = br.readLine()) != null) {
                content = line;
            }
            // beginIndex
            int begin = content.indexOf(':');
            // endIndex
            int end = content.indexOf('k');
            // 截取字符串信息

            content = content.substring(begin + 1, end).trim();
            mTotal = Integer.parseInt(content);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (NullPointerException e) {
            if (0 == mTotal) {
                mTotal = 2 * getAvailableMemory(context);
            }
            return mTotal;
        } finally {
            if (br != null) {
                try {
                    br.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        return mTotal;
    }

    public static int getStatusBarHeight() {
        Resources resources = Resources.getSystem();
        int resourceId = Resources.getSystem().getIdentifier(
                "status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            return resources.getDimensionPixelSize(resourceId);
        }
        return 0;
    }

    public static int getStatusBarH(Context context) {
        int sStatusBarHeight = 0;
        Class<?> c = null;
        Object obj = null;
        Field field = null;
        int x = 0;
        try {
            c = Class.forName("com.android.internal.R$dimen");
            obj = c.newInstance();
            field = c.getField("status_bar_height");
            x = Integer.parseInt(field.get(obj).toString());
            sStatusBarHeight = context.getResources().getDimensionPixelSize(x);
        } catch (Exception e1) {
            e1.printStackTrace();
        }
        return sStatusBarHeight;
    }

    public static int getScreenWidth(Context context) {
        if (sScreenWidth == 0 && null != context) {
            DisplayMetrics dm = new DisplayMetrics();
            WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
            wm.getDefaultDisplay().getMetrics(dm);
            sScreenWidth = dm.widthPixels;
        }
        return sScreenWidth;
    }

    public static int getScreenHeight(Context context) {
        if (sScreenHeight == 0 && null != context) {
            DisplayMetrics dm = new DisplayMetrics();
            WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
            wm.getDefaultDisplay().getMetrics(dm);
            sScreenHeight = dm.heightPixels;
        }
        return sScreenHeight;
    }

    /**
     * 是否小米4<br>
     *
     * @return
     */
    public static boolean isMi4() {
        return "MI 4LTE".equalsIgnoreCase(Build.MODEL);
    }

    public static boolean isHONGMI2() {
        return isModel(HONGMI_2);
    }

    public static boolean isModel(String[] models) {
        final String board = android.os.Build.MODEL;
        if (board == null) {
            return false;
        }
        final int size = models.length;
        try {
            for (int i = 0; i < size; i++) {
                if (board.equals(models[i])
                        || board.equals(models[i].toLowerCase())
                        || board.equals(models[i].toUpperCase())) {
                    return true;
                }
            }
        } catch (Exception e) {
        }
        return false;
    }

    public static boolean isWifiEnable(Context context) {
        try {
            WifiManager wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
            switch (wifiManager.getWifiState()) {
                case WifiManager.WIFI_STATE_ENABLED:
                case WifiManager.WIFI_STATE_ENABLING:
                    return true;
                case WifiManager.WIFI_STATE_DISABLED:
                case WifiManager.WIFI_STATE_DISABLING:
                    return false;
            }
            return wifiManager.isWifiEnabled();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public static final String WIFI_STATE_CHANGED_ACTION = "action.wifi_state_changed";

    public static void setWifiEnable(final Context context, final boolean enable) {
        try {
            WifiManager wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
            boolean isSuccess = wifiManager.setWifiEnabled(enable);
            if (isSuccess) {
                new Thread(){

                    @Override
                    public void run() {
                        try {
                            sleep(200);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }

                        try {
                            if (enable != isWifiEnable(context)) {
                                context.sendBroadcast(new Intent(WIFI_STATE_CHANGED_ACTION));
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }.start();
                LogUtils.d("setWifiEnable :" + enable);
            } else {
                LogUtils.d("setWifiEnable失败");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 获取移动数据状态
     *
     * @param mContext
     * @return
     */
    public static boolean getMobileDataEnabled(Context mContext) {
        try {
            ConnectivityManager connectivityManager = (ConnectivityManager) mContext.getSystemService(Context.CONNECTIVITY_SERVICE);
            Method getMobileDataEnabledMethod = ConnectivityManager.class.getMethod("getMobileDataEnabled");
            return (Boolean) getMobileDataEnabledMethod.invoke(connectivityManager);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * 设置移动数据状态
     *
     * @param mContext
     * @param enable
     */
    public static void setMobileDataEnabled(Context mContext, boolean enable) {
        try {
            ConnectivityManager connectivityManager = (ConnectivityManager) mContext.getSystemService(Context.CONNECTIVITY_SERVICE);
            Method setMobileDataEnabled = ConnectivityManager.class.getMethod("setMobileDataEnabled", boolean.class);
            setMobileDataEnabled.invoke(connectivityManager, enable);
            LogUtils.d("setMobileDataEnabled :" + enable);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void setBluetoothEnabled(boolean mIsOpenBluetooth) {
        try {
            if (mIsOpenBluetooth) {
                BluetoothAdapter.getDefaultAdapter().enable();
            } else {
                BluetoothAdapter.getDefaultAdapter().disable();
            }
            LogUtils.d("setMobileDataEnabled :" + mIsOpenBluetooth);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static boolean getBluetoothEnabled() {
        try {
            return BluetoothAdapter.getDefaultAdapter() != null ? BluetoothAdapter.getDefaultAdapter().isEnabled() : false;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public static int getBluetoothState() {
        return BluetoothAdapter.getDefaultAdapter() != null ? BluetoothAdapter.getDefaultAdapter().getState() : 1;
    }

    /**
     * 是否是三星的手机
     * @return
     */
    public static boolean isSamsungDevice() {
        return Build.BRAND != null &&
                Build.MANUFACTURER != null ?
                Build.BRAND.compareToIgnoreCase("Samsung") == 0 || Build.MANUFACTURER.compareToIgnoreCase("Samsung") == 0
                : false;
    }

    public static boolean isMiui() {
        return Build.MANUFACTURER.toLowerCase().contains("xiaomi");
    }

    public static boolean isMeizu() {
        return Build.BRAND.toLowerCase().contains("meizu") && Build.VERSION.SDK_INT >= 14;
    }

    // 根据系统版本号判断时候为华为2.2 or 2.2.1, Y 则catch
    public static boolean isHuawei() {
        boolean result = false;
        String androidVersion = Build.VERSION.RELEASE;// os版本号
        String brand = Build.BRAND;// 商标
        if (androidVersion == null || brand == null) {
            return result;
        }
        if (brand.equalsIgnoreCase("Huawei")) {
            result = true;
        }
        return result;
    }

    public static String getMiuiVer() {
        String ver = getSystemProperty("ro.miui.ui.version.name");
        return ver;
    }

    public static String getSystemProperty(String propName) {
        String line;
        BufferedReader input = null;
        try {
            Process p = Runtime.getRuntime().exec("getprop " + propName);
            input = new BufferedReader(
                    new InputStreamReader(p.getInputStream()), 1024);
            line = input.readLine();
            input.close();
        } catch (IOException ex) {
//            Log.e(TAG, "Unable to read sysprop " + propName, ex);
            return null;
        } finally {
            if (input != null) {
                try {
                    input.close();
                } catch (IOException e) {
//                    Log.e(TAG, "Exception while closing InputStream", e);
                }
            }
        }
        return line;
    }
}

package com.icode.firstproject.utils;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.KeyguardManager;
import android.bluetooth.BluetoothAdapter;
import android.content.ActivityNotFoundException;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.util.Log;

import java.io.File;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * 和应用程序相关的接口
 */
public class AppUtils {

	public static Set<String> sNotALauncher = new HashSet<String>(
			Arrays.asList(""));

	/**
	 * 获取在功能菜单出现的程序列表
	 *
	 * @param context
	 *            上下文
	 * @return 程序列表，类型是 List<ResolveInfo>
	 */
	public static List<ResolveInfo> getLauncherApps(Context context) {
		List<ResolveInfo> infos = null;
		PackageManager packageMgr = context.getPackageManager();
		Intent intent = new Intent(Intent.ACTION_MAIN);
		intent.addCategory(Intent.CATEGORY_LAUNCHER);
		try {
			infos = packageMgr.queryIntentActivities(intent, 0);
		} catch (OutOfMemoryError e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}

		packageMgr = null;
		return infos;
	}

	/**
	 * 获取默认运行桌面包名（注：存在多个桌面时且未指定默认桌面时，该方法返回Null,使用时需处理这个情况）
	 * @param context
	 * @return
	 */
	public static String getDefaultLauncher(Context context) {
		final Intent intent = new Intent(Intent.ACTION_MAIN);
		intent.addCategory(Intent.CATEGORY_HOME);
		final ResolveInfo res = context.getPackageManager().resolveActivity(intent, PackageManager.MATCH_DEFAULT_ONLY);
		if (res == null || res.activityInfo == null) {
			// should not happen. A home is always installed, isn't it?
			return null;
		}
		if (res.activityInfo.packageName.equals("android")) {
			// 有多个桌面程序存在，且未指定默认项时；
			return null;
		} else {
			return res.activityInfo.packageName;
		}
	}

	/**
	 * 根据包名启动应用
	 * @param context
	 * @param pkg
	 */
	public static void safeStartPackage(final Context context, String pkg) {
		if (context == null || pkg == null) {
			return;
		}
		PackageManager packageManager = context.getPackageManager();
		Intent intent = packageManager.getLaunchIntentForPackage(pkg);
		if (intent != null) {
			safeStartActivity(context, intent);
		}
	}

	/**
	 * 启动程序
	 *
	 * @param context
	 * @param action
	 */
	public static final void safeStartAction(final Context context, String action) {
		Intent intent = new Intent();
		intent.setAction(action);
		intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK + Intent.FLAG_ACTIVITY_SINGLE_TOP);
		try {
			context.startActivity(intent);
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	/**
	 * 安全启动activity，捕获异常
	 *
	 * @param context
	 * @param intent
	 *            启动的intent
	 * @return
	 */
	public static void safeStartActivity(final Context context, Intent intent) {
		try {
			if (context != null) {
				context.startActivity(intent);
			}
		} catch (Exception e) {
		}
	}

	/**
	 * safeStartPackage
	 * @param context
	 * @param pkgName
	 * @return
	 */
	public static Drawable getAppIcon(Context context, String pkgName) {
		try {
			PackageManager packageMgr = context.getPackageManager();
			ApplicationInfo applicationInfo = packageMgr.getApplicationInfo(pkgName, 0);
			return applicationInfo.loadIcon(packageMgr);
		} catch (Exception e) {
			e.printStackTrace();
			return context.getResources().getDrawable(android.R.drawable.sym_def_app_icon);
		}
	}

	/**
	 * 获取应用程序的名称
	 * @param context
	 * @param pkgName
	 * @return
	 */
	public static String getAppName(Context context, String pkgName) {
		try {
			PackageManager packageMgr = context.getPackageManager();
			ApplicationInfo applicationInfo = packageMgr.getApplicationInfo(pkgName, 0);
			return applicationInfo.loadLabel(packageMgr).toString();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "";
	}

	/**
	 * 获取栈顶应用程序
	 * @param context
	 * @return
	 */
	public static ComponentName getTopPackageName(Context context) {
//		try {
//			return ProcessUtils.getLauncherTopApp(context);
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
		return null;
	}

    /**
     * 获取电话应用程包名
     * @param context
     * @return
     */
    public static List<String> getTelPhonePackageName(Context context) {
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("tel:1234"));
		intent.addCategory(Intent.CATEGORY_DEFAULT);
        return queryIntentActivities(context, intent);
    }

    /**
     * 获取通话应用包名
     * @param context
     * @return
     */
    public static List<String> getCallPackageName(Context context) {
        Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:1234"));
        intent.addCategory(Intent.CATEGORY_DEFAULT);
        return queryIntentActivities(context, intent);
    }

    /**
     * 获取拨号应用包名
     * @param context
     * @return
     */
    public static List<String> getDialPackageName(Context context) {
        Intent intent = new Intent(Intent.ACTION_DIAL);
        intent.addCategory(Intent.CATEGORY_DEFAULT);
        return queryIntentActivities(context, intent);
    }

    /**
     * 获取浏览器应用包名
     * @param context
     * @return
     */
    public static List<String> getBrowserPackageName(Context context) {
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.baidu.com"));
        intent.addCategory(Intent.CATEGORY_DEFAULT);
        return queryIntentActivities(context, intent);
    }

    /**
	 * 获取短信的包名
	 * @param context
	 * @return
	 */
	public static List<String> getSmsPackageName(Context context) {
		Intent intent = new Intent(Intent.ACTION_SENDTO, Uri.parse("smsto:1234"));
		intent.addCategory(Intent.CATEGORY_DEFAULT);
		return queryIntentActivities(context, intent);
	}

	/**
	 * 获取邮件的包名
	 * @param context
	 * @return
	 */
	public static List<String> getEmailPackageName(Context context) {
		Intent intent = new Intent(Intent.ACTION_SENDTO, Uri.parse("mailto:xxxx@qq.com"));
		intent.addCategory(Intent.CATEGORY_DEFAULT);
		return queryIntentActivities(context, intent);
	}

    /**
	 * 获取设置的包名
	 * @param context
	 * @return
	 */
	public static List<String> getSettingPackageName(Context context) {
		Intent intent = new Intent(Settings.ACTION_SETTINGS);
		return queryIntentActivities(context, intent);
	}

	/**
	 * 获取桌面的包名
	 * @param context
	 * @return
	 */
	public static List<String> getLauncherPackageName(Context context) {
		Intent intent = new Intent(Intent.ACTION_MAIN);
		intent.addCategory(Intent.CATEGORY_HOME);
		return queryIntentActivities(context, intent);
	}

	/**
	 * 获取安装应用程序的包
	 * @param context
	 * @return
	 */
//	public static List<String> getInstallPackageName(Context context) {
//		Intent intent = new Intent(Intent.ACTION_VIEW);
//		intent.setDataAndType(Uri.fromFile(new File(FileUtil.SDCARD)), "application/vnd.android.package-archive");
//		return queryIntentActivities(context, intent);
//	}

	/**
	 * 获取卸载应用程序的包名
	 * @param context
	 * @return
	 */
	public static List<String> getUnInstallPackageName(Context context) {
		return queryIntentActivities(context, new Intent(Intent.ACTION_DELETE, Uri.parse("package:")));
	}

	public static List<String> queryIntentActivities(Context context, Intent intent) {
		List<String> list = new ArrayList<String>();
		List<ResolveInfo> resolveInfos = context.getPackageManager().queryIntentActivities(intent, 0);
		for (ResolveInfo resolveInfo : resolveInfos) {
			list.add(resolveInfo.activityInfo.packageName);
		}
		return list.isEmpty() ? null : list;
	}

	public static Intent getDefaultLauncherIntent(Context context, String launcher) {
		if (!android.text.TextUtils.isEmpty(launcher)) {
			Intent intentToResolve = new Intent(Intent.ACTION_MAIN);
			intentToResolve.addCategory(Intent.CATEGORY_HOME);
			intentToResolve.setPackage(launcher);
			ResolveInfo ri = context.getPackageManager().resolveActivity(intentToResolve, 0);
			if (ri != null) {
				Intent intent = new Intent(intentToResolve);
				intent.setClassName(ri.activityInfo.applicationInfo.packageName, ri.activityInfo.name);
				intent.setAction(Intent.ACTION_MAIN);
				intent.addCategory(Intent.CATEGORY_HOME);
				intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				return intent;
			}
		}
		return null;
	}


	public static void gotoLauncher(Context context, String defaultLauncher) {
		Intent intent = getDefaultLauncherIntent(context, getDefaultLauncher(context));
		if (null == intent) {
			intent = getDefaultLauncherIntent(context, defaultLauncher);
		}

		if (null == intent) {
			intent = new Intent(Intent.ACTION_MAIN);
			intent.addCategory(Intent.CATEGORY_HOME);
			intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		}
		try {
			context.startActivity(intent);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static boolean launcherApp(Context context, String pkgName) {
		try {
			Intent intent = context.getPackageManager().getLaunchIntentForPackage(pkgName);
			if (null == intent) {
				intent = new Intent(Intent.ACTION_MAIN);
				intent.setPackage(pkgName);
			}
			if (!(context instanceof Activity)) {
				intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			}
			context.startActivity(intent);
			return true;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}

	/**
	 * 获取指定包的版本号
	 *
	 * @author huyong
	 * @param context
	 * @param pkgName
	 */
	public static int getVersionCodeByPkgName(Context context, String pkgName) {
		int versionCode = 0;
		if (pkgName != null) {
			PackageManager pkgManager = context.getPackageManager();
			try {
				PackageInfo pkgInfo = pkgManager.getPackageInfo(pkgName, 0);
				versionCode = pkgInfo.versionCode;
			} catch (PackageManager.NameNotFoundException e) {
				Log.i("AppUtils", "getVersionCodeByPkgName=" + pkgName + " has " + e.getMessage());
			}
		}
		return versionCode;
	}

	/**
	 * 获取指定包的版本名称
	 *
	 * @author huyong
	 * @param context
	 * @param pkgName
	 */
	public static String getVersionNameByPkgName(Context context, String pkgName) {
		String versionName = "0.0";
		if (pkgName != null) {
			PackageManager pkgManager = context.getPackageManager();
			try {
				PackageInfo pkgInfo = pkgManager.getPackageInfo(pkgName, 0);
				versionName = pkgInfo.versionName;
			} catch (PackageManager.NameNotFoundException e) {
			}
		}
		return versionName;
	}

	/**
	 * 获取移动数据状态
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
	 * @param mContext
	 * @param enable
	 */
	public static void setMobileDataEnabled(Context mContext, boolean enable) {
		try {
			ConnectivityManager connectivityManager = (ConnectivityManager) mContext.getSystemService(Context.CONNECTIVITY_SERVICE);
			Method setMobileDataEnabled = ConnectivityManager.class.getMethod("setMobileDataEnabled", boolean.class);
			setMobileDataEnabled.invoke(connectivityManager, enable);
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

	public static List<ResolveInfo> findActivitiesForPackage(Context context, String packageName) {
		final PackageManager packageManager = context.getPackageManager();
		final Intent mainIntent = new Intent(Intent.ACTION_MAIN, null);
		mainIntent.addCategory(Intent.CATEGORY_LAUNCHER);
		mainIntent.setPackage(packageName);
		final List<ResolveInfo> apps = packageManager.queryIntentActivities(mainIntent, 0);
		return apps;
	}

	/**
	 * 检查是安装某包
	 *
	 * @param context
	 * @param packageName
	 *            包名
	 * @return
	 */
	public static boolean isAppExist(final Context context, final String packageName) {
		if (context == null || packageName == null) {
			return false;
		}
		boolean result = false;
		try {
			// context.createPackageContext(packageName,
			// Context.CONTEXT_IGNORE_SECURITY);
			context.getPackageManager().getPackageInfo(packageName,
				PackageManager.GET_SHARED_LIBRARY_FILES);
			result = true;
		} catch (PackageManager.NameNotFoundException e) {
			result = false;
		} catch (Exception e) {
			result = false;
		}
		return result;
	}

	/**
	 * 优先跳转到market，如果失败则转到浏览器
	 *
	 * @param context
	 * @param marketUrl
	 *            market地址
	 * @param browserUrl
	 *            浏览器地址
	 */
	public static void gotoMarket(Context context, String marketUrl, String browserUrl) {
		if (!gotoMarket(context, marketUrl)) {
			gotoSysBrowser(context, browserUrl);
		}
	}

	public static final String MARKET_PACKAGE = "com.android.vending";

	/**
	 * 跳转到Android Market
	 *
	 * @param uriString
	 *            market的uri
	 * @return 成功打开返回true
	 */
	public static boolean gotoMarket(Context context, String uriString) {
		if (!isAppExist(context, MARKET_PACKAGE)) {
			return false;
		}
		boolean ret = false;
		Intent marketIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(uriString));
		marketIntent.setPackage(MARKET_PACKAGE);
		if (context instanceof Activity) {
			marketIntent.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
		} else {
			marketIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		}
		try {
			context.startActivity(marketIntent);
			ret = true;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return ret;
	}

	/**
	 * 系统浏览器直接访问uri，无特殊处理
	 *
	 * @param uriString
	 * @return 成功打开返回true
	 */
	public static boolean gotoSysBrowser(Context context, String uriString) {
		boolean ret = false;
		if (uriString == null) {
			return ret;
		}
		Uri browserUri = Uri.parse(uriString);
		if (null != browserUri) {
			Intent browserIntent = new Intent(Intent.ACTION_VIEW, browserUri);
			browserIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			browserIntent.setClassName("com.android.browser", "com.android.browser.BrowserActivity");
			try {
				context.startActivity(browserIntent);
				ret = true;
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		if (!ret && null != browserUri) {
			Intent browserIntent = new Intent(Intent.ACTION_VIEW, browserUri);
			browserIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			try {
				context.startActivity(browserIntent);
				ret = true;
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return ret;
	}

	/**
	 * 浏览器直接访问uri，无特殊处理
	 *
	 * @param uriString
	 * @return 成功打开返回true
	 */
	public static boolean gotoBrowser(Context context, String uriString) {
		boolean ret = false;
		if (uriString == null) {
			return ret;
		}

		Uri browserUri = Uri.parse(uriString);
		if (null != browserUri) {
			Intent browserIntent = new Intent(Intent.ACTION_VIEW, browserUri);
			browserIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

			try {
				context.startActivity(browserIntent);
				ret = true;
			} catch (ActivityNotFoundException e) {
				e.printStackTrace();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return ret;
	}

	/**
	 * 通过地址获取资源对象
	 * @param context
	 * @param apkPath
	 * @return
	 */
	public static Resources getResourcesByPath(Context context, String apkPath) {
		PackageInfo packageInfo = getPackageInfoByPath(context, apkPath);
		if (null != packageInfo && packageInfo.applicationInfo != null) {
			try {
				return context.getPackageManager().getResourcesForApplication(packageInfo.applicationInfo);
			} catch (PackageManager.NameNotFoundException e) {
				e.printStackTrace();
			}
		}
		return null;
	}

	/**
	 * 获取包信息
	 * @param context
	 * @param apkPath
	 * @return
	 */
	public static PackageInfo getPackageInfoByPath(Context context, String apkPath) {
		try {
			PackageManager packageManager = context.getPackageManager();
			PackageInfo packageInfo = packageManager.getPackageArchiveInfo(apkPath, Context.CONTEXT_INCLUDE_CODE | Context.CONTEXT_IGNORE_SECURITY);
			if (null != packageInfo) {
				ApplicationInfo applicationInfo = packageInfo.applicationInfo;
				if (null != applicationInfo) {
					applicationInfo.publicSourceDir = apkPath;
					applicationInfo.sourceDir = apkPath;
					return packageInfo;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 通过包名获取资源对象
	 * @param context
	 * @param pkgName
	 * @return
	 */
	public static Context getContextByPkgName(Context context, String pkgName) {
		try {
			Context packageContext = context.createPackageContext(pkgName, Context.CONTEXT_INCLUDE_CODE | Context.CONTEXT_IGNORE_SECURITY);
			return packageContext;
		} catch (PackageManager.NameNotFoundException e) {
			e.printStackTrace();
		}
		return null;
	}


	public static void uninstall(@NonNull Context context, @NonNull String pkgName) {
		Intent intent = new Intent(Intent.ACTION_DELETE);
		intent.setData(Uri.parse("package:" + pkgName));
		if (!(context instanceof Activity)) {
			intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		}
		context.startActivity(intent);
	}

	public static String getDefaultBrowserApp(Context context) {
		String default_browser = "android.intent.category.DEFAULT";
		String browsable = "android.intent.category.BROWSABLE";
		String view = "android.intent.action.VIEW";

		Intent intent = new Intent(view);
		intent.addCategory(default_browser);
		intent.addCategory(browsable);
		Uri uri = Uri.parse("http://");
		intent.setDataAndType(uri, null);


		// 找出手机当前安装的所有浏览器程序
		List<ResolveInfo> resolveInfoList = context.getPackageManager().queryIntentActivities(intent, PackageManager.GET_INTENT_FILTERS);
		if (resolveInfoList.size() > 0) {
			ActivityInfo activityInfo = resolveInfoList.get(0).activityInfo;
			return activityInfo.packageName;
		} else {
			return null;
		}
	}

    public static boolean hasDialApp(Context context) {
        PackageManager packageManager = context.getPackageManager();
        Intent intent = new Intent(Intent.ACTION_DIAL);
        ResolveInfo info = packageManager.resolveActivity(intent, 0);
        return info != null;
    }

    public static boolean hasBrowserApp(Context context) {
        PackageManager packageManager = context.getPackageManager();
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.baidu.com"));
        ResolveInfo info = packageManager.resolveActivity(intent, 0);
        return info != null;
    }

    public static boolean hasSmsApp(Context context) {
        PackageManager packageManager = context.getPackageManager();
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("smsto:12345"));
        ResolveInfo info = packageManager.resolveActivity(intent, 0);
        return info != null;
    }

    /**
     * 获取系统短信的包名
     * @param context
     * @return
     */
	public static String getSystemSmsPkgName(Context context) {
		Intent intent = new Intent(Intent.ACTION_SENDTO, Uri.parse("smsto:1234"));
		intent.addCategory(Intent.CATEGORY_DEFAULT);
		List<ResolveInfo> resolveInfos = context.getPackageManager().queryIntentActivities(intent, 0);
		for (ResolveInfo resolveInfo : resolveInfos) {
			ApplicationInfo appInfo = resolveInfo.activityInfo.applicationInfo;
			if ((appInfo.flags & ApplicationInfo.FLAG_SYSTEM) != 0
					|| (appInfo.flags & ApplicationInfo.FLAG_UPDATED_SYSTEM_APP) != 0) {
				return resolveInfo.activityInfo.packageName;
			}
		}
		return null;
	}

	/**
	 * 优先跳转到market，如果失败则转到浏览器
	 *
	 * @param context
	 * @param marketUrl
	 *            market地址
	 * @param browserUrl
	 *            浏览器地址
	 */
	public static void gotoMarketFailToBrowser(Context context, String marketUrl, String browserUrl) {
		if (!gotoMarket(context, marketUrl)) {
			gotoBrowser(context, browserUrl);
		}
	}

    /**
     * 是否是内置应用
     * @param info
     * @return
     */
    public static boolean isSysApp(ApplicationInfo info) {
        return (info.flags & ApplicationInfo.FLAG_UPDATED_SYSTEM_APP) != 0
                || (info.flags & ApplicationInfo.FLAG_SYSTEM) != 0;
    }


	/**
	 * 判断当前界面是否是桌面
	 */
	public static boolean isHome(Context context) {
		ComponentName topComponentName = getTopPackageName(context);
		String topPackageName = null;
		if (null != topComponentName) {
			topPackageName = topComponentName.getPackageName();
		}

		List<String> homes = getLauncherPackageName(context);

		if ((null != homes) && (homes.contains(topPackageName))) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * 判断当前是否是锁屏页面
	 */
	public static boolean isKeyguard(Context context) {
		boolean isKeyguard = false;
		KeyguardManager keyguardManager = (KeyguardManager) context.getSystemService(Context.KEYGUARD_SERVICE);
		if (keyguardManager.inKeyguardRestrictedInputMode()) {
			isKeyguard = true;
		}
		return isKeyguard;
	}


	/**
	 * 查询已安装的应用
	 *
	 * @param context
	 * @return
	 */
	public static List<PackageInfo> getInstalledPackages(Context context) {
		PackageManager pManager = context.getPackageManager();
		List<PackageInfo> paklist = null;
		try {
			paklist = pManager.getInstalledPackages(0);
		} catch (Throwable e) {
			e.printStackTrace();
		}
		if (paklist == null) {
			paklist = new ArrayList<PackageInfo>();
		}
		return paklist;
	}

	/**
	 * 判断一个应用是否已经停止运行.<br>
	 *
	 * @param context
	 * @param packageName
	 * @return
	 */
	@TargetApi(Build.VERSION_CODES.HONEYCOMB_MR1)
	public static boolean isAppStop(Context context, String packageName) {
		boolean isStop = false;
		try {
			ApplicationInfo app = context.getPackageManager()
					.getApplicationInfo(packageName, 0);
			isStop = (app.flags & ApplicationInfo.FLAG_STOPPED) != 0;
		} catch (Exception e) {
			isStop = true; // 通常是程序不存在了吧...
			e.printStackTrace();
		}
		return isStop;
	}

	/**
	 * 判断应用是否是系统应用
	 *
	 * @author kingyang
	 * @return
	 */
	public static boolean isSystemApp(ApplicationInfo applicationInfo) {
		boolean isSystemApp = false;
		if (applicationInfo != null) {
			isSystemApp = (applicationInfo.flags & ApplicationInfo.FLAG_SYSTEM) != 0
					|| (applicationInfo.flags & ApplicationInfo.FLAG_UPDATED_SYSTEM_APP) != 0;
		}
		return isSystemApp;
	}

	/**
	 * 获取桌面类应用的包名.<br>
	 *
	 * @param context
	 * @return
	 */
	public static List<String> getLauncherPackageNames(Context context) {
		List<String> packages = new ArrayList<String>();
		PackageManager packageManager = context.getPackageManager();
		Intent intent = new Intent(Intent.ACTION_MAIN);
		intent.addCategory(Intent.CATEGORY_HOME);
		List<ResolveInfo> resolveInfo = null;
		try {
			resolveInfo = packageManager.queryIntentActivities(intent,
					PackageManager.MATCH_DEFAULT_ONLY);
		} catch (Exception e) {
			e.printStackTrace();
		}
		if (resolveInfo != null && !resolveInfo.isEmpty()) {
			for (ResolveInfo info : resolveInfo) {
				// 过滤掉一些名不符实的桌面
				if (!android.text.TextUtils.isEmpty(info.activityInfo.packageName)
						&& !sNotALauncher
						.contains(info.activityInfo.packageName)) {
					packages.add(info.activityInfo.packageName);
				}
			}
		}
		return packages;
	}

	/**
	 * 判断一个应用是否已经停用（不是停止运行）.<br>
	 *
	 * @param context
	 * @param packageName
	 * @return
	 */
	public static boolean isAppDisable(Context context, String packageName) {
		boolean isDisable = false;
		try {
			ApplicationInfo app = context.getPackageManager()
					.getApplicationInfo(packageName, 0);
			isDisable = !app.enabled;
		} catch (Exception e) {
			isDisable = true; // 通常是程序不存在了吧...
			e.printStackTrace();
		}
		return isDisable;
	}

	/**
	 * 获取app包信息
	 *
	 * @param context
	 * @param packageName
	 *            包名
	 * @return
	 */
	public static PackageInfo getAppPackageInfo(final Context context,
                                                final String packageName) {
		PackageInfo info = null;
		try {
			info = context.getPackageManager().getPackageInfo(packageName, 0);
		} catch (Exception e) {
			info = null;
			e.printStackTrace();
		}
		return info;
	}

    public static Intent createSelfMessageIntent(Context context) {
        Intent intent = null;
        if (Build.VERSION.SDK_INT >= 19) {  //4.4
            String defaultSmsPackageName = getDefaultSmsPackage(context);
            if (defaultSmsPackageName == null) {
                defaultSmsPackageName = "com.google.android.talk";
            }
            try {
                PackageManager packageManager = context.getPackageManager();
                intent = packageManager.getLaunchIntentForPackage(defaultSmsPackageName);
            } catch (Throwable e) {
                intent = null;
            }
        }
        if (null == intent) {
            intent = new Intent(Intent.ACTION_MAIN);
            intent.setType("vnd.android-dir/mms-sms");
        }
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        return intent;
    }
    public static synchronized String getDefaultSmsPackage(Context context) {
        Method mGetDefaultSmsPackageMethod = null;
        try {
            if (mGetDefaultSmsPackageMethod == null) {
                Class<?> smsClass = Class.forName("android.provider.Telephony$Sms");
                Class<?>[] argsOfMethod = new Class<?>[] { Context.class };
                mGetDefaultSmsPackageMethod = smsClass.getMethod("getDefaultSmsPackage", argsOfMethod);
            }
            return (String) mGetDefaultSmsPackageMethod.invoke(null, context);
        } catch (Throwable tr) {
            tr.printStackTrace();
        }
        return null;

    }
}

/*
 * Copyright (c) 2016. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */
package app.witness.com.myapplication.addview;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.res.Resources;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import com.orhanobut.logger.Logger;

import java.util.List;
import java.util.Locale;

/**
 * 获取手机设备信息类
 */
public class PhoneUtil {

    public static final String TAG = "PhoneUtil";

    /**
     * 获取app版本名
     *
     * @param context
     * @return
     * @author yangxiongjie
     * @since JDK 1.6
     */
    public static String getAppVersionName(Context context) {
        PackageInfo packageInfo;
        try {
            packageInfo = context.getPackageManager().getPackageInfo(
                    "com.zzkko", 0);
            return packageInfo.versionName;
        } catch (NameNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static int getAppVersionCode(Context context) {
        PackageInfo packageInfo;
        try {
            packageInfo = context.getPackageManager().getPackageInfo(
                    "com.zzkko", 0);
            return packageInfo.versionCode;
        } catch (NameNotFoundException e) {
            e.printStackTrace();
        }
        return -1;
    }

    /**
     * 获取手机厂商
     *
     * @return
     * @author yangxiongjie
     * @since JDK 1.6
     */
    public static String getVendor() {
        return Build.MANUFACTURER;
    }

    /**
     * 获取设备android，ios
     *
     * @return
     * @author yangxiongjie
     * @since JDK 1.6
     */
    public static String getOs() {
        return "android";
    }

    /**
     * 获取系统版本号
     *
     * @return
     * @author yangxiongjie
     * @since JDK 1.6
     */
    public static String getOsver() {
        return Build.VERSION.RELEASE;
    }

    /**
     * 获取设备型号
     *
     * @return
     * @author yangxiongjie
     * @since JDK 1.6
     */
    public static String getDevice() {
        return Build.MODEL;
    }

    // 获取国际移动用户识别码(International Mobile Subscriber Identity)
    public static String getImsi(Context context) {
        TelephonyManager mTelephonyMgr = (TelephonyManager) context
                .getSystemService(Context.TELEPHONY_SERVICE);
        String imsi = mTelephonyMgr.getSubscriberId();
        return imsi;
    }

    //获取移动设备国际身份码(International Mobile Equipment Identity)
    public static String getImei(Context c) {
        TelephonyManager mTelephonyMgr = (TelephonyManager) c
                .getSystemService(Context.TELEPHONY_SERVICE);
        String imei = mTelephonyMgr.getDeviceId();
        return imei;
    }

    // 获取Mac地址
    public static String getMacAddress(Context c) {
//		return "";
        try {
            WifiManager wifiMgr = (WifiManager) c
                    .getSystemService(Context.WIFI_SERVICE);
            WifiInfo info = (null == wifiMgr ? null :
                    wifiMgr.getConnectionInfo());
            if (null != info) {
                return info.getMacAddress();
            } else {
                return System.currentTimeMillis() + "";
            }
        } catch (Exception e) {
            e.printStackTrace();
            return System.currentTimeMillis() + "";
        }
    }

    // WifiManager
    public static String getDeviceId(Context c) {
        String device = getImei(c); // 移动设备国际身份码(International Mobile Equipment Identity)
        Logger.d(TAG, "dddddddddddddddddddd===" + device);
        if (device == null || "".equals(device)) {
            device = getImsi(c); // 国际移动用户识别码(International Mobile Subscriber Identity)
            Logger.d(TAG, "device  getImsi===" + device);
            if (device == null || "".equals(device)) {
                device = getMacAddress(c); // Mac地址
            }
        }
        return device;
    }

    //当前系统版本是否大于等于指定的系统版本
    public static boolean minSdkCheck(int minSdk) {
        int currSdk = Build.VERSION.SDK_INT;
        return currSdk >= minSdk;
    }

    /**
     * 程序是否在前台运行
     *
     * @return
     */
    public static boolean isAppOnForeground(Context context) {
        // Returns a list of application processes that are running on the
        // device

        ActivityManager activityManager = (ActivityManager) context.getApplicationContext().getSystemService(Context.ACTIVITY_SERVICE);
        String packageName = context.getApplicationContext().getPackageName();
        List<ActivityManager.RunningAppProcessInfo> appProcesses = activityManager
                .getRunningAppProcesses();
        if (appProcesses == null)
            return false;
        for (ActivityManager.RunningAppProcessInfo appProcess : appProcesses) {
            // The name of the process that this object is associated with.
            if (appProcess.processName.equals(packageName)
                    && appProcess.importance == ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND) {
                return true;
            }
        }
        return false;
    }


    /**
     * 强制隐藏软键盘
     */

    public static void disMissKeyBoard(View view) {
        Context context = view.getContext();
        InputMethodManager imm = (InputMethodManager) context
                .getSystemService(Context.INPUT_METHOD_SERVICE);
        // imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0); // 强制隐藏键盘
    }

    /*
    是否需要左右反转布局
    true 表示当前方向是rtl，否则是ltr
     */
    public static boolean shouldReversLayout() {
        int layoutDirectionFromLocale = View.LAYOUT_DIRECTION_LTR;
        try {
            layoutDirectionFromLocale = TextUtils.getLayoutDirectionFromLocale(Locale.getDefault());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return layoutDirectionFromLocale != View.LAYOUT_DIRECTION_LTR;
    }

    public static boolean isArabian(Context context) {
        Locale locale = context.getResources().getConfiguration().locale;
        String language = locale.getLanguage();
        return "ar".equalsIgnoreCase(language);
    }

    public static int getNavigationBarHeight(Activity context) {
        Resources resources = context.getResources();
        int resourceId = resources.getIdentifier("navigation_bar_height", "dimen", "android");
        if (resourceId > 0) {
            return resources.getDimensionPixelSize(resourceId);
        }
        return 0;
    }

    public static int getNavigationBarWidth(Activity context) {
        Resources resources = context.getResources();
        int resourceId = resources.getIdentifier("navigation_bar_width", "dimen", "android");
        if (resourceId > 0) {
            return resources.getDimensionPixelSize(resourceId);
        }
        return 0;
    }

    public static int getNavigationBarHeithtLandscape(Activity context) {
        Resources resources = context.getResources();
        int resourceId = resources.getIdentifier("navigation_bar_height_landscape", "dimen", "android");
        if (resourceId > 0) {
            return resources.getDimensionPixelSize(resourceId);
        }
        return 0;
    }

    /**
     * 判断系统是否显示虚拟导航按钮， 有则会影响屏幕显示高度
     *
     * @param context
     * @return
     */
    public static boolean isVirtualKeyShow(Activity context) {
        if (isHighThanOrEqual4point0() == true) {
            Resources resources = context.getResources();
            int resourceId = resources.getIdentifier("config_showNavigationBar", "bool", "android");
            if (resourceId > 0) {
                return resources.getBoolean(resourceId);
            }
        }
        return false;
    }

    public static boolean isHighThanOrEqual4point0() {
        return getAPIVersion() >= 14;
    }

    public static int getAPIVersion() {
        int APIVersion = Build.VERSION.SDK_INT;
        return APIVersion;
    }

    public static String getPhoneTopPackageName(Context context) {
        ActivityManager activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        String packageName = null;
        if (Build.VERSION.SDK_INT == Build.VERSION_CODES.LOLLIPOP) {
            try {
                packageName = activityManager.getRunningAppProcesses().get(0).processName;
            } catch (Exception e) {
                e.printStackTrace();
            }
        } /*else if (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP) {
            packageName = ProcessManager.getRunningForegroundApps(context.getApplicationContext()).get(0).getPackageName();
        } */ else {
            packageName = activityManager.getRunningTasks(1).get(0).topActivity.getPackageName();
        }
        return packageName;
    }

    public static String getLocaleLanguage() {
        Locale locale = Locale.getDefault();
        String language = locale.getLanguage();
        return language;
    }

    public static boolean isBackground(Context context) {
        ActivityManager activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningAppProcessInfo> appProcesses = activityManager.getRunningAppProcesses();
        for (ActivityManager.RunningAppProcessInfo appProcess : appProcesses) {
            if (appProcess.processName.equals(context.getPackageName())) {
                if (appProcess.importance == ActivityManager.RunningAppProcessInfo.IMPORTANCE_BACKGROUND) {
                    return true;
                }else{
                    return false;
                }
            }
        }
        return false;
    }
}

package com.jdry.zhcm.utils;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;

/**
 * Created by JDRY-SJM on 2018/3/9.
 */

public class Utils {
    public static String getBaiDuServiceUrl(String coords) {
        return "http://api.map.baidu.com/geoconv/v1/?coords=" + coords + "&from=1&to=5&ak=5LVC9NpZfM2ur7zsHpML6tyLVFpjfBTe"
                + "&mcode=96:85:54:C8:06:91:65:25:93:5E:F8:02:3F:42:DE:13:B7:14:4D:CE;com.jdry.nmfs";
    }

    public static String getPhaseDesc(int phaseNo) {
        String result = "";
        switch (phaseNo) {
            case 1:
                result = "A相";
                break;
            case 2:
                result = "B相";
                break;
            case 3:
                result = "C相";
                break;
        }
        return result;
    }

    /**
     * 获取版本名称
     *
     * @param context 上下文
     * @return 版本名称
     */
    public static String getVersionName(Context context) {
        PackageManager pm = context.getPackageManager();
        try {
            PackageInfo packageInfo = pm.getPackageInfo(context.getPackageName(), 0);
            return packageInfo.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 获取版本号
     *
     * @param context 上下文
     * @return 版本号
     */
    public static int getVersionCode(Context context) {
        PackageManager pm = context.getPackageManager();
        try {
            PackageInfo packageInfo = pm.getPackageInfo(context.getPackageName(), 0);
            return packageInfo.versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return 0;
    }
}

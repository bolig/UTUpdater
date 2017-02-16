package cn.utsoft.cd.utupdater.util;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;

/**
 * Created by 李波 on 2017/2/13.
 * Function:
 * Desc:
 */

public class VersionUtil {

    private VersionUtil() {

    }

    /**
     * 获取应用程序名称
     *
     * @param context
     * @return
     */
    public static String getAppName(Context context) {
        try {
            PackageManager packageManager = context.getPackageManager();
            PackageInfo packageInfo = packageManager.getPackageInfo(context.getPackageName(), 0);
            int labelRes = packageInfo.applicationInfo.labelRes;
            return context.getResources().getString(labelRes);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 获取版本号
     *
     * @param context
     * @return 当前应用的版本号
     */
    public static String getVersion(Context context) {
        try {
            PackageManager manager = context.getPackageManager();
            PackageInfo info = manager.getPackageInfo(context.getPackageName(), 0);
            String version = info.versionName;
            return version;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            return "";
        }
    }

    /**
     * 版本比较
     *
     * @param nowVersion    app版本
     * @param serverVersion 服务器版本
     * @return
     */
    public static boolean compareVersion(String nowVersion, String serverVersion) {
        if (nowVersion != null && serverVersion != null) {
            String[] nowVersions = nowVersion.split("\\.");
            String[] serverVersions = serverVersion.split("\\.");
            if (nowVersion != null && serverVersion != null && nowVersions.length > 1 && serverVersions.length > 1) {
                int nowVersionFirst = Integer.parseInt(nowVersions[0]);
                int serverVersionFirst = Integer.parseInt(serverVersions[0]);
                int nowVersionSecond = Integer.parseInt(nowVersions[1]);
                int serverVersionSecond = Integer.parseInt(serverVersions[1]);
                if (nowVersionFirst < serverVersionFirst) {
                    return true;
                } else if (nowVersionFirst == serverVersionFirst && nowVersionSecond < serverVersionSecond) {
                    return true;
                }
            }
        }
        return false;
    }
}

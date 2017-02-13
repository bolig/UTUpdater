package cn.utsoft.cd.utupdater.util;

import android.content.Context;

import java.io.File;

/**
 * Created by 李波 on 2017/2/9.
 * Function:
 * Desc:
 */
public class ApkFileUtil {

    public static File create(Context context, String versionName) {
        File cacheDir = getCacheDir(context);
        cacheDir.mkdirs();
        File file = new File(cacheDir, "update_v_" + versionName + ".apk");
        if (file.exists()) {
            file.delete();
            file = new File(cacheDir, "update_v_" + versionName + ".apk");
        }
        return file;
    }

    private static File getCacheDir(Context context) {
        File cacheDir = context.getExternalCacheDir();
        if (cacheDir == null) {
            cacheDir = context.getCacheDir();
        }
        cacheDir = new File(cacheDir, "updater");
        return cacheDir;
    }
}

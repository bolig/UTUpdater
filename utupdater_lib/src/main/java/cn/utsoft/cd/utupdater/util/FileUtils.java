package cn.utsoft.cd.utupdater.util;

import android.content.Context;
import android.text.TextUtils;

import java.io.File;

/**
 * Created by 李波 on 2017/2/9.
 * Function:
 * Desc:
 */
public class FileUtils {

    /**
     * 创建apk缓存地址
     *
     * @param context
     * @param name
     * @return
     */
    public static File create(Context context, String name) {
        File cacheDir = getCacheDir(context);
        cacheDir.mkdirs();
        if (TextUtils.isEmpty(name)) {
            name = "t_" + System.currentTimeMillis();
        }
        File file = new File(cacheDir, "updater_" + name);
        if (file.exists()) {
            file.delete();
            file = new File(cacheDir, "updater_" + name);
        }
        return file;
    }

    /**
     * 获取系统缓存路径
     *
     * @param context
     * @return
     */
    private static File getCacheDir(Context context) {
        File cacheDir = context.getExternalCacheDir();
        if (cacheDir == null) {
            cacheDir = context.getCacheDir();
        }
        cacheDir = new File(cacheDir, "updater");
        return cacheDir;
    }
}

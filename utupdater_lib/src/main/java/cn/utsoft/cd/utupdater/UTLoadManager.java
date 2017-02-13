package cn.utsoft.cd.utupdater;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.support.v4.content.FileProvider;
import android.text.TextUtils;

import java.io.File;

import cn.utsoft.cd.utupdater.config.DownloadConfig;
import cn.utsoft.cd.utupdater.event.DownloadObserver;
import cn.utsoft.cd.utupdater.event.UTUpdateCallback;
import cn.utsoft.cd.utupdater.service.DowloadService;

/**
 * Created by 李波 on 2017/2/7.
 * Function: 主体类: 1. 用于检测是否需要更新
 * 2. 用于下载apk，
 * 3. 用于安装apk
 * 4...
 * Desc:
 */
public class UTLoadManager {

    public static void check() {

    }


    /**
     * 启动下载
     *
     * @param context
     * @param url
     * @param
     */
    public static void load(Context context, String tag, String url, String name, String versionName, int version) {
        Intent intent = new Intent(context, DowloadService.class);
        intent.setAction(DownloadConfig.ACTION_ADD_DOWNLOAD);
        intent.putExtra(DownloadConfig.DATA_TAG, tag);
        intent.putExtra(DownloadConfig.DATA_URL, url);
        intent.putExtra(DownloadConfig.DATA_NAME, name);
        intent.putExtra(DownloadConfig.DATA_VERSION, versionName);
        intent.putExtra(DownloadConfig.DATA_VERSION, version);
        context.startService(intent);
    }

    /**
     * 暂停下载
     *
     * @param context
     * @param tag
     */
    public static void pause(Context context, String tag) {
        Intent intent = new Intent(context, DowloadService.class);
        intent.setAction(DownloadConfig.ACTION_PAUSE_DOWNLOAD);
        intent.putExtra(DownloadConfig.DATA_TAG, tag);
        context.startService(intent);
    }

    /**
     * 重启下载
     *
     * @param context
     * @param tag
     */
    public static void resume(Context context, String tag) {
        Intent intent = new Intent(context, DowloadService.class);
        intent.setAction(DownloadConfig.ACTION_RESUME_DOWNLOAD);
        intent.putExtra(DownloadConfig.DATA_TAG, tag);
        context.startService(intent);
    }

    /**
     * 移除下载
     *
     * @param context
     * @param tag
     */
    public static void remove(Context context, String tag) {
        Intent intent = new Intent(context, DowloadService.class);
        intent.setAction(DownloadConfig.ACTION_REMOVE_DOWNLOAD);
        intent.putExtra(DownloadConfig.DATA_TAG, tag);
        context.startService(intent);
    }

    /**
     * 清空下载记录
     *
     * @param context
     */
    public static void clearDownloadHistory(Context context) {
        Intent intent = new Intent(context, DowloadService.class);
        intent.setAction(DownloadConfig.ACTION_CLEAR_DOWNLOAD_HISTORY);
        context.startService(intent);
    }

    /**
     * 添加消息回调
     *
     * @param callback
     */
    public static void observer(UTUpdateCallback callback) {
        DownloadObserver
                .getIns()
                .setCallback(callback);
    }

    /**
     * 启动安装
     *
     * @param context
     * @param apkPath
     */
    public static void installApk(Context context, String apkPath) {
        if (context == null || TextUtils.isEmpty(apkPath)) {
            return;
        }

        File file = new File(apkPath);
        Intent intent = new Intent(Intent.ACTION_VIEW);

        //判读版本是否在7.0以上
        if (Build.VERSION.SDK_INT >= 24) {
            //加入provider
            Uri apkUri = FileProvider.getUriForFile(context, "cn.utsoft.cd.utupdater.fileprovider", file);
            //授予一个URI的临时权限
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            intent.setDataAndType(apkUri, "application/vnd.android.package-archive");
        } else {
            intent.setDataAndType(Uri.fromFile(file), "application/vnd.android.package-archive");
        }
        context.startActivity(intent);
    }
}


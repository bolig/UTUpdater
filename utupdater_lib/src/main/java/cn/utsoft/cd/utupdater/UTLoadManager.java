package cn.utsoft.cd.utupdater;

import android.content.Context;
import android.content.Intent;

import cn.utsoft.cd.utupdater.service.DowloadService;
import cn.utsoft.cd.utupdater.config.DownloadConfig;
import cn.utsoft.cd.utupdater.event.DownloadObserver;
import cn.utsoft.cd.utupdater.event.UTUpdateCallback;
import cn.utsoft.cd.utupdater.util.TagUtil;

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
     * @param desc
     * @param callback
     */
    public static void load(Context context,
                            String url,
                            String desc,
                            String version,
                            UTUpdateCallback callback) {
        String tag = TagUtil.tag(version);
        DownloadObserver.getIns().add(tag, callback);

        Intent intent = new Intent(context, DowloadService.class);
        intent.putExtra(DownloadConfig.DATA_TAG, tag);
        intent.putExtra(DownloadConfig.DATA_URL, url);
        intent.putExtra(DownloadConfig.DATA_NAME, version);
        intent.putExtra(DownloadConfig.DATA_VERSION, version);
        context.startService(intent);
    }

    public static void install() {

    }
}

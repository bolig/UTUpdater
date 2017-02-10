package cn.utsoft.cd.utupdater.service;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;

import cn.utsoft.cd.utupdater.config.DownloadConfig;

/**
 * Created by 李波 on 2017/2/8.
 * Function: 后台下载service
 * Desc:
 */
public class DowloadService extends Service {

    private DownloadManager mManager;
    private Context mContext;

    @Override
    public void onCreate() {
        super.onCreate();
        mContext = this.getApplicationContext();
        mManager = DownloadManager.getIns(mContext);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (intent != null) {
            String action = intent.getAction();
            if (DownloadConfig.ACTION_NEW_DOWNLOAD_TASK.equals(action)) {
                // TODO: 创建新的下载列表
            } else {
                if (intent.hasExtra(DownloadConfig.DATA_URL)) {
                    int version = intent.getIntExtra(DownloadConfig.DATA_VERSION, 0);

                    String url = intent.getStringExtra(DownloadConfig.DATA_URL);
                    String tag = intent.getStringExtra(DownloadConfig.DATA_TAG);
                    String desc = intent.getStringExtra(DownloadConfig.DATA_NAME);
                    String versionName = intent.getStringExtra(DownloadConfig.DATA_VERSIONNAME);

                    mManager.addDownload(tag, url, desc, versionName, version);
                }
            }
        }
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mManager.destroy();
    }
}

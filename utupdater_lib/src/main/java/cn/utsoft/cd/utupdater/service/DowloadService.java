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
            String tag = null;
            switch (action) {
                case DownloadConfig.ACTION_ADD_DOWNLOAD:
                    if (checkManager()) {
                        if (intent.hasExtra(DownloadConfig.DATA_URL)) {
                            tag = intent.getStringExtra(DownloadConfig.DATA_TAG);

                            int version = intent.getIntExtra(DownloadConfig.DATA_VERSION, 0);

                            String url = intent.getStringExtra(DownloadConfig.DATA_URL);
                            String desc = intent.getStringExtra(DownloadConfig.DATA_NAME);
                            String versionName = intent.getStringExtra(DownloadConfig.DATA_VERSIONNAME);

                            mManager.addDownload(tag, url, desc, versionName, version);
                        }
                    }
                    break;
                case DownloadConfig.ACTION_PAUSE_DOWNLOAD:
                    if (checkManager()) {
                        tag = intent.getStringExtra(DownloadConfig.DATA_TAG);

                        mManager.pauseDownload(tag);
                    }
                    break;
                case DownloadConfig.ACTION_RESUME_DOWNLOAD:
                    if (checkManager()) {
                        tag = intent.getStringExtra(DownloadConfig.DATA_TAG);

                        mManager.resumeDownload(tag);
                    }
                    break;
                case DownloadConfig.ACTION_CLEAR_DOWNLOAD_HISTORY:
                    if (checkManager()) {
                        mManager.clearDownloadHistory();
                    }
                    break;
            }
        }
        return super.onStartCommand(intent, flags, startId);
    }

    private boolean checkManager() {
        return mManager != null;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mManager.destroy();
    }
}

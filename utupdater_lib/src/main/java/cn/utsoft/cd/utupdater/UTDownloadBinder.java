package cn.utsoft.cd.utupdater;

import android.content.ComponentName;
import android.content.ServiceConnection;
import android.os.IBinder;

import java.util.List;

import cn.utsoft.cd.utupdater.entity.LoadHolder;
import cn.utsoft.cd.utupdater.service.DownloadService;
import cn.utsoft.cd.utupdater.service.IDownload;

/**
 * Created by 李波 on 2017/2/16.
 * Function:
 * Desc:
 */

public class UTDownloadBinder implements IDownload {

    // service连接
    private final ServiceConnection mSConn;
    // 下载控制器
    private IDownload mManager;
    // 绑定成功回调
    private OnBindListener bindListener;

    public UTDownloadBinder() {
        mSConn = new ServiceConnection() {
            @Override
            public void onServiceConnected(ComponentName name, IBinder service) {
                DownloadService.DownloadBinder binder = (DownloadService.DownloadBinder) service;
                DownloadService dowloadService = binder.getService();
                mManager = dowloadService.getManager();
                if (bindListener != null) {
                    bindListener.onBind();
                }
            }

            @Override
            public void onServiceDisconnected(ComponentName name) {

            }
        };
    }

    private boolean checkManager() {
        return mManager != null;
    }

    @Override
    public void addAllDownload(List<LoadHolder> list) {
        if (checkManager()) {
            mManager.addAllDownload(list);
        }
    }

    @Override
    public void addDownload(LoadHolder holder) {
        if (checkManager()) {
            mManager.addDownload(holder);
        }
    }

    @Override
    public void addDownload(String tag, String url, String fileName, String title) {
        if (checkManager()) {
            mManager.addDownload(tag, url, fileName, title);
        }
    }

    @Override
    public void addDownload(String tag, String url) {
        if (checkManager()) {
            mManager.addDownload(tag, url);
        }
    }

    @Override
    public void addDownload(String url) {
        if (checkManager()) {
            mManager.addDownload(url);
        }
    }

    @Override
    public void removeDownload(String tag) {
        if (checkManager()) {
            mManager.removeDownload(tag);
        }
    }

    @Override
    public void removeAllDownload() {
        if (checkManager()) {
            mManager.removeAllDownload();
        }
    }

    @Override
    public void pauseDownload(String tag) {
        if (checkManager()) {
            mManager.pauseDownload(tag);
        }
    }

    @Override
    public void pauseAllDownload() {
        if (checkManager()) {
            mManager.pauseAllDownload();
        }
    }

    @Override
    public void resumeDownload(String tag) {
        if (checkManager()) {
            mManager.resumeDownload(tag);
        }
    }

    @Override
    public void resumeAllDownload() {
        if (checkManager()) {
            mManager.resumeAllDownload();
        }
    }

    @Override
    public void clearDownloadHistory() {
        if (checkManager()) {
            mManager.clearDownloadHistory();
        }
    }

    @Override
    public void setDownloadListener(UTUpdaterListener listener) {
        if (checkManager()) {
            mManager.setDownloadListener(listener);
        }
    }

    public interface OnBindListener {

        void onBind();
    }
}

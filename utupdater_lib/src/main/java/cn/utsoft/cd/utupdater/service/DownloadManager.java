package cn.utsoft.cd.utupdater.service;

import android.content.Context;
import android.os.Bundle;
import android.os.Message;

import java.io.File;
import java.util.List;

import cn.utsoft.cd.utupdater.config.DownloadConfig;
import cn.utsoft.cd.utupdater.db.DownloadDaoImpl;
import cn.utsoft.cd.utupdater.entity.RequestBean;
import cn.utsoft.cd.utupdater.event.DownloadObserver;
import cn.utsoft.cd.utupdater.event.UTUpdateCallback;
import cn.utsoft.cd.utupdater.net.DownloadQueue;
import cn.utsoft.cd.utupdater.util.ApkFileUtil;

/**
 * Created by 李波 on 2017/2/8.
 * Function: 下载管理与下载信息管理类
 * Desc:
 */
public class DownloadManager implements IDownloadManager {

    private volatile static DownloadManager instance;

    private final Context mContext;

    private DownloadQueue mQueue;

    private DownloadHandler mMsgHandler = new DownloadHandler() {

        @Override
        public void handleMessage(Message msg) {
            int what = msg.what;
            Bundle data = msg.getData();
            String tag = data.getString("tag");

            UTUpdateCallback callback = DownloadObserver.getIns().getCallback();

            switch (what) {
                case DownloadConfig.FLAG_REQUEST_ADD_TASK: // tag标示的下载请求到下载队列成功

                    break;
                case DownloadConfig.FLAG_REQUEST_START:    // tag标示的下载请求开始被执行
                    if (callback != null) {
                        callback.onStart(tag);
                    }
                    break;
                case DownloadConfig.FLAG_REQUEST_PROGRESS: // tag标示的下载进度
                    long current = data.getLong("current", 0);
                    long length = data.getLong("length", 0);

                    if (callback != null) {
                        callback.onProgress(tag, current, length);
                    }
                    break;
                case DownloadConfig.FLAG_REQUEST_FINSIH:
                    String filePath = data.getString("path");

                    // 回调下载完成
                    if (callback != null) {
                        callback.onFinish(tag, filePath);
                    }

//                    // 下载完成后移除监听
//                    DownloadObserver
//                            .getIns()
//                            .remove(tag);

                    // 保存下载状态
                    DownloadDaoImpl
                            .getIns(mContext)
                            .updateDownloadFinishInfo(tag, 1);
                    break;
                case DownloadConfig.FLAG_REQUEST_ERROR:
                    int code = data.getInt("code");
                    String errorMsg = data.getString("msg");

                    if (callback != null) {
                        callback.onError(tag, code, errorMsg);
                    }
                    break;
                case DownloadConfig.FLAG_REQUEST_PAUSE:     // 当前请求被暂停成功后, 保存当前下载进度, 以便断点续传
                    long current1 = data.getLong("current", 0);
                    long length1 = data.getLong("length", 0);

                    DownloadDaoImpl
                            .getIns(mContext)
                            .updateDownloadInfo(tag, current1, length1);
                    break;
            }
        }
    };

    public DownloadManager(Context context) {
        this.mContext = context;
    }

    public static DownloadManager getIns(Context context) {
        if (instance == null) {
            synchronized (DownloadManager.class) {
                if (instance == null) {
                    instance = new DownloadManager(context);
                }
            }
        }
        return instance;
    }

    @Override
    public void addDownload(String tag, String url, String desc, String versionName, int version) {
        RequestBean request = new RequestBean(tag,
                url,
                desc,
                versionName,
                version);

        if (request.isNull()) {
            return;
        }

        DownloadDaoImpl dao = DownloadDaoImpl.getIns(mContext);

        RequestBean contains = dao.queryDownloadInfo(request.tag);

        if (contains != null) {
            request = contains;
        } else {
            File file = ApkFileUtil.create(mContext, request.name);

            request.path = file.getAbsolutePath();

            dao.insertDownloadInfo(request);
        }


        getDownloadQueue().addDownloadRequest(request); // 加入下载队列
    }

    @Override
    public void removeDownload(String tag) {
        getDownloadQueue()
                .removeDownloadRequest(tag);
    }

    @Override
    public void addAllDownload(List<RequestBean> list) {
        getDownloadQueue()
                .addAllDownloadRequest(list);
    }

    @Override
    public void pauseDownload(String tag) {
        getDownloadQueue()
                .pauseDownloadRequest(tag);
    }

    @Override
    public void resumeDownload(String tag) {
        getDownloadQueue()
                .resumeRequest(tag);
    }

    @Override
    public void clearDownload() {

    }

    public DownloadQueue getDownloadQueue() {
        if (mQueue == null) {
            synchronized (DownloadManager.class) {
                if (mQueue == null) {
                    mQueue = DownloadQueue.getIns(mContext, mMsgHandler);
                }
            }
        }
        return mQueue;
    }

    @Override
    public void destroy() {
        if (mQueue != null) {
            mQueue.destory();
            mQueue = null;
        }
    }
}

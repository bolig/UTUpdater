package cn.utsoft.cd.utupdater.net;

import android.content.Context;

import java.io.IOException;

import cn.utsoft.cd.utupdater.UTUpdaterListener;
import cn.utsoft.cd.utupdater.event.DownloadCallback;
import cn.utsoft.cd.utupdater.event.RequestHandler;

/**
 * Created by 李波 on 2017/2/8.
 * Function: 请求基类
 * Desc:
 */
public abstract class Request implements Runnable, IRequest {

    private final String tag;                           // 请求唯一标示
    private final Context mContext;
    private final UTUpdaterListener mListener;          // 请求回调
    private volatile RequestHandler handler;

    private Object LOCK = new Object();                 // 同步锁

    protected DownloadCallback callback;      // 下载完成回调

    private volatile boolean isFinish = false;          // 请求是否执行完成
    private volatile boolean isInterrupt = false;       // 请求是否被中断
    private volatile boolean isNetDisconnect = false;   // 网络是否中断
    private volatile boolean isDownloadFinish = false;  // 是否下载完成
    private volatile boolean isAddDownloadTask = false; // 是否添加下载队列

    public Request(Context context, String tag, UTUpdaterListener listener) {
        this.tag = tag;
        this.mListener = listener;
        this.mContext = context.getApplicationContext();
        this.handler = new RequestHandler(mContext, getTag(), mListener);
    }

    /**
     * 设置下载完成回调
     *
     * @param callback
     */
    public void setCallback(DownloadCallback callback) {
        this.callback = callback;
    }

    @Override
    public void run() {
        try {
            if (checkNectEnable()) {
                create();

                request();
            }

            // 请求被执行完
            finish();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean checkNectEnable() {
        synchronized (LOCK) {
            return !isInterrupt
                    && !isNetDisconnect;
        }
    }

    @Override
    public void interrupt() {
        synchronized (LOCK) {
            this.isInterrupt = true;

            this.isNetDisconnect = false;
        }
    }

    @Override
    public boolean isFinish() {
        synchronized (LOCK) {
            return isFinish;
        }
    }

    @Override
    public void reset() {
        synchronized (LOCK) {
            this.isFinish = false;
            this.isInterrupt = false;
            this.isNetDisconnect = false;
        }
    }

    @Override
    public void finish() {
        synchronized (LOCK) {
            this.isFinish = true;
            this.isAddDownloadTask = false;
        }
    }

    @Override
    public void networkDisconnect() {
        synchronized (LOCK) {
            this.isNetDisconnect = true;
        }
    }

    @Override
    public boolean isNetworkDisconnect() {
        synchronized (LOCK) {
            return isNetDisconnect;
        }
    }

    @Override
    public void downloadFinish() {
        synchronized (LOCK) {
            isDownloadFinish = true;
        }
    }

    @Override
    public boolean isDownloadFinish() {
        synchronized (LOCK) {
            return isDownloadFinish;
        }
    }

    @Override
    public void addDownloadTask() {
        synchronized (LOCK) {
            isAddDownloadTask = true;
        }
    }

    @Override
    public boolean isAddDownloadTask() {
        synchronized (LOCK) {
            return isAddDownloadTask;
        }
    }

    @Override
    public String getTag() {
        return tag;
    }

    @Override
    public Context getContext() {
        return mContext;
    }

    @Override
    public RequestHandler getHandler() {
        return handler;
    }
}

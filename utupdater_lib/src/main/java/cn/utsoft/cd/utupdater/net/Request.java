package cn.utsoft.cd.utupdater.net;

import android.content.Context;

import java.io.IOException;

import cn.utsoft.cd.utupdater.service.DownloadHandler;

/**
 * Created by 李波 on 2017/2/8.
 * Function:
 * Desc:
 */
public abstract class Request implements Runnable, IRequest {

    private final String tag;
    private final Context mContext;
    private final DownloadHandler mMsgHandler;

    private Object LOCK = new Object();

    private volatile boolean interrupt = false;
    private volatile boolean isInterrupted = false;

    public Request(Context context, String tag, DownloadHandler handler) {
        this.tag = tag;
        this.mMsgHandler = handler;
        this.mContext = context.getApplicationContext();
    }

    @Override
    public void run() {
        try {
            if (!checkNectEnable()) {
                return;
            }

            create();

            request();

            finish();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean checkNectEnable() {
        return !interrupt;
    }

    @Override
    public void interrupt() {
        synchronized (LOCK) {
            this.interrupt = true;
        }
    }

    @Override
    public boolean isInterrupted() {
        return isInterrupted;
    }

    @Override
    public void reset() {
        synchronized (LOCK) {
            this.interrupt = false;
            this.isInterrupted = false;
        }
    }

    @Override
    public void finish() {
        synchronized (LOCK) {
            this.isInterrupted = true;
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
    public DownloadHandler getHandler() {
        return mMsgHandler;
    }
}

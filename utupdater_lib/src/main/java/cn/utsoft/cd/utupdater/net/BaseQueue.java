package cn.utsoft.cd.utupdater.net;

import android.content.Context;
import android.text.TextUtils;

import java.util.HashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by 李波 on 2017/2/10.
 * Function:
 * Desc:
 */
public class BaseQueue {

    public static final int MAX_DOWNLOADING_TASK = 3; //最大同时下载数

    private final Context mContext;
    private ExecutorService mThreadExecutor; // 线程池
    private HashMap<String, Request> mRequestQueue;
    private HashMap<String, Request> mPauseQueue;

    private Object LOCK = new Object();

    public BaseQueue(Context context) {
        this.mContext = context;
        this.mPauseQueue = new HashMap<>();
        this.mRequestQueue = new HashMap<>();
        this.mThreadExecutor = Executors.newFixedThreadPool(MAX_DOWNLOADING_TASK);
    }

    public void clear() {
        synchronized (LOCK) {
            if (mPauseQueue != null) {
                mPauseQueue.clear();
            }
            if (mRequestQueue != null) {
                mPauseQueue.clear();
            }
            if (mThreadExecutor != null) {
                mThreadExecutor.shutdownNow();
                mThreadExecutor = null;
            }
        }
    }

    public Context getContext() {
        return mContext;
    }

    public void addRequest(String tag, Request request) {
        if (TextUtils.isEmpty(tag)
                || request == null) {
            return;
        }
        synchronized (LOCK) {
            mThreadExecutor.submit(request);
            mRequestQueue.put(tag, request);
        }
    }

    public void removeRequest(String tag) {
        synchronized (LOCK) {
            Request request = mRequestQueue.remove(tag);
            if (request == null) {
                return;
            }
            request.interrupt();
        }
    }

    public void pauseRequest(String tag) {
        synchronized (LOCK) {
            Request request = mRequestQueue.remove(tag);
            if (request == null) {
                return;
            }
            request.interrupt();

            mPauseQueue.put(tag, request);
        }
    }

    public void resumeRequest(String tag) {
        synchronized (LOCK) {
            Request request = mPauseQueue.remove(tag);
            if (request == null) {
                return;
            }

            if (request.isInterrupted()) {
                request.reset();
                addRequest(tag, request);
            } else {
                request.reset();
            }
        }
    }


}

package cn.utsoft.cd.utupdater.event;

import android.text.TextUtils;

import java.lang.ref.WeakReference;
import java.util.HashMap;
import java.util.Map;

import cn.utsoft.cd.utupdater.UTUpdaterCallback;
import cn.utsoft.cd.utupdater.UTUpdaterListener;

/**
 * Created by 李波 on 2017/2/8.
 * Function:
 * Desc:
 */
public class Observer implements UTUpdaterListener {

    private volatile static Observer instance;

    private volatile UTUpdaterCallback callback;

    private volatile Map<String, UTUpdaterListener> mListenerMap;

    private volatile WeakReference<UTUpdaterListener> mListener;

    private Observer() {
        mListenerMap = new HashMap<>();
    }

    public static Observer getIns() {
        if (instance == null) {
            synchronized (Observer.class) {
                if (instance == null) {
                    instance = new Observer();
                }
            }
        }
        return instance;
    }

    public UTUpdaterCallback getCallback() {
        return callback;
    }

    public void setCallback(UTUpdaterCallback callback) {
        this.callback = callback;
    }

    /**
     * 添加下载进度监听
     *
     * @param tag
     * @param listener
     */
    public void addListener(String tag, UTUpdaterListener listener) {
        if (TextUtils.isEmpty(tag) || listener != null) {
            return;
        }
        mListenerMap.put(tag, listener);
    }

    /**
     * 移除监听
     *
     * @param tag
     */
    public void removeListener(String tag) {
        if (TextUtils.isEmpty(tag)) {
            return;
        }
        mListenerMap.remove(tag);
    }

    /**
     * 获取下载进度监听
     *
     * @param tag
     * @return
     */
    public UTUpdaterListener getListener(String tag) {
        if (TextUtils.isEmpty(tag)) {
            return null;
        }
        return mListenerMap.get(tag);
    }

    public void setListener(UTUpdaterListener listener) {
        if (listener != null) {
            this.mListener = new WeakReference<>(listener);
        }
    }

    @Override
    public void onPrepare(String tag) {
        UTUpdaterListener l = null;
        if (mListener != null) {
            l = mListener.get();
            if (l != null) {
                l.onPrepare(tag);
            }
        }
        l = mListenerMap.get(tag);
        if (l != null) {
            l.onPrepare(tag);
        }
    }

    @Override
    public void onStart(String tag) {
        UTUpdaterListener l = null;
        if (mListener != null) {
            l = mListener.get();
            if (l != null) {
                l.onStart(tag);
            }
        }
        l = mListenerMap.get(tag);
        if (l != null) {
            l.onStart(tag);
        }
    }

    @Override
    public void onPause(String tag) {
        UTUpdaterListener l = null;
        if (mListener != null) {
            l = mListener.get();
            if (l != null) {
                l.onPause(tag);
            }
        }
        l = mListenerMap.get(tag);
        if (l != null) {
            l.onPause(tag);
        }
    }

    @Override
    public void onProgress(String tag, long current, long total, String velocity) {
        UTUpdaterListener l = null;
        if (mListener != null) {
            l = mListener.get();
            if (l != null) {
                l.onProgress(tag, current, total, velocity);
            }
        }
        l = mListenerMap.get(tag);
        if (l != null) {
            l.onProgress(tag, current, total, velocity);
        }
    }

    @Override
    public void onComplete(String tag, String path) {
        UTUpdaterListener l = null;
        if (mListener != null) {
            l = mListener.get();
            if (l != null) {
                l.onComplete(tag, path);
            }
        }
        l = mListenerMap.get(tag);
        if (l != null) {
            l.onComplete(tag, path);
        }

        removeListener(tag);
    }

    @Override
    public void onRemove(String tag) {
        UTUpdaterListener l = null;
        if (mListener != null) {
            l = mListener.get();
            if (l != null) {
                l.onRemove(tag);
            }
        }
        l = mListenerMap.get(tag);
        if (l != null) {
            l.onRemove(tag);
        }
    }

    @Override
    public void onError(String tag, int code, String msg) {
        UTUpdaterListener l = null;
        if (mListener != null) {
            l = mListener.get();
            if (l != null) {
                l.onError(tag, code, msg);
            }
        }
        l = mListenerMap.get(tag);
        if (l != null) {
            l.onError(tag, code, msg);
        }
    }
}

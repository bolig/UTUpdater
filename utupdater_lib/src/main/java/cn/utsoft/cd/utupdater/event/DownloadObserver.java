package cn.utsoft.cd.utupdater.event;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by 李波 on 2017/2/8.
 * Function:
 * Desc:
 */
public class DownloadObserver {
    private volatile static DownloadObserver instance;
    private Object LOCK = new Object();

    private Map<String, UTUpdateCallback> callbackMap;
    private UTUpdateCallback callback;

    private DownloadObserver() {
        this.callbackMap = new HashMap<>();
    }

    public static DownloadObserver getIns() {
        if (instance == null) {
            synchronized (DownloadObserver.class) {
                if (instance == null) {
                    instance = new DownloadObserver();
                }
            }
        }
        return instance;
    }

//    private boolean check() {
//        return callbackMap != null;
//    }
//
//    public DownloadObserver add(String tag, UTUpdateCallback callback) {
//        synchronized (LOCK) {
//            if (check()) {
//                callbackMap.put(tag, callback);
//            }
//        }
//        return this;
//    }
//
//    public DownloadObserver remove(String tag) {
//        synchronized (LOCK) {
//            if (check()) {
//                callbackMap.remove(tag);
//            }
//        }
//        return this;
//    }
//
//    public UTUpdateCallback get(String tag) {
//        synchronized (LOCK) {
//            if (check()) {
//                return callbackMap.get(tag);
//            }
//        }
//        return null;
//    }
//
//    public void clear() {
//        synchronized (LOCK) {
//            if (check()) {
//                callbackMap.clear();
//            }
//        }
//    }

    public UTUpdateCallback getCallback() {
        return callback;
    }

    public void setCallback(UTUpdateCallback callback) {
        this.callback = callback;
    }
}

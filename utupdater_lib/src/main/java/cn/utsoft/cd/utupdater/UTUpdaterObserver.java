package cn.utsoft.cd.utupdater;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by 李波 on 2017/2/8.
 * Function:
 * Desc:
 */
public class UTUpdaterObserver {
    private volatile static UTUpdaterObserver instance;

    private UTUpdaterCallback callback;
    private Map<String, UTUpdaterListener> listenerMap;

    private UTUpdaterObserver() {
        listenerMap = new HashMap<>();
    }

    public static UTUpdaterObserver getIns() {
        if (instance == null) {
            synchronized (UTUpdaterObserver.class) {
                if (instance == null) {
                    instance = new UTUpdaterObserver();
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

    public void addListener(String tag, UTUpdaterListener listener) {
        listenerMap.put(tag, listener);
    }

    public UTUpdaterListener getListener(String tag) {
        return listenerMap.remove(tag);
    }
}

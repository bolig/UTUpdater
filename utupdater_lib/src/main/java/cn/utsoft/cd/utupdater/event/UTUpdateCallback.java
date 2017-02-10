package cn.utsoft.cd.utupdater.event;

/**
 * Created by 李波 on 2017/2/8.
 * Function:
 * Desc:
 */
public interface UTUpdateCallback {

    void onStart(String tag);

    void onProgress(String tag, long current, long total);

    void onFinish(String tag, String path);

    void onError(String tag, int code, String msg);
}

package cn.utsoft.cd.utupdater.event;

/**
 * Created by 李波 on 2017/2/8.
 * Function:
 * Desc:
 */
public interface UTUpdateCallback {

    void onStart();

    void onProgress(long current, long total);

    void onFinish(String path);

    void onPause();

    void onError(int code, String msg);
}

package cn.utsoft.cd.utupdater;

/**
 * Created by 李波 on 2017/2/15.
 * Function: 下载生命周期回调
 * Desc:
 */
public interface UTUpdaterListener {

    /**
     * 请求已预备下载, 即添加下载队列成功
     * 但此时被没有被执行
     *
     * @param tag
     */
    void onPrepare(String tag);

    /**
     * 下载请求被执行
     *
     * @param tag
     */
    void onStart(String tag);

    /**
     * 下载请求被暂停
     *
     * @param tag
     */
    void onPause(String tag);

    /**
     * 下载进度回调
     *
     * @param tag
     * @param current
     * @param total
     * @param velocity 下载速度
     */
    void onProgress(String tag, long current, long total, String velocity);

    /**
     * 下载完成
     *
     * @param tag
     * @param path
     */
    void onComplete(String tag, String path);

    /**
     * 下载过程中发生异常
     *
     * @param tag
     * @param code
     * @param msg
     */
    void onError(String tag, int code, String msg);
}

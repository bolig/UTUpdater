package cn.utsoft.cd.utupdater.service;

/**
 * Created by 李波 on 2017/2/10.
 * Function:
 * Desc:
 */
public interface IHandler {

    /**
     * 添加下载队列成功
     *
     * @param tag
     */
    void sendAddTaskSuccess(String tag);

    /**
     * 发送下载请求开始下载
     *
     * @param tag 请求唯一标示
     */
    void sendStart(String tag);

    /**
     * 发送下载进度
     *
     * @param tag
     * @param current
     * @param total
     */
    void sendProgres(String tag, long current, long total);

    /**
     * 发送下载完成
     *
     * @param tag
     * @param filePath
     */
    void sendFinish(String tag, String filePath);

    /**
     * 发送下载异常
     *
     * @param tag
     * @param code
     * @param msg
     */
    void sendError(String tag, int code, String msg);

    /**
     * 当请求暂停成功后回调
     *
     * @param tag
     * @param current
     * @param total
     */
    void sendPause(String tag, long current, long total);
}

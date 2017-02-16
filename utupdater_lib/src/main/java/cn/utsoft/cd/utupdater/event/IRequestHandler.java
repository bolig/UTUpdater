package cn.utsoft.cd.utupdater.event;

/**
 * Created by 李波 on 2017/2/10.
 * Function:
 * Desc:
 */
public interface IRequestHandler {

    /**
     * 发送下载请求开始下载
     *
     * @param tag 请求唯一标示
     */
    void sendStart(String tag);

    /**
     * 发送准备完成
     *
     * @param tag
     */
    void sendPrepare(String tag);

    /**
     * 发送下载进度
     *
     * @param tag
     * @param current
     * @param total
     */
    void sendProgress(String tag, long current, long total, String velocity);

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
     */
    void sendPause(String tag);

    /**
     * 保存当前下载进度
     *
     * @param tag
     * @param current
     * @param total
     */
    void sendSaveProgress(String tag, long current, long total);

    /**
     * 保存当前下载已经完成
     *
     * @param tag
     */
    void sendSaveFinished(String tag);
}

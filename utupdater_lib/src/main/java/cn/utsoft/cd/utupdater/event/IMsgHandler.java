package cn.utsoft.cd.utupdater.event;

/**
 * Created by 李波 on 2017/2/15.
 * Function:
 * Desc:
 */
public interface IMsgHandler {

    /**
     * 通知网络状态改变
     *
     * @param enable
     */
    void sendNetworkChange(boolean enable);

    /**
     * 通知下载请求暂停完成
     */
    void sendPauseAllRequest();

    /**
     * 通知下载请求重启完成
     */
    void sendResumeAllRequest();

    /**
     * 通知移除所有请求完成
     */
    void sendRemoveAllRequest();

    /**
     * 通知数据清空完成
     */
    void sendClearHistory();
}

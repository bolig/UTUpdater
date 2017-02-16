package cn.utsoft.cd.utupdater;

/**
 * Created by 李波 on 2017/2/8.
 * Function: 下载整体状态改变回调
 * Desc:
 */
public interface UTUpdaterCallback {

    /**
     * 网络状态变化
     *
     * @param enable
     */
    void onNetworkChange(boolean enable);

    /**
     * 暂停所有请求完成回调
     */
    void onPauseAllDownload();

    /**
     * 重启所有请求完成回调
     */
    void onResumeAllDownload();

    /**
     * 移除所有请求完成回调
     */
    void onRemoveAllDownload();

    /**
     * 清空数据完成
     */
    void onClearDownloadHistory();
}

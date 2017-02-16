package cn.utsoft.cd.utupdater.event;

/**
 * Created by 李波 on 2017/2/15.
 * Function:
 * Desc:
 */
public interface DownloadCallback {

    /**
     * 下载完成回调
     *
     * @param tag
     */
    void onDownloadFinish(String tag);

    /**
     * 下载出错
     *
     * @param tag
     */
    void onDownloadError(String tag);
}

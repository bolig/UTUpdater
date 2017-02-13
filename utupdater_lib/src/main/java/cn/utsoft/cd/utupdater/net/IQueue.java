package cn.utsoft.cd.utupdater.net;

import java.util.List;

import cn.utsoft.cd.utupdater.entity.RequestBean;

/**
 * Created by 李波 on 2017/2/10.
 * Function:
 * Desc:
 */
public interface IQueue {

    /**
     * 添加一个下载请求到下载队列
     *
     * @param bean
     */
    void addDownloadRequest(RequestBean bean);

    /**
     * 添加一个集合到下载队列
     *
     * @param beanList
     */
    void addAllDownloadRequest(List<RequestBean> beanList);

    /**
     * 移除一个下载请求
     *
     * @param tag
     */
    void removeDownloadRequest(String tag);

    /**
     * 移除所有下载信息
     */
    void removeAllDownloadRequest();

    /**
     * 暂停一个下载请求
     *
     * @param tag
     */
    void pauseDownloadRequest(String tag);

    void pauseAllDownloadRequest();

    /**
     * 重启下载信息
     *
     * @param tag
     */
    void resumeDownloadRequest(String tag);

    /**
     * @param tag
     */
    void resumeAllDownloadRequest(String tag);

    /**
     * 清理队列
     */
    void destory();
}

package cn.utsoft.cd.utupdater.net;

import android.support.annotation.Nullable;

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
    void addDownloadRequest(@Nullable RequestBean bean);

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
     * 将请求从队列中移除, 一般用于请求下载完成
     *
     * @param tag
     */
    void removeRequestFromQueue(String tag);

    /**
     * 暂停一个下载请求
     *
     * @param tag
     */
    void pauseDownloadRequest(String tag);

    /**
     * 暂停所有请求
     */
    void pauseAllDownloadRequest();

    /**
     * 重启下载信息
     *
     * @param tag
     */
    void resumeDownloadRequest(String tag);

    /**
     * 重启所有下载请求
     */
    void resumeAllDownloadRequest();

    /**
     * 网络断开
     */
    void netDisconnect();

    /**
     * 网络重新连接
     */
    void netReconnect();

    /**
     * 清理队列
     */
    void destroy();
}

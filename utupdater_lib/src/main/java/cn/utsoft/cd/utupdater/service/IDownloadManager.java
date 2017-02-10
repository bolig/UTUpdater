package cn.utsoft.cd.utupdater.service;

import java.util.List;

import cn.utsoft.cd.utupdater.entity.RequestBean;

/**
 * Created by 李波 on 2017/2/10.
 * Function:
 * Desc:
 */

public interface IDownloadManager {

    /**
     * 添加一个下载请求
     *
     * @param tag
     * @param url
     * @param desc
     * @param versionName
     * @param version
     */
    void addDownload(String tag, String url, String desc, String versionName, int version);

    /**
     * 移除一个下载
     *
     * @param tag
     */
    void removeDownload(String tag);

    /**
     * 添加下载集合
     *
     * @param list
     */
    void addAllDownload(List<RequestBean> list);

    /**
     * 暂停一个下载请求
     *
     * @param tag
     */
    void pauseDownload(String tag);

    /**
     * 重现
     *
     * @param tag
     */
    void resumeDownload(String tag);

    /**
     * 清空下载队列
     */
    void clearDownload();

    /**
     * 释放下载资源
     */
    void destroy();
}

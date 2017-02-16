package cn.utsoft.cd.utupdater.service;

import java.util.List;

import cn.utsoft.cd.utupdater.UTUpdaterListener;
import cn.utsoft.cd.utupdater.entity.LoadHolder;

/**
 * Created by 李波 on 2017/2/10.
 * Function:
 * Desc:
 */

public interface IDownload {

    /**
     * 批量添加下载请求
     *
     * @param list
     */
    void addAllDownload(List<LoadHolder> list);

    /**
     * 添加一个下载请求
     *
     * @param holder
     */
    void addDownload(LoadHolder holder);

    /**
     * 添加一个下载请求
     *
     * @param tag
     * @param url
     * @param fileName
     * @param title
     */
    void addDownload(String tag, String url, String fileName, String title);

    /**
     * 添加一个下载请求
     *
     * @param tag
     * @param url
     */
    void addDownload(String tag, String url);


    /**
     * 添加一个下载请求
     *
     * @param url
     */
    void addDownload(String url);


    /**
     * 移除一个下载
     *
     * @param tag
     */
    void removeDownload(String tag);

    /**
     * 移除所有请求
     */
    void removeAllDownload();

    /**
     * 暂停一个下载请求
     *
     * @param tag
     */
    void pauseDownload(String tag);

    /**
     * 暂停所有请求
     */
    void pauseAllDownload();

    /**
     * 启动下载
     *
     * @param tag
     */
    void resumeDownload(String tag);

    /**
     * 启动所有下载
     */
    void resumeAllDownload();

    /**
     * 清空下载记录
     */
    void clearDownloadHistory();

    /**
     * 设置下载监听
     *
     * @param listener
     */
    void setDownloadListener(UTUpdaterListener listener);
}

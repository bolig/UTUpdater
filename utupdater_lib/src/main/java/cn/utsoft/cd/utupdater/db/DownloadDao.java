package cn.utsoft.cd.utupdater.db;


import java.util.List;

import cn.utsoft.cd.utupdater.entity.RequestBean;

/**
 * Create By 李波 on 2017/2/8.
 * Function:
 * Desc:
 */
public interface DownloadDao {

    /**
     * 添加下载信息
     *
     * @param info
     */
    void insertDownloadInfo(RequestBean info);

    /**
     * 删除下载信息
     *
     * @param url
     */
    void deleteDownloadInfo(String url);

    /**
     * 更新下载进度信息
     *
     * @param url
     * @param finished
     */
    void updateDownloadInfo(String url, int finished);

    /**
     * 查询下载信息
     *
     * @param url
     * @return
     */
    List<RequestBean> queryDownloadInfos(String url);

    /**
     * 是否存在该下载信息
     *
     * @param url
     * @return
     */
    boolean contains(String url);
}

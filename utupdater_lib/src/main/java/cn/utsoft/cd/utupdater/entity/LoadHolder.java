package cn.utsoft.cd.utupdater.entity;

/**
 * Created by 李波 on 2017/2/16.
 * Function:
 * Desc:
 */
public interface LoadHolder {

    // 获取下载路径(必传)
    String getRequestUrl();

    // 获取请求唯一标示(默认取下载URL)
    String getRequestTag();

    // 获取下载文件(默认取时间戳为文件名)
    String getRequestFileName();

    // 获取文件标题(用于显示在状态栏)
    String getRequestTitle();
}

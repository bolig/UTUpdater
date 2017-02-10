package cn.utsoft.cd.utupdater.entity;

import android.text.TextUtils;

import java.io.Serializable;

/**
 * Created by 李波 on 2017/2/8.
 * Function: 下载参数封装类
 * Desc:
 */
public class RequestBean implements Serializable {
    public int id;

    public int version;
    public int finished; // 0: 没下载完成, 1:下载完成

    public String tag;
    public String name;
    public String url;
    public String versionName;

    public String path;  // apk下载本地路径

    public long length;
    public long progress;

    public RequestBean() {
    }

    public RequestBean(String tag, String url, String name, String versionName, int version) {
        this.tag = tag;
        this.name = name;
        this.url = url;
        this.version = version;
        this.versionName = versionName;
    }

    public boolean isNull() {
        return TextUtils.isEmpty(url)
                || TextUtils.isEmpty(tag);
    }
}
package cn.utsoft.cd.utupdater.entity;

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
    public String fileName;
    public String url;
    public String title;

    public String path;  // apk下载本地路径

    public long length;
    public long progress;

    public RequestBean() {
    }

    public RequestBean(String tag, String url, String fileName, String title) {
        this.tag = tag;
        this.fileName = fileName;
        this.url = url;
        this.title = title;
    }

    public long getProgress() {
        return progress == 0 ? -1 : progress;
    }

    public long getLength() {
        return length == 0 ? -1 : length;
    }

    /**
     * 判断当前请求是否完成下载
     *
     * @return
     */
    public boolean finish() {
        return finished == 1;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        RequestBean bean = (RequestBean) o;

        if (version != bean.version) return false;
        return url != null ? url.equals(bean.url) : bean.url == null;
    }

    @Override
    public int hashCode() {
        int result = version;
        result = 31 * result + (url != null ? url.hashCode() : 0);
        return result;
    }
}

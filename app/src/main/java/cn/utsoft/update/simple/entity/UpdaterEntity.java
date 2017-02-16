package cn.utsoft.update.simple.entity;

/**
 * Created by 李波 on 2017/2/10.
 * Function:
 * Desc:
 */

public class UpdaterEntity {

    public String tag;
    public String name;
    public String url;
    public String versionName;
    public int version;

    public int status = 0; // 0.初始化状态; 1.添加下载队列; 2.正在下载; 3.暂停下载; 4.下载完成; -1. 出错
    public long progress = 0;
    public long total = 0;

    public String path;

    public boolean isAPK = false;

    public UpdaterEntity() {

    }

    public UpdaterEntity(String tag, String url) {
        this.tag = tag;
        this.url = url;

        this.version = (int) (System.currentTimeMillis() / 1000);
        this.versionName = version + "";
        this.name = "apk_" + versionName;
    }

    public UpdaterEntity(String tag, String name, String url, String versionName, int version) {
        this.tag = tag;
        this.name = name;
        this.url = url;
        this.versionName = versionName;
        this.version = version;
    }
}

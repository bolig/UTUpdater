package cn.utsoft.cd.utupdater.config;

/**
 * Created by 李波 on 2017/2/8.
 * Function: 配置参数
 * Desc:
 */
public class DownloadConfig {
    public static final int FLAG_REQUEST_ADD_TASK = 0x000;
    public static final int FLAG_REQUEST_START = 0x001;
    public static final int FLAG_REQUEST_PROGRESS = 0x002;
    public static final int FLAG_REQUEST_FINSIH = 0x003;
    public static final int FLAG_REQUEST_ERROR = 0x004;
    public static final int FLAG_REQUEST_PAUSE = 0x005;

    public static final String ACTION_NEW_DOWNLOAD_TASK = "NEW_DOWNLOAD_TASK"; // 创建新的下载列表

    public static final String DATA_TAG = "DATA_TAG";                   // 唯一标示
    public static final String DATA_URL = "DATA_URL";                   // 下载路径
    public static final String DATA_NAME = "DATA_NAME";                 // 下载标题
    public static final String DATA_VERSION = "DATA_VERSION";           // 下载版本名
    public static final String DATA_VERSIONNAME = "DATA_VERSIONNAME";   // 下载版本

    public static final String DOWNLOAD_TABLE = "DOWNLOAD_TABLE";
}

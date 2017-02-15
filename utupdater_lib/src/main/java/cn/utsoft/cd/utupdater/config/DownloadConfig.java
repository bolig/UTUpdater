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
    public static final int FLAG_REQUEST_FINISH = 0x003;
    public static final int FLAG_REQUEST_ERROR = 0x004;
    public static final int FLAG_REQUEST_PAUSE = 0x005;

    public static final int FLAG_PAUSEALL_REQUEST = 0x006;  // 暂停所有请求
    public static final int FLAG_RESUMEALL_REQUEST = 0x007; // 重启所有请求
    public static final int FLAG_NETWORK_CHANGE = 0x008;    // 网络改变
    public static final int FLAG_DOWNLOAD_COMPLETE = 0x009; // 有请求下载完成
    public static final int FLAG_CLEAR_DOWNLOADINFO = 0x010;// 清空下载信息

    public static final String ACTION_ADD_DOWNLOAD = "cn.utsoft.ACTION_ADD_DOWNLOAD";
    public static final String ACTION_PAUSE_DOWNLOAD = "cn.utsoft.ACTION_PAUSE_DOWNLOAD";
    public static final String ACTION_RESUME_DOWNLOAD = "cn.utsoft.ACTION_RESUME_DOWNLOAD";
    public static final String ACTION_REMOVE_DOWNLOAD = "cn.utsoft.ACTION_REMOVE_DOWNLOAD";
    public static final String ACTION_CLEAR_DOWNLOAD_HISTORY = "cn.utsoft.ACTION_CLEAR_DOWNLOAD_HISTORY";

    public static final String DATA_TAG = "DATA_TAG";                   // 唯一标示
    public static final String DATA_URL = "DATA_URL";                   // 下载路径
    public static final String DATA_NAME = "DATA_NAME";                 // 下载标题
    public static final String DATA_VERSION = "DATA_VERSION";           // 下载版本名
    public static final String DATA_VERSION_NAME = "DATA_VERSIO_NNAME"; // 下载版本

    public static final String DOWNLOAD_TABLE = "DOWNLOAD_TABLE";       // 数据库表名

    public static final String SPU_SETTING_ONLY_WIFI_DOWNLOAD = "SPU_SETTING_ONLY_WIFI_DOWNLOAD"; // 设置仅wifi状态下下载;
}

package cn.utsoft.cd.utupdater.service;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.util.Log;

import java.io.File;
import java.util.List;

import cn.utsoft.cd.utupdater.db.DownloadDaoImpl;
import cn.utsoft.cd.utupdater.entity.RequestBean;
import cn.utsoft.cd.utupdater.event.MsgHandler;
import cn.utsoft.cd.utupdater.net.DownloadQueue;
import cn.utsoft.cd.utupdater.util.NetUtil;

/**
 * Created by 李波 on 2017/2/8.
 * Function: 下载管理与下载信息管理类
 * Desc:
 */
public class DownloadManager implements IDownloadManager {

    private volatile static DownloadManager instance;

    private final Context mContext;

    private DownloadQueue mQueue;

    /**
     * 操作消息通知
     */
    private MsgHandler mMsgHandler = new MsgHandler();

    /**
     * 网络状态监听
     */
    private BroadcastReceiver mNetStateReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            boolean enable = NetUtil.netConnection(mContext);

            mMsgHandler.sendNetworkChange(enable);

            if (enable) {
                // 网络重新连接
                getDownloadQueue().netReconnect();
                Log.e("DownloadManager", "网络重新连接");
            } else {
                // 网络断开连接
                getDownloadQueue().netDisconnect();
                Log.e("DownloadManager", "网络断开连接");
            }
        }
    };

    public DownloadManager(Context context) {
        this.mContext = context;

        init();
    }

    /**
     * 初始化化网络监听
     */
    private void init() {
        boolean enable = NetUtil.netConnection(mContext);

        mMsgHandler.sendNetworkChange(enable);

        IntentFilter filter = new IntentFilter();

        filter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);

        mContext.registerReceiver(mNetStateReceiver, filter);
    }

    public static DownloadManager getIns(Context context) {
        if (instance == null) {
            synchronized (DownloadManager.class) {
                if (instance == null) {
                    instance = new DownloadManager(context);
                }
            }
        }
        return instance;
    }

    @Override
    public void addDownload(String tag, String url, String desc, String versionName, int version) {
        RequestBean request = new RequestBean(tag,
                url,
                desc,
                versionName,
                version);

        if (request.isNull()) {
            return;
        }

        getDownloadQueue().addDownloadRequest(request); // 加入下载队列
    }

    @Override
    public void removeDownload(String tag) {
        DownloadDaoImpl dao = DownloadDaoImpl.getIns(mContext);

        RequestBean bean = dao.queryDownloadInfo(tag);
        dao.deleteDownloadInfo(tag);

        String path = bean.path;
        File file = new File(path);
        if (file.exists()) {
            file.delete();
        }

        getDownloadQueue()
                .removeDownloadRequest(tag);
    }

    @Override
    public void addAllDownload(List<RequestBean> list) {
        getDownloadQueue()
                .addAllDownloadRequest(list);
    }

    @Override
    public void pauseDownload(String tag) {
        getDownloadQueue()
                .pauseDownloadRequest(tag);
    }

    @Override
    public void resumeDownload(String tag) {
        getDownloadQueue()
                .resumeDownloadRequest(tag);
    }

    @Override
    public void clearDownload() {
        getDownloadQueue()
                .removeAllDownloadRequest();
    }

    @Override
    public void clearDownloadHistory() {
        DownloadDaoImpl dao = DownloadDaoImpl.getIns(mContext);

        List<RequestBean> list = dao.queryAll();
        for (int i = 0; i < list.size(); i++) {
            RequestBean bean = list.get(i);
            String path = bean.path;
            File file = new File(path);
            if (file.exists()) {
                file.delete();
            }
        }

        dao.deleteAll();

        mMsgHandler.sendClearHistory();
    }

    /**
     * 获取下载队列管理器
     *
     * @return
     */
    public DownloadQueue getDownloadQueue() {
        if (mQueue == null) {
            synchronized (DownloadManager.class) {
                if (mQueue == null) {
                    mQueue = DownloadQueue.getIns(mContext, mMsgHandler);
                }
            }
        }
        return mQueue;
    }

    @Override
    public void destroy() {
        if (mQueue != null) {
            mQueue.destroy();
            mQueue = null;
        }
        if (mNetStateReceiver != null) {
            mContext.unregisterReceiver(mNetStateReceiver);
        }
    }
}

package cn.utsoft.cd.utupdater.service;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.util.Log;

import java.io.File;
import java.util.List;

import cn.utsoft.cd.utupdater.UTUpdaterListener;
import cn.utsoft.cd.utupdater.db.DownloadDaoImpl;
import cn.utsoft.cd.utupdater.entity.LoadHolder;
import cn.utsoft.cd.utupdater.entity.RequestBean;
import cn.utsoft.cd.utupdater.event.MsgHandler;
import cn.utsoft.cd.utupdater.event.Observer;
import cn.utsoft.cd.utupdater.net.DownloadQueue;
import cn.utsoft.cd.utupdater.util.NetUtil;

/**
 * Created by 李波 on 2017/2/8.
 * Function: 下载管理与下载信息管理类
 * Desc:
 */
public class DownloadManager implements IDownload {

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
    public void addAllDownload(List<LoadHolder> list) {

    }

    @Override
    public void addDownload(LoadHolder holder) {
        addDownload(holder.getRequestTag(),
                holder.getRequestUrl(),
                holder.getRequestFileName(),
                holder.getRequestTitle());
    }

    @Override
    public void addDownload(@NonNull String tag, @NonNull String url, String fileName, String title) {
        if (TextUtils.isEmpty(url)) {
            throw new NullPointerException("下载URL不能为空");
        }
        if (TextUtils.isEmpty(tag)) {
            tag = url;
        }
        // 创建下载请求
        RequestBean request = new RequestBean(tag, url, fileName, title);
        // 加入下载队列
        getDownloadQueue().addDownloadRequest(request);
    }

    @Override
    public void addDownload(String tag, String url) {
        addDownload(tag, url, null, null);
    }

    @Override
    public void addDownload(String url) {
        addDownload(null, url, null, null);
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
    public void removeAllDownload() {
        getDownloadQueue()
                .removeAllDownloadRequest();
    }

    @Override
    public void pauseDownload(String tag) {
        getDownloadQueue()
                .pauseDownloadRequest(tag);
    }

    @Override
    public void pauseAllDownload() {
        getDownloadQueue()
                .pauseAllDownloadRequest();
    }

    @Override
    public void resumeDownload(String tag) {
        getDownloadQueue()
                .resumeDownloadRequest(tag);
    }

    @Override
    public void resumeAllDownload() {
        getDownloadQueue()
                .resumeAllDownloadRequest();
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

    @Override
    public void setDownloadListener(UTUpdaterListener listener) {
        Observer.getIns()
                .setListener(listener);
    }

    /**
     * 获取下载队列管理器
     *
     * @return
     */
    private DownloadQueue getDownloadQueue() {
        if (mQueue == null) {
            synchronized (DownloadManager.class) {
                if (mQueue == null) {
                    mQueue = DownloadQueue.getIns(mContext, mMsgHandler);
                }
            }
        }
        return mQueue;
    }

    /**
     * 回收资源
     */
    void destroy() {
        if (mQueue != null) {
            mQueue.destroy();
            mQueue = null;
        }
        if (mNetStateReceiver != null) {
            mContext.unregisterReceiver(mNetStateReceiver);
        }
    }
}

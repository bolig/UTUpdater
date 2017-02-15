package cn.utsoft.cd.utupdater.net;

import android.content.Context;
import android.text.TextUtils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import cn.utsoft.cd.utupdater.event.DownloadCallback;
import cn.utsoft.cd.utupdater.util.NetUtil;

/**
 * Created by 李波 on 2017/2/10.
 * Function:
 * Desc:
 */
public class BaseQueue {

    public static final int MAX_DOWNLOADING_TASK = 3; //最大同时下载数

    private final Context mContext;
    private ExecutorService mThreadExecutor; // 线程池
    private HashMap<String, Request> mRequestQueue;
    private HashMap<String, Request> mPauseQueue;

    private Object LOCK = new Object();

    private volatile boolean netDisconnect = false;

    /**
     * 当请求下载完成回调重队列中移除
     */
    private DownloadCallback mFinishCallback = new DownloadCallback() {

        @Override
        public void onDownloadFinish(String tag) {
            removeRequest(tag);
        }

        @Override
        public void onDownloadError(String tag) {
//            removeRequest(tag);
        }
    };

    public BaseQueue(Context context) {
        this.mContext = context;
        this.mPauseQueue = new HashMap<>();
        this.mRequestQueue = new HashMap<>();
        this.mThreadExecutor = Executors.newFixedThreadPool(MAX_DOWNLOADING_TASK);

        this.netDisconnect = !NetUtil.netConnection(mContext);
    }

    /**
     * 释放资源
     */
    public void clear() {
        synchronized (LOCK) {
            if (mPauseQueue != null) {
                mPauseQueue.clear();
            }
            if (mRequestQueue != null) {
                mPauseQueue.clear();
            }
            if (mThreadExecutor != null) {
                mThreadExecutor.shutdownNow();
                mThreadExecutor = null;
            }
        }
    }

    protected Context getContext() {
        return mContext;
    }

    /**
     * 添加下载请求
     *
     * @param tag
     * @param request
     */
    protected void addRequest(String tag, Request request) {
        if (TextUtils.isEmpty(tag)
                || request == null) {
            return;
        }

        /**
         * 设置下载完成监听
         */
        request.setCallback(mFinishCallback);

        synchronized (LOCK) {
            // 当前网络为断开时
            if (netDisconnect) {
                // 标记当前请求为因断网而暂停
                request.networkDisconnect();
                mPauseQueue.put(tag, request);
            } else {
                // 将请求加入线程池
                mThreadExecutor.submit(request);
                // 添加下载队列
                mRequestQueue.put(tag, request);
                // 标记当前请求已被加入下载队列
                request.addDownloadTask();
            }
        }
    }

    /**
     * 移除下载请求
     *
     * @param tag
     */
    protected void removeRequest(String tag) {
        synchronized (LOCK) {
            Request request = mRequestQueue.remove(tag);
            if (request != null) {
                request.interrupt();
            } else {
                mPauseQueue.remove(tag);
            }
        }
    }

    /**
     * 移除所有请求
     */
    protected void removeAllRequest() {
        synchronized (LOCK) {
            mPauseQueue.clear();

            Collection<Request> values = mRequestQueue.values();
            Iterator<Request> iterator = values.iterator();

            // 中断所有正在执行的连接
            while (iterator.hasNext()) {
                Request request = iterator.next();

                request.interrupt();
            }

            mRequestQueue.clear();
        }
    }

    /**
     * 暂停下载请求
     *
     * @param tag
     */
    protected void pauseRequest(String tag) {
        synchronized (LOCK) {
            Request request = mRequestQueue.remove(tag);
            if (request == null) {
                request = mPauseQueue.get(tag);
                if (request != null) {
                    request.interrupt();
                }
                return;
            }
            request.interrupt();

            mPauseQueue.put(tag, request);
        }
    }

    /**
     * 暂停所有请求
     */
    protected void pasueAllRequest() {
        synchronized (LOCK) {
            Set<String> keySet = mRequestQueue.keySet();
            Iterator<String> iterator = keySet.iterator();
            while (iterator.hasNext()) {
                String tag = iterator.next();

                Request request = mRequestQueue.get(tag);

                if (request.isDownloadFinish()) {
                    continue;
                }

                request.interrupt();

                mPauseQueue.put(tag, request);
            }
            mRequestQueue.clear();
        }
    }

    /**
     * 重启下载请求
     *
     * @param tag
     */
    protected void resumeRequest(String tag) {
        synchronized (LOCK) {
            Request request = mPauseQueue.remove(tag);
            if (request == null) {
                return;
            }

            // 当请求已经下载完成, 直接移除
            if (request.isDownloadFinish()) {
                return;
            }

            // 当重启时, 如果网络标记为断开, 标记当前请求为被网络断开中断,
            // 当网络重新连接的时候, 它会被自动加入到下载队列中
            if (netDisconnect) {
                request.networkDisconnect();

                mPauseQueue.put(tag, request);
            } else {
                if (request.isFinish()) { // 如果请求中断后执行完成, 重新添加新的请求
                    request.reset();
                    addRequest(tag, request);
                } else { // 如果请求中断后, 还没有被执行, 刷新状态
                    request.reset();
                }

                //将下载请求加入下载队列
                mRequestQueue.put(tag, request);
            }
        }
    }

    /**
     * 重启列表中的所有请求
     */
    protected void resumeAllRequest() {
        synchronized (LOCK) {
            Set<String> keySet = mPauseQueue.keySet();
            Iterator<String> iterator = keySet.iterator();

            while (iterator.hasNext()) {
                String tag = iterator.next();

                Request request = mPauseQueue.get(tag);

                // 当请求已经下载完成, 直接移除
                if (request.isDownloadFinish()) {
                    continue;
                }

                // 当重启时, 如果网络标记为断开, 标记当前请求为被网络断开中断,
                // 当网络重新连接的时候, 它会被自动加入到下载队列中
                if (netDisconnect) {
                    request.networkDisconnect();
                } else {
                    if (request.isFinish()) { // 如果请求中断后执行完成, 重新添加新的请求
                        request.reset();
                        addRequest(tag, request);
                    } else { // 如果请求中断后, 还没有被执行, 刷新状态
                        request.reset();
                    }

                    //将下载请求加入下载队列
                    mRequestQueue.put(tag, request);
                }
            }

            // 清空暂停集合
            mPauseQueue.clear();
        }
    }

    /**
     * 网络断开连接, 暂停所有的下载请求
     */
    public void netDisconnect() {
        if (netDisconnect) {
            return;
        }
        synchronized (LOCK) {
            netDisconnect = true; // 标记为网络断开

            Set<String> keySet = mRequestQueue.keySet();
            Iterator<String> iterator = keySet.iterator();
            while (iterator.hasNext()) {
                String tag = iterator.next();

                Request request = mRequestQueue.get(tag);

                // 中断网络
                request.networkDisconnect();

                mPauseQueue.put(tag, request);
            }
            mRequestQueue.clear();
        }
    }

    /**
     * 网络重新连接, 重启因网络断开暂停的所有下载请求
     */
    public void netReconnect() {
        if (!netDisconnect) {
            return;
        }
        synchronized (LOCK) {
            netDisconnect = false; // 标记为网络连接

            Set<String> keySet = mPauseQueue.keySet();
            Iterator<String> iterator = keySet.iterator();

            List<String> removeList = new ArrayList<>();

            while (iterator.hasNext()) {
                String tag = iterator.next();

                Request request = mPauseQueue.get(tag);

                // 判断下载请求是否是因断网而被暂停
                // 如果是则重启当前下载请求
                if (request.isNetworkDisconnect()) {
                    if (request.isFinish()) { // 如果请求中断后执行完成, 重新添加新的请求
                        request.reset();
                        addRequest(tag, request);
                    } else { // 如果请求中断后, 还没有被执行, 刷新状态
                        request.reset();

                        // 当请求未被加入下载队列时加入队列
                        if (!request.isAddDownloadTask()) {
                            addRequest(tag, request);
                        }
                    }
                    removeList.add(tag);
                }
            }

            for (int i = 0; i < removeList.size(); i++) {
                mPauseQueue.remove(removeList.get(i));
            }
        }
    }
}

package cn.utsoft.cd.utupdater.net;

import android.content.Context;

import java.util.List;

import cn.utsoft.cd.utupdater.entity.RequestBean;
import cn.utsoft.cd.utupdater.service.DownloadHandler;

/**
 * Created by 李波 on 2017/2/9.
 * Function:
 * Desc:
 */
public class DownloadQueue extends BaseQueue implements IQueue {
    private static DownloadQueue instance;

    private final DownloadHandler mMsgHandler;

    private DownloadQueue(Context context, DownloadHandler handler) {
        super(context);
        this.mMsgHandler = handler;
    }

    public static DownloadQueue getIns(Context context, DownloadHandler handler) {
        if (instance == null) {
            synchronized (DownloadQueue.class) {
                if (instance == null) {
                    instance = new DownloadQueue(context, handler);
                }
            }
        }
        return instance;
    }

    @Override
    public void addDownloadRequest(RequestBean bean) {
        DownloadRequest request = new DownloadRequest(getContext(), bean, mMsgHandler);

        addRequest(bean.tag, request);
    }

    @Override
    public void addAllDownloadRequest(List<RequestBean> beanList) {
        for (int i = 0; i < beanList.size(); i++) {
            RequestBean bean = beanList.get(i);

            addDownloadRequest(bean);
        }
    }

    @Override
    public void removeDownloadRequest(String tag) {
        removeRequest(tag);
    }

    @Override
    public void pauseDownloadRequest(String tag) {
        pauseRequest(tag);
    }

    @Override
    public void pauseDownload(String tag) {

    }

    @Override
    public void resumeDownloadRequest(String tag) {
        resumeRequest(tag);
    }

    @Override
    public void destory() {
        clear();
        instance = null;
    }
}

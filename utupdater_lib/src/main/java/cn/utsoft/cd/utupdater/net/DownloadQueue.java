package cn.utsoft.cd.utupdater.net;

import android.content.Context;
import android.text.TextUtils;

import java.io.File;
import java.util.List;

import cn.utsoft.cd.utupdater.UTUpdaterListener;
import cn.utsoft.cd.utupdater.UTUpdaterObserver;
import cn.utsoft.cd.utupdater.db.DownloadDaoImpl;
import cn.utsoft.cd.utupdater.entity.RequestBean;
import cn.utsoft.cd.utupdater.event.MsgHandler;
import cn.utsoft.cd.utupdater.util.FileUtils;

/**
 * Created by 李波 on 2017/2/9.
 * Function:
 * Desc:
 */
public class DownloadQueue extends BaseQueue implements IQueue {

    private static DownloadQueue instance;

    private final MsgHandler mMsgHandler;

    private DownloadQueue(Context context, MsgHandler handler) {
        super(context);
        this.mMsgHandler = handler;
    }

    public static DownloadQueue getIns(Context context, MsgHandler handler) {
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
        String tag = bean.tag;

        UTUpdaterListener listener = UTUpdaterObserver.getIns().getListener(tag);

        // 检查请求是否有历史记录
        DownloadDaoImpl dao = DownloadDaoImpl.getIns(getContext());
        RequestBean history = dao.queryDownloadInfo(bean.tag);

        int status = checkDownloadStatus(bean, history);
        switch (status) {
            case 0: // 没有历史记录
                File file = FileUtils.create(getContext(), bean.name);
                bean.path = file.getAbsolutePath();
                dao.insertDownloadInfo(bean);
                break;
            case 1: // 没有下载完成
                bean = history;
                long progress = bean.getProgress();
                long length = bean.getLength();
                if (listener != null && progress > 0 && length > progress) {
                    listener.onProgress(tag, progress, length, "");
                }
                break;
            case 2:
                bean = history;
                if (listener != null) {
                    // 回调进度为完成
                    listener.onProgress(tag, bean.getLength(), bean.getLength(), "");
                    // 回调下载完成
                    listener.onComplete(tag, bean.path);
                }
                return;
        }

        // 创建下载请求
        DownloadRequest request = new DownloadRequest(getContext(), bean, listener);

        // 添加下载队列
        super.addRequest(tag, request);

        // 回调准备完毕
        if (listener != null) {
            listener.onPrepare(tag);
        }
    }

    /**
     * 检查下载信息(0：需创建新的下载记录; 1: 已有记录, 但未完成; 3: 完成下载, 并且文件存在)
     *
     * @param current
     * @param history
     * @return
     */
    private int checkDownloadStatus(RequestBean current, RequestBean history) {
        if (current.equals(history)) {
            String path = history.path;
            if (TextUtils.isEmpty(path)) {
                return 0;
            } else {
                File file = new File(path);
                if (!file.exists()) {
                    return 0;
                }
            }
            if (history.finish()) {
                return 2;
            } else {
                return 1;
            }
        } else {
            return 0;
        }
    }

    @Override
    public void addAllDownloadRequest(List<RequestBean> beanList) {
//        for (int i = 0; i < beanList.size(); i++) {
//            RequestBean bean = beanList.get(i);
//
//            addDownloadRequest(bean);
//        }
    }

    @Override
    public void removeDownloadRequest(String tag) {
        super.removeRequest(tag);
    }

    @Override
    public void removeAllDownloadRequest() {
        super.removeAllRequest();
    }

    @Override
    public void removeRequestFromQueue(String tag) {
        super.removeRequest(tag);
    }

    @Override
    public void pauseDownloadRequest(String tag) {
        super.pauseRequest(tag);
    }

    @Override
    public void pauseAllDownloadRequest() {
        super.pasueAllRequest();

        mMsgHandler.sendPauseAllRequest();
    }

    @Override
    public void resumeDownloadRequest(String tag) {
        super.resumeRequest(tag);
    }

    @Override
    public void resumeAllDownloadRequest() {
        super.resumeAllRequest();

        mMsgHandler.sendResumeAllRequest();
    }

    @Override
    public void destroy() {
        clear();

        instance = null;
    }
}

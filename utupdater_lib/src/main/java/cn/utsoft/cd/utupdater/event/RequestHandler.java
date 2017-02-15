package cn.utsoft.cd.utupdater.event;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

import cn.utsoft.cd.utupdater.UTUpdaterListener;
import cn.utsoft.cd.utupdater.config.DownloadConfig;
import cn.utsoft.cd.utupdater.db.DownloadDaoImpl;

/**
 * Created by 李波 on 2017/2/15.
 * Function:
 * Desc:
 */
public class RequestHandler extends Handler implements IRequestHandler {

    private final UTUpdaterListener listener;
    private final Context mContext;
    private final String tag;

    public RequestHandler(Context context, String tag, UTUpdaterListener listener) {
        this.tag = tag;
        this.mContext = context;
        this.listener = listener;
    }

    @Override
    public void handleMessage(Message msg) {
        Bundle data = msg.getData();
        String tag = data.getString("tag");
        switch (msg.what) {
            case DownloadConfig.FLAG_REQUEST_START:
                if (checkListener()) {
                    listener.onStart(tag);
                }
                break;
            case DownloadConfig.FLAG_REQUEST_PAUSE:
                long current = data.getLong("current", -1);
                long length = data.getLong("length", -1);

                DownloadDaoImpl
                        .getIns(mContext)
                        .updateDownloadInfo(tag, current, length);

                if (checkListener()) {
                    listener.onPause(tag);
                }
                break;
            case DownloadConfig.FLAG_REQUEST_PROGRESS:
                long current1 = data.getLong("current", -1);
                long length1 = data.getLong("length", -1);
                String v = data.getString("velocity");

                if (checkListener()) {
                    listener.onProgress(tag, current1, length1, v);
                }
                break;
            case DownloadConfig.FLAG_REQUEST_FINISH:
                String filePath = data.getString("path");

                // 回调下载完成
                if (checkListener()) {
                    listener.onComplete(tag, filePath);
                }

                // 保存下载完成状态
                DownloadDaoImpl
                        .getIns(mContext)
                        .updateDownloadFinishInfo(tag, 1);
                break;
            case DownloadConfig.FLAG_REQUEST_ERROR:
                int code = data.getInt("code");
                String errorMsg = data.getString("msg");

                if (checkListener()) {
                    listener.onError(tag, code, errorMsg);
                }
                break;
        }
    }


    /**
     * 检查是否有监听器
     *
     * @return
     */
    private boolean checkListener() {
        return listener != null;
    }

    @Override
    public void sendStart(String tag) {
        Bundle extras = new Bundle();
        extras.putString("tag", tag);

        Message message =
                buildMeassge(DownloadConfig.FLAG_REQUEST_START, extras);
        sendMessage(message);
    }

    @Override
    public void sendProgress(String tag, long current, long total, String velocity) {
        Bundle extras = new Bundle();
        extras.putString("tag", tag);
        extras.putLong("current", current);
        extras.putLong("length", total);
        extras.putString("velocity", velocity);

        Message message =
                buildMeassge(DownloadConfig.FLAG_REQUEST_PROGRESS, extras);
        sendMessage(message);
    }

    @Override
    public void sendFinish(String tag, String filePath) {
        Bundle extras = new Bundle();
        extras.putString("tag", tag);
        extras.putString("path", filePath);

        Message message =
                buildMeassge(DownloadConfig.FLAG_REQUEST_FINISH, extras);
        sendMessage(message);
    }

    @Override
    public void sendError(String tag, int code, String msg) {
        Bundle extras = new Bundle();
        extras.putString("tag", tag);
        extras.putInt("code", code);
        extras.putString("msg", msg);

        Message message =
                buildMeassge(DownloadConfig.FLAG_REQUEST_ERROR, extras);
        sendMessage(message);
    }

    @Override
    public void sendPause(String tag, long current, long total) {
        Bundle extras = new Bundle();
        extras.putString("tag", tag);
        extras.putLong("current", current);
        extras.putLong("length", total);

        Message message =
                buildMeassge(DownloadConfig.FLAG_REQUEST_PAUSE, extras);
        sendMessage(message);
    }

    /**
     * 封装消息
     *
     * @param flag
     * @param extras
     * @return
     */
    private Message buildMeassge(int flag, Bundle extras) {
        Message message = new Message();
        message.what = flag;
        message.setData(extras);
        return message;
    }
}

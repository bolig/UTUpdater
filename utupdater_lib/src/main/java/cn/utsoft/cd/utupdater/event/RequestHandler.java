package cn.utsoft.cd.utupdater.event;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

import cn.utsoft.cd.utupdater.config.DownloadConfig;
import cn.utsoft.cd.utupdater.config.ErrorCode;
import cn.utsoft.cd.utupdater.db.DownloadDaoImpl;
import cn.utsoft.cd.utupdater.util.NetUtil;

/**
 * Created by 李波 on 2017/2/15.
 * Function:
 * Desc:
 */
public class RequestHandler extends Handler implements IRequestHandler {

    private final Context mContext;

    public RequestHandler(Context context) {
        this.mContext = context;
    }

    @Override
    public void handleMessage(Message msg) {
        Bundle data = msg.getData();
        String tag = data.getString("tag");
        switch (msg.what) {
            case DownloadConfig.FLAG_REQUEST_START:
                Observer.getIns()
                        .onStart(tag);
                break;
            case DownloadConfig.FLAG_REQUEST_PAUSE:
                Observer.getIns()
                        .onPause(tag);
                break;
            case DownloadConfig.FLAG_REQUEST_PROGRESS:
                long current1 = data.getLong("current", -1);
                long length1 = data.getLong("length", -1);
                String v = data.getString("velocity");

                Observer.getIns()
                        .onProgress(tag, current1, length1, v);
                break;
            case DownloadConfig.FLAG_REQUEST_FINISH:
                String filePath = data.getString("path");

                Observer.getIns()
                        .onComplete(tag, filePath);
                break;
            case DownloadConfig.FLAG_REQUEST_ERROR:
                int code = data.getInt("code");
                String errorMsg = data.getString("msg");

                Observer.getIns()
                        .onError(tag, code, errorMsg);
                break;
            case DownloadConfig.FLAG_REQUEST_ADD_TASK:
                Observer.getIns()
                        .onPrepare(tag);
                break;
            case DownloadConfig.FLAG_SAVE_PROGRESS:
                long current = data.getLong("current", -1);
                long length = data.getLong("length", -1);
                DownloadDaoImpl
                        .getIns(mContext)
                        .updateDownloadInfo(tag, current, length);
                break;
            case DownloadConfig.FLAG_SAVE_FINISHED:
                // 保存下载完成状态
                DownloadDaoImpl
                        .getIns(mContext)
                        .updateDownloadFinishInfo(tag, 1);
                break;
        }
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
        if (NetUtil.netConnection(mContext)) {
            Bundle extras = new Bundle();
            extras.putString("tag", tag);
            extras.putInt("code", code);
            extras.putString("msg", msg);

            Message message =
                    buildMeassge(DownloadConfig.FLAG_REQUEST_ERROR, extras);
            sendMessage(message);
        } else {
            Bundle extras = new Bundle();
            extras.putString("tag", tag);
            extras.putInt("code", ErrorCode.ERROR_DISCONNECT_CODE);
            extras.putString("msg", "网络连接断开");

            Message message =
                    buildMeassge(DownloadConfig.FLAG_REQUEST_ERROR, extras);
            sendMessage(message);
        }
    }

    @Override
    public void sendPause(String tag) {
        Bundle extras = new Bundle();
        extras.putString("tag", tag);

        Message message =
                buildMeassge(DownloadConfig.FLAG_REQUEST_PAUSE, extras);
        sendMessage(message);
    }

    @Override
    public void sendSaveProgress(String tag, long current, long total) {
        Bundle extras = new Bundle();
        extras.putString("tag", tag);
        extras.putLong("current", current);
        extras.putLong("length", total);

        Message message =
                buildMeassge(DownloadConfig.FLAG_SAVE_PROGRESS, extras);
        sendMessage(message);
    }

    @Override
    public void sendSaveFinished(String tag) {
        Bundle extras = new Bundle();
        extras.putString("tag", tag);

        Message message =
                buildMeassge(DownloadConfig.FLAG_SAVE_FINISHED, extras);
        sendMessage(message);
    }

    @Override
    public void sendPrepare(String tag) {
        Bundle extras = new Bundle();
        extras.putString("tag", tag);

        Message message =
                buildMeassge(DownloadConfig.FLAG_REQUEST_ADD_TASK, extras);
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

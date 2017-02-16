package cn.utsoft.cd.utupdater.event;

import android.os.Handler;
import android.os.Message;

import cn.utsoft.cd.utupdater.UTUpdaterCallback;
import cn.utsoft.cd.utupdater.config.DownloadConfig;

/**
 * Created by 李波 on 2017/2/10.
 * Function: 自定义消息发布Handler
 * Desc:
 */
public class MsgHandler extends Handler implements IMsgHandler {

    @Override
    public void handleMessage(Message msg) {
        UTUpdaterCallback callback
                = Observer.getIns().getCallback();
        if (callback == null) {
            return;
        }
        switch (msg.what) {
            case DownloadConfig.FLAG_NETWORK_CHANGE:
                callback.onNetworkChange((Boolean) msg.obj);
                break;
            case DownloadConfig.FLAG_PAUSEALL_REQUEST:
                callback.onPauseAllDownload();
                break;
            case DownloadConfig.FLAG_RESUMEALL_REQUEST:
                callback.onResumeAllDownload();
                break;
            case DownloadConfig.FLAG_CLEAR_DOWNLOADINFO:
                callback.onClearDownloadHistory();
                break;
            case DownloadConfig.FLAG_REMOVEALL_REQUEST:
                callback.onRemoveAllDownload();
                break;
        }
    }

    @Override
    public void sendNetworkChange(boolean enable) {
        Message message = Message.obtain(this,
                DownloadConfig.FLAG_NETWORK_CHANGE,
                enable);
        sendMessage(message);
    }

    @Override
    public void sendPauseAllRequest() {
        sendEmptyMessage(DownloadConfig.FLAG_PAUSEALL_REQUEST);
    }

    @Override
    public void sendResumeAllRequest() {
        sendEmptyMessage(DownloadConfig.FLAG_RESUMEALL_REQUEST);
    }

    @Override
    public void sendRemoveAllRequest() {
        sendEmptyMessage(DownloadConfig.FLAG_REMOVEALL_REQUEST);
    }

    @Override
    public void sendClearHistory() {
        sendEmptyMessage(DownloadConfig.FLAG_CLEAR_DOWNLOADINFO);
    }
}

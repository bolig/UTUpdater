package cn.utsoft.cd.utupdater.service;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

import cn.utsoft.cd.utupdater.config.DownloadConfig;

/**
 * Created by 李波 on 2017/2/10.
 * Function: 自定义消息发布Handler
 * Desc:
 */
public class DownloadHandler extends Handler implements IHandler {

    @Override
    public void sendAddTaskSuccess(String tag) {
        Bundle extras = new Bundle();
        extras.putString("tag", tag);

        Message message =
                buildMeassge(DownloadConfig.FLAG_REQUEST_ADD_TASK, extras);
        sendMessage(message);
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
    public void sendProgres(String tag, long current, long total) {
        Bundle extras = new Bundle();
        extras.putString("tag", tag);
        extras.putLong("current", current);
        extras.putLong("length", total);

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
                buildMeassge(DownloadConfig.FLAG_REQUEST_FINSIH, extras);
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

    private Message buildMeassge(int flag, Bundle extras) {
        Message message = new Message();
        message.what = flag;
        message.setData(extras);
        return message;
    }
}

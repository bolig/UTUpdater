package cn.utsoft.cd.utupdater.net;

import android.content.Context;

import java.io.IOException;

import cn.utsoft.cd.utupdater.service.DownloadHandler;

/**
 * Created by 李波 on 2017/2/9.
 * Function:
 * Desc:
 */

public interface IRequest {

    String getTag();

    Context getContext();

    DownloadHandler getHandler();

    void create() throws IOException;

    void request();

    void destroy();

    void interrupt();

    void finish();

    boolean isInterrupted();

    void reset();

    boolean checkNectEnable();
}

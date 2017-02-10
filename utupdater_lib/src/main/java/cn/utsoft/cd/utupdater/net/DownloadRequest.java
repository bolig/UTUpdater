package cn.utsoft.cd.utupdater.net;

import android.content.Context;
import android.util.Log;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.net.ConnectException;
import java.net.HttpURLConnection;
import java.net.URL;

import cn.utsoft.cd.utupdater.config.ErrorCode;
import cn.utsoft.cd.utupdater.entity.RequestBean;
import cn.utsoft.cd.utupdater.service.DownloadHandler;

/**
 * Created by 李波 on 2017/2/8.
 * Function:
 * Desc:
 */
public class DownloadRequest extends Request {

    private final RequestBean requestParams;

    private HttpURLConnection mConnection;

    private long current;
    private long total = -1;

    private RandomAccessFile tempFile;

    public DownloadRequest(Context context, RequestBean request, DownloadHandler handler) {
        super(context, request.tag, handler);

        if (request == null || request.isNull()) {
            throw new NullPointerException("url is Null");
        }
        this.requestParams = request;
        this.current = requestParams.progress;
        this.total = requestParams.length;
    }

    @Override
    public void create() throws IOException {
        URL url = new URL(requestParams.url);
        mConnection = (HttpURLConnection) url.openConnection();
        mConnection.setConnectTimeout(5000);
        mConnection.setDoInput(true);
        mConnection.setRequestMethod("GET");

        // 设置文件写入位置
        File file = new File(requestParams.path);
        tempFile = new RandomAccessFile(file, "rwd");
        tempFile.seek(current);

        if (tempFile.length() <= 0) {
            current = 0;
        }

        if (current > 100 && total > 100) {
            mConnection.setRequestProperty("Range",
                    "bytes=" + current + "-" + total);
        }
    }

    @Override
    public void request() {
        InputStream in = null;
        try {

            Log.e("MainActivity", "thread --- " + Thread.currentThread().getName());

            getHandler().sendStart(getTag());

            in = connect();

            if (in == null){
                getHandler().sendError(getTag(),
                        ErrorCode.ERROR_CONNECT_CODE,
                        "网络链接失败");
            }
        } catch (ConnectException e) {
            e.printStackTrace();
            getHandler().sendError(getTag(),
                    ErrorCode.ERROR_TIMEOUT_CODE,
                    "连接超时");
        } catch (IOException e) {
            e.printStackTrace();
            getHandler().sendError(getTag(),
                    ErrorCode.ERROR_CONNECT_CODE,
                    "初始化网络链接失败");
        } finally {
            try {
                tempFile.close();
                mConnection.disconnect();
                if (null != in) {
                    in.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void destroy() {
        if (mConnection != null) {
            mConnection.disconnect();
            mConnection = null;
        }
        if (tempFile != null) {
            try {
                tempFile.close();
                tempFile = null;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 创建连接， 写入文件，更新进度
     *
     * @return
     * @throws IOException
     */
    private InputStream connect() throws IOException {
        InputStream in = null;
        int responseCode = mConnection.getResponseCode();

        if (responseCode == 206 || responseCode == 200) {   //206 Partial Content
            // 读取数据
            in = mConnection.getInputStream();

            total = mConnection.getContentLength();

            byte[] b = new byte[1024 * 4];

            int len = -1;

            while ((len = in.read(b)) != -1) {
                // 写入文件
                tempFile.write(b, 0, len);

                current += len; // 当前进度

                getHandler().sendProgres(getTag(), // 发布下载进度
                        current,
                        total);

                if (!checkNectEnable()) {
                    if (current < total && total > 100) {
                        getHandler().sendPause(getTag(), // 当连接中断时保持当前下载信息
                                current,
                                total);
                    }
                    return in;
                }
            }
            getHandler().sendFinish(getTag(),// 下载完成返回本地路径
                    requestParams.path);
        } else {
            String message = mConnection.getResponseMessage();
            getHandler().sendError(getTag(),
                    responseCode,
                    message);
        }
        return in;
    }
}

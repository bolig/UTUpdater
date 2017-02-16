package cn.utsoft.cd.utupdater.net;

import android.content.Context;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.net.ConnectException;
import java.net.HttpURLConnection;
import java.net.URL;

import cn.utsoft.cd.utupdater.config.ErrorCode;
import cn.utsoft.cd.utupdater.entity.RequestBean;
import cn.utsoft.cd.utupdater.event.RequestHandler;

/**
 * Created by 李波 on 2017/2/8.
 * Function: 下载请求
 * Desc:
 */
public class DownloadRequest extends Request {
    private final RequestBean requestParams;

    private HttpURLConnection mConnection;

    private long current;
    private long total = -1;

    private RandomAccessFile tempFile;

    public DownloadRequest(Context context,
                           RequestBean request,
                           RequestHandler handler) {
        super(context, request.tag, handler);

        if (request == null) {
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
        // 断点续传
        if (current > 100 && total > current) {
            mConnection.setRequestProperty("Range",
                    "bytes=" + current + "-" + total);
        }
    }

    @Override
    public void request() {
        InputStream in = null;
        try {
            // 发布开始下载状态
            getHandler().sendStart(getTag());
            // 创建下载连接, 返回流以做资源释放
            in = connect();
            if (in == null) {
                getHandler().sendError(getTag(),
                        ErrorCode.ERROR_CONNECT_CODE,
                        "网络连接失败");
            }
        } catch (ConnectException e) {
            e.printStackTrace();

            /**
             * 当网络断开时保存当前下载进度
             */
            if (current > 100 && total > current) {
                getHandler().sendSaveProgress(getTag(),
                        current, total);
            }

            // 回调移除, 移除请求
            callback.onDownloadError(getTag());

            if (checkNectEnable()) {
                getHandler().sendError(getTag(),
                        ErrorCode.ERROR_TIMEOUT_CODE,
                        "连接超时");
            }
        } catch (IOException e) {
            e.printStackTrace();

            /**
             * 当网络断开时保存当前下载进度
             */
            if (current > 100 && total > current) {
                getHandler().sendSaveProgress(getTag(),
                        current, total);
            }

            // 回调移除, 移除请求
            callback.onDownloadError(getTag());

            if (checkNectEnable()) {
                getHandler().sendError(getTag(),
                        ErrorCode.ERROR_CONNECT_CODE,
                        "初始化网络链接失败");
            }
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
            // 获取数据流
            in = mConnection.getInputStream();
            if (in == null) {
                return null;
            }
            // 因为断点续传每次返回的ContentLength为剩余的总量, 这样会使进度计算有误
            if (total <= 0) {
                total = mConnection.getContentLength();
            }
            byte[] b = new byte[1024 * 4];
            int len = -1;
            ProgressHelper helper = new ProgressHelper();
            helper.start();

            while ((len = in.read(b)) != -1) {
                // 写入文件
                tempFile.write(b, 0, len);
                // 当前进度
                current += len;
                // 检查发布进度时间间隔, 发布进度, 和计算速度
                helper.publish(current, total, false);
                // 检查当前请求是否已被标记为不可用
                if (!checkNectEnable() && current < total) {
                    // 发布当前进度信息
                    helper.publish(current, total, true);
                    if (current < total && total > 100) {
                        // 发布当前请求已被暂停
                        getHandler().sendPause(getTag());
                        // 当连接中断时保持当前下载信息
                        getHandler().sendSaveProgress(getTag(),
                                current,
                                total);
                    }
                    return in;
                }
            }

            // 发布进度
            helper.publish(current, total, true);

            // 通知下载队列下载完成
            if (callback != null) {
                callback.onDownloadFinish(getTag());
            }

            // 下载完成返回本地路径
            getHandler().sendFinish(getTag(),
                    requestParams.path);
            // 更新数据库下载完成状态
            getHandler().sendSaveFinished(getTag());
        } else {
            String message = mConnection.getResponseMessage();
            // 当前连接异常时返回连接信息
            getHandler().sendError(getTag(),
                    responseCode,
                    message);
        }
        return in;
    }

    /**
     * 进度辅助类, 用于管理进度发布时间和下载速度
     */
    public class ProgressHelper {
        private int delay;
        private long startTime;
        private long progress;

        private Object LOCK = new Object();

        public ProgressHelper() {
            this(1000);
        }

        public ProgressHelper(int delay) {
            this.delay = delay;
        }

        /**
         * 记录开始时间
         */
        public void start() {
            synchronized (LOCK) {
                startTime = System.currentTimeMillis();
            }
        }

        /**
         * 发布进度
         *
         * @param current
         * @param total
         */
        public void publish(long current, long total, boolean finish) {
            synchronized (LOCK) {
                long timeMillis = System.currentTimeMillis();
                int offsetTime = (int) (timeMillis - startTime);
                if (offsetTime >= delay || finish) {
                    start(); // 刷新开始时间

                    float offsetLoad = current - progress; // 下载增量

                    // 当完成时不计算速度
                    String velocity = finish ? "" :
                            calculateVelocity(offsetLoad, offsetTime);

                    // 发布进度
                    getHandler().sendProgress(getTag(), current, total, velocity);

                    this.progress = current;
                }
            }
        }

        /**
         * 计算下载速度
         *
         * @param offsetLoad
         * @param offsetTime
         * @return
         */
        public String calculateVelocity(float offsetLoad, float offsetTime) {
            // offsetLoad / offsetTime的结果单位为 b/ms * 1.024f 单位转换成kb/s
            float v = offsetLoad / offsetTime * 1.024f;
            if (v > 1000) {
                // 当KB/s大于1000时, 单位转换成MB/s
                float v1 = v / 1024f;
                String result = String.format("%.2f", v1);
                return result + "MB/s";
            } else {
                String result = String.format("%.2f", v);
                return result + "KB/s";
            }
        }
    }
}

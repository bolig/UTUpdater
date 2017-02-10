package cn.utsoft.cd.utupdater.net;

import android.util.Log;

import java.io.File;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.net.HttpURLConnection;
import java.net.URL;

import cn.utsoft.cd.utupdater.BuildConfig;

/**
 * Created by 李波 on 2017/2/8.
 * Function:
 * Desc:
 */

public class HttpURLConnectionHelper {

    public static void download(String uri,
                                String filePath,
                                long start,
                                long end,
                                long length,
                                final DownloadCallback callback) {

        if (callback == null) {
            return;
        }

        if (BuildConfig.DEBUG) Log.e("HttpURLConnectionHelper", "download开始下载");

        HttpURLConnection conn = null;

        RandomAccessFile raf = null;

        InputStream in = null;

        long finished = 0;

        callback.onStart(); // 回调

        try {
            URL url = new URL(uri);
            conn = (HttpURLConnection) url.openConnection();
            conn.setConnectTimeout(5000);
            conn.setDoInput(true);
            conn.setRequestMethod("GET");
            // 设置下载位置
            // int start = info.getStart() + info.getFinished();
            if (length > 100) {
                conn.setRequestProperty("Range", "bytes=" + start + "-" + end);
            }

            // 设置文件写入位置
            File file = new File(filePath);
            raf = new RandomAccessFile(file, "rwd");
            raf.seek(start);
            //Intent intent = new Intent(DownloadService.ACTION_UPDATE);
            // 开始下载

            if (callback.check()) {
                return;
            }

            int responseCode = conn.getResponseCode();
            if (responseCode == 206 || responseCode == 200) {   //206 Partial Content
                // 读取数据
                in = conn.getInputStream();

                length = conn.getContentLength();

                byte[] b = new byte[1024 * 4];

                int len = -1;

                while ((len = in.read(b)) != -1) {
                    // 写入文件
                    raf.write(b, 0, len);

                    finished += len; // 当前进度

                    if (finished < length){
                        callback.onProgress(finished, length);
                        if (callback.check()) {
                            return;
                        }
                    } else {
                        callback.onComplete(filePath);
                    }
                }
            } else {
                String message = conn.getResponseMessage();
                callback.onError(responseCode, message);
            }
        } catch (Exception e) {
            e.printStackTrace();
            String msg = "下载失败";
            if (BuildConfig.DEBUG) Log.e("HttpURLConnectionHelper", msg);
        } finally {
            try {
                raf.close();
                if (null != in) {
                    in.close();
                }
                conn.disconnect();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public interface DownloadCallback {

        void onStart();

        void onProgress(long current, long length);

        void onComplete(String filePath);

        void onError(int code, String msg);

        boolean check();


    }
}
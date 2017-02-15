package cn.utsoft.cd.utupdater.net;

import android.content.Context;

import java.io.IOException;

import cn.utsoft.cd.utupdater.event.RequestHandler;

/**
 * Created by 李波 on 2017/2/9.
 * Function:
 * Desc:
 */

public interface IRequest {

    /**
     * 获取当前请求的唯一标示
     *
     * @return
     */
    String getTag();

    /**
     * 获取上下文
     *
     * @return
     */
    Context getContext();

    /**
     * 获取消息分发类
     *
     * @return
     */
    RequestHandler getHandler();

    /**
     * 用于检测状态和创建连接
     *
     * @throws IOException
     */
    void create() throws IOException;

    /**
     * 发起连接, 保存文件
     */
    void request();

    /**
     * 清空资源
     */
    void destroy();

    /**
     * 中断连接, 把当前请求标记为中断;
     * 1.如果正在执行, 中断连接, 保存当前下载状态;
     * 2.如果还没有执行, 不创建连接, 直接完成;
     * 3.如果刚好执行完, 该方法不起作用;
     */
    void interrupt();

    /**
     * 执行完成, 不一定是下载完成
     */
    void finish();

    /**
     * 是否执行完成, 不一定是下载完成
     *
     * @return
     */
    boolean isFinish();

    /**
     * 网络断开
     */
    void networkDisconnect();

    /**
     * 是否网络断开而暂停
     *
     * @return
     */
    boolean isNetworkDisconnect();

    /**
     * 下载完成
     */
    void downloadFinish();

    /**
     * 是否下载完成
     *
     * @return
     */
    boolean isDownloadFinish();

    /**
     * 标记已添加队列
     */
    void addDownloadTask();

    /**
     * 是否添加队列
     *
     * @return
     */
    boolean isAddDownloadTask();

    /**
     * 重置请求状态, 用于刷新暂停和网络断开
     */
    void reset();

    /**
     * 检查请求当前状态, true标示正常, false标示当前请求已被暂停或异常
     *
     * @return
     */
    boolean checkNectEnable();
}

package cn.utsoft.cd.utupdater.util;

import android.app.ActivityManager;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;

import java.util.List;

/**
 * Created by 李波 on 2016/11/30.
 * Function:
 * Desc:
 */
public class NotificationUtil {

    public static final String MY_PKG_NAME = "com.utouu.uhoo";

    /**
     * 添加不含跳转的动作的系统消息
     *
     * @param context
     * @param title
     * @param content
     * @return
     */
    public static int addNotification(Context context, String title, String content, int id) {
        //创建 NotificationManager
        NotificationManager mNotifyManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        //创建 Notification.Builder 对象
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context)
                //点击通知后自动清除
                .setAutoCancel(true)
                .setContentTitle(title)
                .setContentText(content);
        // 发送通知
        mNotifyManager.notify(id, builder.build());

//        BeepManager manager = BeepManager.getInstance(context);
//        manager.playBeepSoundAndVibrate();

        return id;
    }

    /**
     * 添加含跳转的动作的系统消息
     *
     * @param context
     * @param title
     * @param content
     * @return
     */
    public static int addNotification(Context context, String title, String content, Intent intent, int id) {
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        //创建 NotificationManager
        NotificationManager mNotifyManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        //创建 Notification.Builder 对象
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context)
                //点击通知后自动清除
                .setAutoCancel(true)
                .setContentTitle(title)
                .setContentText(content)
                .setContentIntent(pendingIntent);
        // 发送通知
        mNotifyManager.notify(id, builder.build());

//        BeepManager manager = BeepManager.getInstance(context);
//        manager.playBeepSoundAndVibrate();

        return id;
    }

    /**
     * 移除系统消息
     *
     * @param context
     * @param id
     */
    public static void removeNotification(Context context, int id) {
        //创建 NotificationManager
        NotificationManager mNotifyManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        mNotifyManager.cancel(id);
    }

    /**
     * 判断app是否正在运行
     *
     * @param context
     * @return 0: app没有启动, 1: app启动并且在前台, -1: app已启动，但是在后台运行
     */
    public static int cuurentAppIsRunning(Context context) {
        ActivityManager activityManager =
                (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        // 100表示取的最大的任务数
        List<ActivityManager.RunningTaskInfo> list = activityManager.getRunningTasks(100);
        for (ActivityManager.RunningTaskInfo info : list) {
            if (info.topActivity.getPackageName().equals(MY_PKG_NAME)) {
                return 1;
            } else if (info.baseActivity.getPackageName().equals(MY_PKG_NAME)) {
                return -1;
            }
        }
        return 0;
    }

    /**
     * 判断app是否正在运行
     *
     * @param context
     * @return 0: app没有启动, 1: app启动并且在前台, -1: app已启动，但是在后台运行
     */
    public static int getAppCurrentStatus(Context context) {
        ActivityManager activityManager =
                (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        String packageName = context.getPackageName();

        // 获取手机所有正在运行的进程
        List<ActivityManager.RunningAppProcessInfo> appProcesses = activityManager
                .getRunningAppProcesses();

        if (appProcesses == null)
            return 0;

        for (ActivityManager.RunningAppProcessInfo appProcess : appProcesses) {
            if (appProcess.processName.equals(packageName)) {
                if (appProcess.importance ==
                        ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND) {
                    return 1;
                }
                if (appProcess.importance == ActivityManager.RunningAppProcessInfo.IMPORTANCE_BACKGROUND
                        || appProcess.importance == ActivityManager.RunningAppProcessInfo.IMPORTANCE_SERVICE) {
                    return -1;
                }
            }
        }
        return 0;
    }
}

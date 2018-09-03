package com.icode.firstproject.base;

import android.app.Notification;
import android.app.Service;
import android.os.Build;

/**
 * 基础服务类
 */
public abstract class BaseService extends Service {

    public static final int FORE_GROUND_ID = 2183971;

    @Override
    public void onCreate() {
        super.onCreate();
        if (isStartDaemonService()) {
            startDaemon();
        }
    }

    /**
     * 是否开启进程守护
     * @return
     */
    public abstract boolean isStartDaemonService();

    /**
     * 获取默认前台进程ID
     * @return
     */
    public int getNotificationId() {
        return getClass().getSimpleName().hashCode();
    }

    /**
     * 开启前台进程
     */
    private void startDaemon() {
        int notificationId = getNotificationId();
        if (Build.VERSION.SDK_INT < 18) {
            try {
                startForeground(notificationId, new Notification());
            } catch (Throwable e) {
                e.printStackTrace();
            }
        } else {
            try {
                startForeground(notificationId, new Notification());
                DaemonService.startDaemonService(this, notificationId);
            } catch (Throwable e) {
                e.printStackTrace();
            }
        }
    }

}

package com.icode.firstproject.base;

import android.app.Notification;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;

/**
 */
public class DaemonService extends Service {

    public static final String NOTIFICATION_ID = "notification_id";

    public static void startDaemonService(Context context, int id) {
        Intent innerIntent = new Intent(context, DaemonService.class);
        innerIntent.putExtra(NOTIFICATION_ID, id);
        context.startService(innerIntent);
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        try {
            int id = intent.getIntExtra(NOTIFICATION_ID, BaseService.FORE_GROUND_ID);
            startForeground(id, new Notification());
            stopForeground(true);
            stopSelf();
        } catch (Throwable e) {
            e.printStackTrace();
        }
        return super.onStartCommand(intent, flags, startId);
    }

}

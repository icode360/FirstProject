package com.icode.firstproject.base;

import android.app.Application;
import android.content.Intent;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;


public class BaseApp extends Application {


    private static HandlerThread sWorkerThread = new HandlerThread("base-main");

    static {
        sWorkerThread.start();
    }

    private static Handler sWorker = new Handler(sWorkerThread.getLooper());

    private static Handler sHandler = new Handler(Looper.getMainLooper());

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public void startActivity(Intent intent) {
        super.startActivity(intent);
    }

    /**
     * 向线程发送数据
     * @param r runnable 对象
     */
    public static void postThread(Runnable r) {
        sWorker.post(r);
    }

    public static void postThreadDelayed(Runnable r, long delayMillis) {
        sWorker.postDelayed(r, delayMillis);
    }

    /**
     * 向ＵＩ线程发送数据
     * @param r runnable 对象
     */
    public static void postRunInUiThread(Runnable r) {
        sHandler.post(r);
    }

    public static void postUiThreadDelayed(Runnable r, long delayMillis) {
        sHandler.postDelayed(r, delayMillis);
    }

    public static void removeFromUiThread(Runnable r) {
        sHandler.removeCallbacks(r);
    }

    public static Handler getsHandler() {
        return sHandler;
    }

    public static Handler getsWorker() {
        return sWorker;
    }
}

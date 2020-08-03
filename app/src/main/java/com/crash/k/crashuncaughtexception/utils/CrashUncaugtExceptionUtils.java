package com.crash.k.crashuncaughtexception.utils;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.crash.k.crashuncaughtexception.ErroActivity;

import java.io.PrintWriter;
import java.io.StringWriter;

/**
 * Created by K on 2015/12/21.
 */
public class CrashUncaugtExceptionUtils {

    public final static String EXTRA_STRACE_MESSAGE = "extra_strace_message";
    private static final String TAG = "CrashException";

    /**
     * 设置传递错误堆栈信息最大长度 保证不超过128 KB
     */
    private static final int MAX_STACK_TRACE_SIZE = 131071; //128 KB - 1

    private static Application application;

    /**
     * 初始化
     */
    public static void install(Context context) {

        //设置该线程由于未捕获到异常而突然终止时调用的处理程序
        Thread.setDefaultUncaughtExceptionHandler(new Thread.UncaughtExceptionHandler() {
            @Override
            public void uncaughtException(Thread thread, Throwable throwable) {
                //处理异常,我们还可以把异常信息写入文件，以供后来分析。
                String stackTraceString = getThrowableStraceStr(thread, throwable);
                Log.e(TAG, stackTraceString);

                startErroActivity(stackTraceString);
                killCurrentProcess();
            }
        });

        application = (Application) context.getApplicationContext();
        application.registerActivityLifecycleCallbacks(new Application.ActivityLifecycleCallbacks() {
            @Override
            public void onActivityCreated(Activity activity, Bundle bundle) {
                Log.i(TAG, "onActivityCreated");

            }

            @Override
            public void onActivityStarted(Activity activity) {

            }

            @Override
            public void onActivityResumed(Activity activity) {
                Thread.UncaughtExceptionHandler unCaughtExceptionHandler = Thread.getDefaultUncaughtExceptionHandler();
                Log.i("LifecycleCallbacks", activity.getClass().getName() + "-->\n" + unCaughtExceptionHandler.getClass().getName() + "");
            }

            @Override
            public void onActivityPaused(Activity activity) {

            }

            @Override
            public void onActivityStopped(Activity activity) {

            }

            @Override
            public void onActivitySaveInstanceState(Activity activity, Bundle bundle) {

            }

            @Override
            public void onActivityDestroyed(Activity activity) {

            }
        });
    }

    private static void startErroActivity(String straceMsg) {
        //Reduce data to 128KB so we don't get a TransactionTooLargeException when sending the intent.
        //The limit is 1MB on Android but some devices seem to have it lower.
        //See: http://developer.android.com/reference/android/os/TransactionTooLargeException.html
        //And: http://stackoverflow.com/questions/11451393/what-to-do-on-transactiontoolargeexception#comment46697371_12809171
        if (straceMsg.length() > MAX_STACK_TRACE_SIZE) {
            String disclaimer = " [stack trace too large]";
            straceMsg = straceMsg.substring(0, MAX_STACK_TRACE_SIZE - disclaimer.length()) + disclaimer;
        }

        //启动错误页面
        Intent intent = new Intent(application, ErroActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        intent.putExtra(EXTRA_STRACE_MESSAGE, straceMsg);
        application.startActivity(intent);
    }

    /**
     * 获取此 throwable 及其追踪信息
     *
     * @param thread
     * @param throwable
     * @return
     */
    private static String getThrowableStraceStr(Thread thread, Throwable throwable) {
        Log.e("CrashException", thread.getName() + "--Exception:" + throwable + "\n\n");
        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);
        throwable.printStackTrace(pw);//
        return sw.toString();
    }

    /**
     * 获取应用的启动页Activity
     * INTERNAL method used to get the default launcher activity for the app.
     * If there is no launchable activity, this returns null.
     *
     * @param context A valid context. Must not be null.
     * @return A valid activity class, or null if no suitable one is found
     */
    @SuppressWarnings("unchecked")
    public static Class<? extends Activity> getLauncherActivity(Context context) {
        Intent intent = context.getPackageManager().getLaunchIntentForPackage(context.getPackageName());
        if (intent != null) {
            try {
                return (Class<? extends Activity>) Class.forName(intent.getComponent().getClassName());
            } catch (ClassNotFoundException e) {
                //Should not happen, print it to the log!
                Log.e(TAG, "Failed when resolving the restart activity class via getLaunchIntentForPackage, stack trace follows!", e);
            }
        }
        return null;
    }


    /**
     * 结束进程
     * INTERNAL method that kills the current process. It is used after
     * restarting or killing the app.
     */
    private static void killCurrentProcess() {
        //杀死当前进程
        android.os.Process.killProcess(android.os.Process.myPid());
        System.exit(10);
    }
}

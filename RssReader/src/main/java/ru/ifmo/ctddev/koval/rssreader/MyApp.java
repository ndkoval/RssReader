package ru.ifmo.ctddev.koval.rssreader;

import android.app.AlarmManager;
import android.app.Application;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

public class MyApp extends Application {

    private static Context context;

    public static Context getAppContext() {
        return MyApp.context;
    }

    public static void restartAlarmManager() {
        AlarmManager am = (AlarmManager) getAppContext().getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(getAppContext(), AlarmReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(getAppContext(), 0, intent, PendingIntent.FLAG_CANCEL_CURRENT);
        am.cancel(pendingIntent);
        am.set(AlarmManager.RTC_WAKEUP, System.currentTimeMillis() + AlarmManager.INTERVAL_HALF_HOUR, pendingIntent);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        MyApp.context = getApplicationContext();
    }
}


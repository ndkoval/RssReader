package ru.ifmo.ctddev.koval.rssreader;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class AlarmReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        //starting update
        Intent serviceIntent = new Intent(context, UpdateFeedsIntentService.class);
        context.startService(serviceIntent);

        //set up next update time
        MyApp.restartAlarmManager();
    }
}

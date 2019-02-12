package com.lukmannudin.assosiate.searchmovie.Alarm;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class DailyReminder {
    private Context context;
    public DailyReminder(Context context){
        this.context = context;
    }

    public void enabledDailyAlarm(){
        AlarmReceiver alarmReceiver = new AlarmReceiver();
        alarmReceiver.setRepeatingAlarm(context);
    }

    public void disabledDailyAlarm(){
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);

            Intent updateServiceIntent = new Intent(context, AlarmReceiver.class);
            PendingIntent pendingUpdateIntent = PendingIntent.getService(context,
                    AlarmReceiver.ID_REPEATING,
                    updateServiceIntent, 0);

            // Cancel alarms
            try {
                alarmManager.cancel(pendingUpdateIntent);
                Log.i("cek", "disableReleaseAlarm: ");
            } catch (Exception e) {
                Log.i("Alarm", "AlarmManager update was not canceled. " + e.toString());
            }
        }
}

package com.lukmannudin.assosiate.searchmovie;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SwitchCompat;
import android.util.Log;
import android.widget.CompoundButton;
import android.widget.Toast;

import com.lukmannudin.assosiate.searchmovie.Alarm.AlarmReceiver;
import com.lukmannudin.assosiate.searchmovie.Alarm.ReleaseReminder;
import com.lukmannudin.assosiate.searchmovie.dao.Database.StatusHelper;

import java.util.ArrayList;
import java.util.List;

public class ReminderSetting extends AppCompatActivity {
    private StatusHelper statusHelper;
    private SwitchCompat scDaily, scRelease;
    private int DAILY_FIELD = 0;
    private int RELEASE_FIELD = 1;
    private Context context;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reminder_setting);
        scDaily = findViewById(R.id.scDailyReminder);
        scRelease = findViewById(R.id.scReleaseReminder);

        statusHelper = StatusHelper.getInstance(getApplicationContext());
        statusHelper.open();

        List<String> s = statusHelper.getAllStatus();
        Log.i("cekStatus","DAILY:"+s.get(0)+": RELEASE:"+s.get(1));

        if (!s.get(DAILY_FIELD).equals(String.valueOf(1))) {
            scDaily.setChecked(false);
        } else {
            scDaily.setChecked(true);
        }

        if (!s.get(RELEASE_FIELD).equals(String.valueOf(1))) {
            scRelease.setChecked(false);
        } else {
            scRelease.setChecked(true);
        }

        scDaily.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                List<String> sNew = statusHelper.getAllStatus();
                String sRelease = sNew.get(RELEASE_FIELD);
                List<String> sr = new ArrayList<>();
                if (isChecked) {
                    sr.add(0, String.valueOf(1));
                    sr.add(1,sRelease);
                    sr.add(2,String.valueOf(0));
                    statusHelper.updateStatus(sr);
                    Toast.makeText(ReminderSetting.this, "Daily Reminder ON", Toast.LENGTH_SHORT).show();
                } else {
                    sr.add(0, String.valueOf(0));
                    sr.add(1,sRelease);
                    sr.add(2,String.valueOf(0));
                    statusHelper.updateStatus(sr);
                    Toast.makeText(ReminderSetting.this, "Daily Reminder OFF", Toast.LENGTH_SHORT).show();
                }
                List<String> s = statusHelper.getAllStatus();
                Log.i("cekStatus","DAILY:"+s.get(0)+": RELEASE:"+s.get(1));
            }
        });
        context = this;
        final ReleaseReminder r = new ReleaseReminder(context);

        scRelease.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                List<String> sr = new ArrayList<>();
                List<String> sNew = statusHelper.getAllStatus();
                String sDaily = sNew.get(DAILY_FIELD);
                if (isChecked) {
                    sr.add(0,sDaily);
                    sr.add(1, String.valueOf(1));
                    sr.add(2,String.valueOf(0));
                    statusHelper.updateStatus(sr);
                    r.enableReleaseAlarm(context);
                    Toast.makeText(ReminderSetting.this, "Release Reminder ON", Toast.LENGTH_SHORT).show();
                    List<String> s = statusHelper.getAllStatus();
                    Log.i("cekStatus","DAILY:"+s.get(0)+": RELEASE:"+s.get(1)+" TOTAL ALARM:"+s.get(2));
                } else {
                    sr.add(0,sDaily);
                    sr.add(1, String.valueOf(0));
                    sr.add(2,String.valueOf(0));
                    statusHelper.updateStatus(sr);
                    List<String> sNewB = statusHelper.getAllStatus();

                    r.disableReleaseAlarm(Integer.valueOf(sNewB.get(2)));
                    Toast.makeText(ReminderSetting.this, "Release Reminder OFF", Toast.LENGTH_SHORT).show();
                }
                List<String> s = statusHelper.getAllStatus();
                Log.i("cekStatus","DAILY:"+s.get(0)+": RELEASE:"+s.get(1)+" TOTAL ALARM:"+s.get(2));

            }
        });
        List<String> sBab = statusHelper.getAllStatus();

        Log.i("AYEUNA","DAILY:"+sBab.get(0)+": RELEASE:"+sBab.get(1)+" TOTAL ALARM:"+sBab.get(2));
//        List<String> sr = new ArrayList<>();
//        sr.add(0,String.valueOf(1));
//        sr.add(1,String.valueOf(0));
//        statusHelper.updateStatus(sr);
//        List<String> s = statusHelper.getAllStatus();
//        Log.i("kunti",String.valueOf(s.get(0)));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        statusHelper.close();
    }
}

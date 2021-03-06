package com.lukmannudin.assosiate.searchmovie;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SwitchCompat;
import android.util.Log;
import android.view.MenuItem;
import android.widget.CompoundButton;
import android.widget.Toast;

import com.lukmannudin.assosiate.searchmovie.Alarm.DailyReminder;
import com.lukmannudin.assosiate.searchmovie.Alarm.ReleaseReminder;
import com.lukmannudin.assosiate.searchmovie.dao.Database.StatusHelper;

import java.util.ArrayList;
import java.util.List;

public class ReminderSetting extends AppCompatActivity {
    private StatusHelper statusHelper;
    private SwitchCompat scDaily, scRelease;
    private int DAILY_FIELD = 0;
    private int RELEASE_FIELD = 1;
    private int TOTAL_ALARM_FIELD = 2;
    private Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reminder_setting);
        scDaily = findViewById(R.id.scDailyReminder);
        scRelease = findViewById(R.id.scReleaseReminder);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Settings");
        statusHelper = StatusHelper.getInstance(getApplicationContext());
        statusHelper.open();

        List<String> s = statusHelper.getAllStatus();
        Log.i("cekStatus", "DAILY:" + s.get(0) + ": RELEASE:" + s.get(1));

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
        context = this;


        final DailyReminder rx = new DailyReminder(context);

        scDaily.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                List<String> sNew = statusHelper.getAllStatus();
                String sRelease = sNew.get(RELEASE_FIELD);
                String sAlarm = sNew.get(TOTAL_ALARM_FIELD);
                List<String> sr = new ArrayList<>();
                if (isChecked) {
                    sr.add(0, String.valueOf(1));
                    sr.add(1, sRelease);
                    sr.add(2, sAlarm);
                    statusHelper.updateStatus(sr);
                    rx.enabledDailyAlarm();
                    Toast.makeText(ReminderSetting.this, "Daily Reminder ON", Toast.LENGTH_SHORT).show();
                } else {
                    sr.add(0, String.valueOf(0));
                    sr.add(1, sRelease);
                    sr.add(2, sAlarm);
                    rx.disabledDailyAlarm();
                    statusHelper.updateStatus(sr);
                    Toast.makeText(ReminderSetting.this, "Daily Reminder OFF", Toast.LENGTH_SHORT).show();
                }
                List<String> s = statusHelper.getAllStatus();
                Log.i("cekStatus", "DAILY:" + s.get(0) + ": RELEASE:" + s.get(1));
            }
        });
        final ReleaseReminder r = new ReleaseReminder(context);

        scRelease.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                List<String> sr = new ArrayList<>();
                List<String> sNew = statusHelper.getAllStatus();
                String sDaily = sNew.get(DAILY_FIELD);
                if (isChecked) {
                    sr.add(0, sDaily);
                    sr.add(1, String.valueOf(1));
                    sr.add(2, String.valueOf(0));
                    statusHelper.updateStatus(sr);
                    r.enableReleaseAlarm(context);
                    Toast.makeText(ReminderSetting.this, "Release Reminder ON", Toast.LENGTH_SHORT).show();
                    List<String> s = statusHelper.getAllStatus();
                    Log.i("cekStatus", "DAILY:" + s.get(0) + ": RELEASE:" + s.get(1) + " TOTAL ALARM:" + s.get(2));
                } else {
                    sr.add(0, sDaily);
                    sr.add(1, String.valueOf(0));
                    sr.add(2, String.valueOf(0));
                    List<String> sNewB = statusHelper.getAllStatus();
                    statusHelper.updateStatus(sr);
                    r.disableReleaseAlarm(Integer.valueOf(sNewB.get(2)));
                    Toast.makeText(ReminderSetting.this, "Release Reminder OFF", Toast.LENGTH_SHORT).show();
                }
                List<String> s = statusHelper.getAllStatus();
                Log.i("cekStatus", "DAILY:" + s.get(0) + ": RELEASE:" + s.get(1) + " TOTAL ALARM:" + s.get(2));

            }
        });
        List<String> sBab = statusHelper.getAllStatus();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home){
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        statusHelper.close();
    }
}

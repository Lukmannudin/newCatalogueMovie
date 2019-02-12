package com.lukmannudin.assosiate.searchmovie.Alarm;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.lukmannudin.assosiate.searchmovie.BuildConfig;
import com.lukmannudin.assosiate.searchmovie.dao.Database.StatusHelper;
import com.lukmannudin.assosiate.searchmovie.dao.NowPlayingResponse;
import com.lukmannudin.assosiate.searchmovie.dao.ResultsItem;
import com.lukmannudin.assosiate.searchmovie.network.APIClient;
import com.lukmannudin.assosiate.searchmovie.network.MovieService;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.SingleObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class ReleaseReminder {
    private List<ResultsItem> data = new ArrayList<>();
    private Context context;
    private Disposable disposable;


    public ReleaseReminder(Context context) {
        this.context = context;
    }

    private void getData(final Context context) {
        MovieService movieService = APIClient.getClient()
                .create(MovieService.class);
        movieService.getUpComing(BuildConfig.API_KEY)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new SingleObserver<NowPlayingResponse>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        disposable = d;
                    }

                    @Override
                    public void onSuccess(NowPlayingResponse nowPlayingResponse) {
                        Log.i("sizeData", String.valueOf(nowPlayingResponse.getResults().size()));
                        processData(context, nowPlayingResponse.getResults());

                    }

                    @Override
                    public void onError(Throwable e) {

                    }
                });
    }

    private void processData(Context context, List<ResultsItem> resultsItems) {
        StatusHelper statusHelper;
        statusHelper = StatusHelper.getInstance(context);
        statusHelper.open();
        List<String> sr = new ArrayList<>();
        this.data = resultsItems;
        AlarmReceiver alarmReceiver;
        alarmReceiver = new AlarmReceiver();
        List<String> s = statusHelper.getAllStatus();
        Log.i("lol", String.valueOf(s.size()));
        sr.add(s.get(0));
        sr.add(s.get(1));
        sr.add(String.valueOf(resultsItems.size()));
        statusHelper.updateStatus(sr);
        alarmReceiver.setOneTimeAlarm(context,
                resultsItems);
    }

    public void enableReleaseAlarm(Context context) {
        getData(context);
    }

    public void disableReleaseAlarm(int totalAlarm) {
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);

        for (int i = 1; i < totalAlarm; i++) {
            Intent updateServiceIntent = new Intent(context, AlarmReceiver.class);
            PendingIntent pendingUpdateIntent = PendingIntent.getService(context,
                    data.get(i).getId(),
                    updateServiceIntent, 0);

            // Cancel alarms
            try {
                alarmManager.cancel(pendingUpdateIntent);
            } catch (Exception e) {
                Log.i("Alarm", "AlarmManager update was not canceled. " + e.toString());
            }
        }
    }

}

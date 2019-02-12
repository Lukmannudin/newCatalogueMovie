package com.lukmannudin.assosiate.searchmovie.Widget;

import android.app.job.JobParameters;
import android.app.job.JobService;
import android.appwidget.AppWidgetManager;
import android.content.Intent;
import android.util.Log;

public class UpdateWidgetService extends JobService {
    private static final String TOAST_ACTION = "com.dicoding.picodiploma.TOAST_ACTION";

    @Override
    public boolean onStartJob(JobParameters params) {
     try {
         Intent intent = new Intent(this.getApplicationContext(), ImageBannerWidget.class);
         intent.setAction(AppWidgetManager.ACTION_APPWIDGET_UPDATE);
         this.sendBroadcast(intent);
         jobFinished(params, false);
     } catch (Exception e){
         Log.i("ERROR",e.getLocalizedMessage());
     }

        return true;
    }

    @Override
    public boolean onStopJob(JobParameters params) {
        return false;
    }
}

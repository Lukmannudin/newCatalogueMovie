package com.lukmannudin.assosiate.searchmovie.Widget;

import android.app.job.JobParameters;
import android.app.job.JobService;
import android.appwidget.AppWidgetManager;
import android.content.Intent;

public class UpdateWidgetService extends JobService {
    private static final String TOAST_ACTION = "com.dicoding.picodiploma.TOAST_ACTION";

    @Override
    public boolean onStartJob(JobParameters params) {
        Intent intent = new Intent(this.getApplicationContext(), ImageBannerWidget.class);
        intent.setAction(AppWidgetManager.ACTION_APPWIDGET_UPDATE);
        this.sendBroadcast(intent);
        jobFinished(params, false);
        return true;

    }

    @Override
    public boolean onStopJob(JobParameters params) {
        return false;
    }
}

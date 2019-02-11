package com.lukmannudin.assosiate.searchmovie.Widget;

import android.app.PendingIntent;
import android.app.job.JobParameters;
import android.app.job.JobService;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Intent;
import android.net.Uri;
import android.widget.RemoteViews;

import com.lukmannudin.assosiate.searchmovie.R;

public class UpdateWidgetService extends JobService {
    private static final String TOAST_ACTION = "com.dicoding.picodiploma.TOAST_ACTION";
    @Override
    public boolean onStartJob(JobParameters params) {
        AppWidgetManager manager = AppWidgetManager.getInstance(this);
        ComponentName theWidget = new ComponentName(this, StackWidgetService.class);
        Intent intent = new Intent(this.getApplicationContext(), StackWidgetService.class);
        intent.setData(Uri.parse(intent.toUri(Intent.URI_INTENT_SCHEME)));
        RemoteViews views = new RemoteViews(this.getApplicationContext().getPackageName(), R.layout.image_banner_widget);
        views.setRemoteAdapter(R.id.stack_view, intent);
        views.setEmptyView(R.id.stack_view, R.id.empty_view);
        Intent toastIntent = new Intent(this.getApplicationContext(), ImageBannerWidget.class);
        toastIntent.setAction(UpdateWidgetService.TOAST_ACTION);
        toastIntent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, theWidget);
        intent.setData(Uri.parse(intent.toUri(Intent.URI_INTENT_SCHEME)));
        PendingIntent toastPendingIntent = PendingIntent.getBroadcast(this.getApplicationContext(), 0, toastIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        views.setPendingIntentTemplate(R.id.stack_view, toastPendingIntent);
        manager.updateAppWidget(theWidget, views);
        jobFinished(params, false);
        return true;
    }

    @Override
    public boolean onStopJob(JobParameters params) {
        return false;
    }
}

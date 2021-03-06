package com.lukmannudin.assosiate.searchmovie.Widget;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.lukmannudin.assosiate.searchmovie.R;
import com.lukmannudin.assosiate.searchmovie.dao.Database.FavoriteHelper;
import com.lukmannudin.assosiate.searchmovie.dao.Model.Movie;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class StackRemoteViewsFactory implements RemoteViewsService.RemoteViewsFactory {
    private final List<Movie> mWidgetItems = new ArrayList<>();
    private final Context mContext;
    private FavoriteHelper favoriteHelper;

    public StackRemoteViewsFactory(Context mContext) {
        this.mContext = mContext;
    }

    @Override
    public void onCreate() {
        favoriteHelper = FavoriteHelper.getInstance(mContext);
        favoriteHelper.open();
    }

    @Override
    public void onDataSetChanged() {
        mWidgetItems.clear();
        List<Movie> mv = favoriteHelper.getAllFavorite();
        //Saya sengaja memasukan data initial kosong ini karena jika data kosong pada perangkat saya samsung UI/Experience nya force close
        if (mv.size() == 0) {
            Movie m = new Movie();
            m.setPosterPath("");
            m.setOriginalLanguage("");
            m.setVoteAverage(0.0);
            m.setPopularity(0.0);
            m.setOriginalLanguage("");
            m.setOverview("");
            m.setReleaseDate("");
            mWidgetItems.add(m);
        } else {
            mWidgetItems.addAll(mv);
        }
    }

    @Override
    public void onDestroy() {

    }

    @Override
    public int getCount() {
        return mWidgetItems.size();
    }

    @Override
    public RemoteViews getViewAt(int position) {
        RemoteViews rv = new RemoteViews(mContext.getPackageName(), R.layout.widget_item);
        if (mWidgetItems.size() > 0) {
            try {
                Bitmap preview = Glide.with(mContext)
                        .asBitmap()
                        .load("https://image.tmdb.org/t/p/w500/" + mWidgetItems.get(position).getPosterPath())
                        .apply(new RequestOptions().fitCenter())
                        .submit()
                        .get();
                rv.setImageViewBitmap(R.id.imageView, preview);
                Bundle extras = new Bundle();
                extras.putInt(ImageBannerWidget.EXTRA_ITEM, position);
                Intent fillInIntent = new Intent();
                fillInIntent.putExtras(extras);
                rv.setOnClickFillInIntent(R.id.imageView, fillInIntent);
                return rv;
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    @Override
    public RemoteViews getLoadingView() {
        return null;
    }

    @Override
    public int getViewTypeCount() {
        return 1;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

}

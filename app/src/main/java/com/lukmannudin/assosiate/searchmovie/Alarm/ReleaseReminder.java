package com.lukmannudin.assosiate.searchmovie.Alarm;

import android.content.Context;

import com.lukmannudin.assosiate.searchmovie.BuildConfig;
import com.lukmannudin.assosiate.searchmovie.dao.NowPlayingResponse;
import com.lukmannudin.assosiate.searchmovie.dao.ResultsItem;
import com.lukmannudin.assosiate.searchmovie.network.APIClient;
import com.lukmannudin.assosiate.searchmovie.network.MovieService;

import java.util.List;

import io.reactivex.SingleObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class ReleaseReminder {

    private int idDataReleaseCode[] = new int[5];
    private void getData(final Context context) {
        MovieService movieService = APIClient.getClient()
                .create(MovieService.class);
        movieService.getUpComing(BuildConfig.API_KEY)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new SingleObserver<NowPlayingResponse>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onSuccess(NowPlayingResponse nowPlayingResponse) {
                        processData(context, nowPlayingResponse.getResults());
                    }

                    @Override
                    public void onError(Throwable e) {

                    }
                });
    }

    private void processData(Context context, List<ResultsItem> resultsItems) {
        AlarmReceiver alarmReceiver;
        alarmReceiver = new AlarmReceiver();
        alarmReceiver.setOneTimeAlarm(context,
                resultsItems);
    }

}

package com.lukmannudin.assosiate.searchmovie.main.now_playing_fragment;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.lukmannudin.assosiate.searchmovie.BuildConfig;
import com.lukmannudin.assosiate.searchmovie.R;
import com.lukmannudin.assosiate.searchmovie.Utils;
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

/**
 * A simple {@link Fragment} subclass.
 */
public class NowPlayngFragment extends Fragment {
    private ProgressBar loading;
    RecyclerView movieRecyclerView;
    private static final String KEY_MOVIES = "KEY_MOVIES";
    private ArrayList<ResultsItem> data = new ArrayList<>();
    NowPlayingAdapter adapter;
    List<ResultsItem> data2;
    private int pageId;
    private Disposable disposable;
    public NowPlayngFragment() {
        // Required empty public constructor
    }

    public static NowPlayngFragment newInstance(int pageId){
        NowPlayngFragment fragment = new NowPlayngFragment();
        Bundle args = new Bundle();
        args.putInt(Utils.page,pageId);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState != null) {
            data = savedInstanceState.getParcelableArrayList(KEY_MOVIES);
            data2 = new ArrayList<>(data);
        }

        if (getArguments() != null) {
            pageId = getArguments().getInt(Utils.page);
        }
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        outState.putParcelableArrayList(KEY_MOVIES, data);
        super.onSaveInstanceState(outState);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_now_playng, container, false);
        movieRecyclerView = view.findViewById(R.id.rvMovie);
        loading = view.findViewById(R.id.mainProggressbar);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
        movieRecyclerView.setLayoutManager(layoutManager);
        if (data2 != null){
            adapter = new NowPlayingAdapter(getContext(), data2,pageId);
            movieRecyclerView.setAdapter(adapter);
            adapter.notifyDataSetChanged();
        } else {
            getData();
        }
        return view;
    }

    private void getData() {
        loading.setVisibility(View.VISIBLE);
        MovieService movieService = APIClient.getClient()
                .create(MovieService.class);

          movieService.getNowPlaying(BuildConfig.API_KEY)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new SingleObserver<NowPlayingResponse>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        disposable = d;
                    }

                    @Override
                    public void onSuccess(NowPlayingResponse nowPlayingResponse) {
                        processData(nowPlayingResponse.getResults());
                    }

                    @Override
                    public void onError(Throwable e) {

                    }
                });

    }

    private void processData(List<ResultsItem> resultsItems) {
        data.addAll(resultsItems);
        adapter = new NowPlayingAdapter(getContext(), resultsItems,pageId);
        movieRecyclerView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
        loading.setVisibility(View.GONE);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        disposable.dispose();
    }
}

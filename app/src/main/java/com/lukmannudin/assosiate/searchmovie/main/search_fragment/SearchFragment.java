package com.lukmannudin.assosiate.searchmovie.main.search_fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;

import com.lukmannudin.assosiate.searchmovie.BuildConfig;
import com.lukmannudin.assosiate.searchmovie.R;
import com.lukmannudin.assosiate.searchmovie.Utils;
import com.lukmannudin.assosiate.searchmovie.dao.Model.MovieTrending;
import com.lukmannudin.assosiate.searchmovie.dao.Response;
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
public class SearchFragment extends Fragment {
    private List<MovieTrending> movieTrendingList = new ArrayList<>();
    private SearchAdapter adapter;
    private EditText edtSearch;
    private ProgressBar loading;
    private int pageId;


    public SearchFragment() {
        // Required empty public constructor
    }

    public static SearchFragment newInstance(int pageId) {
        SearchFragment fragment = new SearchFragment();
        Bundle args = new Bundle();
        args.putInt(Utils.page, pageId);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            pageId = getArguments().getInt(Utils.page);
        }
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_search, container, false);

        RecyclerView movieRecyclerView = view.findViewById(R.id.rvMovie);
        loading = view.findViewById(R.id.mainProggressbar);
        Button btnSearch = view.findViewById(R.id.btnSearch);
        edtSearch = view.findViewById(R.id.edtSearchText);
        getData();
        adapter = new SearchAdapter(movieTrendingList);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
        movieRecyclerView.setLayoutManager(layoutManager);
        movieRecyclerView.setAdapter(adapter);

        edtSearch.clearFocus();

        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String queryData = edtSearch.getText().toString();
                Log.i("querySearch", queryData);
                searchData(queryData);
            }
        });

        return view;
    }

    private void getData() {
        loading.setVisibility(View.VISIBLE);
        MovieService movieService = APIClient.getClient()
                .create(MovieService.class);
        movieService.getMovieTrending(BuildConfig.API_KEY)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new SingleObserver<Response<MovieTrending>>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onSuccess(Response<MovieTrending> movieResponse) {
                        processData(movieResponse.getResults());
                    }

                    @Override
                    public void onError(Throwable e) {

                    }
                });
    }

    private void processData(List<MovieTrending> data) {
        movieTrendingList.clear();
        movieTrendingList.addAll(data);
        loading.setVisibility(View.GONE);
        adapter.notifyDataSetChanged();
    }

    private void searchData(String queryData){
        loading.setVisibility(View.VISIBLE);
        MovieService movieService = APIClient.getClient()
                .create(MovieService.class);
        movieService.searchMovie(BuildConfig.API_KEY, queryData)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new SingleObserver<Response<MovieTrending>>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onSuccess(Response<MovieTrending> movieResponse) {
                        processData(movieResponse.getResults());
                    }

                    @Override
                    public void onError(Throwable e) {

                    }
                });
//        loading.setVisibility(View.GONE);
    }
}

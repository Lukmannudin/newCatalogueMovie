package com.lukmannudin.assosiate.searchmovie.detail;

import android.app.job.JobInfo;
import android.app.job.JobScheduler;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.lukmannudin.assosiate.searchmovie.BuildConfig;
import com.lukmannudin.assosiate.searchmovie.R;
import com.lukmannudin.assosiate.searchmovie.Utils;
import com.lukmannudin.assosiate.searchmovie.Widget.UpdateWidgetService;
import com.lukmannudin.assosiate.searchmovie.dao.Database.FavoriteHelper;
import com.lukmannudin.assosiate.searchmovie.dao.Model.Genre;
import com.lukmannudin.assosiate.searchmovie.dao.Model.Movie;
import com.lukmannudin.assosiate.searchmovie.main.MainActivity;
import com.lukmannudin.assosiate.searchmovie.network.APIClient;
import com.lukmannudin.assosiate.searchmovie.network.MovieService;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import de.hdodenhof.circleimageview.CircleImageView;
import io.reactivex.SingleObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class MovieDetail extends AppCompatActivity {
    private MovieDetailAdapter adapter;
    private int movieId;
    private String KEY_DATA = "key_data";
    TextView movieName, movieScore, movieDuration, movieLang, movieOverview, movieRelease;
    CircleImageView posterImage;
    RecyclerView rvGenres;
    private List<Genre> genreList = new ArrayList<>();
    private Movie movieData = new Movie();
    private FavoriteHelper favoriteHelper;
    private Boolean isFavorite = false;
    private Menu menuItem = null;
    private int pageId;
    private Disposable disposable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.movie_detail);
        posterImage = findViewById(R.id.ciPosterImage);
        movieName = findViewById(R.id.tvMovieName);
        movieScore = findViewById(R.id.movieScore);
        movieDuration = findViewById(R.id.movieDuration);
        movieLang = findViewById(R.id.movieLanguage);
        movieOverview = findViewById(R.id.tvOverview);
        movieRelease = findViewById(R.id.tvRelease);
        rvGenres = findViewById(R.id.rvGenres);
        adapter = new MovieDetailAdapter(genreList);
        favoriteHelper = FavoriteHelper.getInstance(getApplicationContext());
        favoriteHelper.open();
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        rvGenres.setLayoutManager(layoutManager);
        rvGenres.setAdapter(adapter);

        movieId = getIntent().getIntExtra("movieId", 0);

        try {
            if (favoriteHelper.favoriteState(movieId)) {
                isFavorite = true;
            }
        } catch (Exception e) {
            Log.i("ERROR", e.getLocalizedMessage());
        }

        pageId = getIntent().getIntExtra(Utils.page, 0);
        Log.i("fra detail", String.valueOf(pageId));

        String movieTitle = getIntent().getStringExtra("movieTitle");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(movieTitle);
        layoutVisible(false);
        Log.i("cikok", String.valueOf(pageId));
        if (savedInstanceState != null) {
            movieData = savedInstanceState.getParcelable(KEY_DATA);
            genreList.addAll(movieData.getGenres());
            layoutVisible(true);
            adapter.notifyDataSetChanged();
            initView(movieData);
        } else {
            getData();
        }
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        outState.putParcelable(KEY_DATA, movieData);
        super.onSaveInstanceState(outState);
    }

    void getData() {
        MovieService movieService = APIClient.getClient()
                .create(MovieService.class);
        movieService.getMovie(movieId, BuildConfig.API_KEY)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new SingleObserver<Movie>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        disposable = d;
                    }

                    @Override
                    public void onSuccess(Movie movie) {
                        processData(movie);
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.i("detail", e.getLocalizedMessage());
                    }
                });
    }

    void processData(Movie data) {
        genreList.clear();
        genreList.addAll(data.getGenres());
        layoutVisible(true);
        adapter.notifyDataSetChanged();
        initView(data);
        movieData = data;
    }

    void layoutVisible(Boolean status) {
        ProgressBar loadingView = findViewById(R.id.movieDetailProggresbar);
        if (!status) {
            loadingView.setVisibility(View.VISIBLE);
            movieName.setVisibility(View.GONE);
            movieScore.setVisibility(View.GONE);
            movieDuration.setVisibility(View.GONE);
            movieLang.setVisibility(View.GONE);
            movieOverview.setVisibility(View.GONE);
            movieRelease.setVisibility(View.GONE);
            posterImage.setVisibility(View.GONE);
        } else {
            loadingView.setVisibility(View.GONE);
            movieName.setVisibility(View.VISIBLE);
            movieScore.setVisibility(View.VISIBLE);
            movieDuration.setVisibility(View.VISIBLE);
            movieLang.setVisibility(View.VISIBLE);
            movieOverview.setVisibility(View.VISIBLE);
            movieRelease.setVisibility(View.VISIBLE);
            posterImage.setVisibility(View.VISIBLE);

        }
    }


    void initView(Movie movie) {
        Glide.with(this)
                .load("https://image.tmdb.org/t/p/w185/" + movie.getPosterPath())
                .into(posterImage);
        movieName.setText(movie.getOriginalTitle());
        movieScore.setText(String.valueOf(movie.getVoteAverage()));
        movieDuration.setText(String.valueOf(movie.getPopularity()));
        movieLang.setText(movie.getOriginalLanguage());
        movieOverview.setText(movie.getOverview());
        movieRelease.setText(movie.getReleaseDate());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_detail, menu);
        menuItem = menu;
        setFavorite();
        return true;
    }

    private void removeFromFavorite() {
        favoriteHelper.deleteFavorite(movieId);
        Toast.makeText(this, "Removed from Favorite", Toast.LENGTH_SHORT).show();
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                Intent i = new Intent(this, MainActivity.class);
                i.putExtra(Utils.page, pageId);
                Log.i("cik", String.valueOf(pageId));
                startActivity(i);
                break;

            case R.id.favorite: {
                if (isFavorite) {
                    try {
                        removeFromFavorite();
                    } catch (Exception e) {
                        Log.i("cekidot",e.getLocalizedMessage());
                    }
                } else {
                    favoriteHelper.insertFavorite(movieData);
                    Toast.makeText(this, "Added to Favorite", Toast.LENGTH_SHORT).show();
                }
                isFavorite = !isFavorite;
                setFavorite();
                startJob();
                break;
            }
        }
        return true;
    }

    private static int jobId = 100;
    private static int SCHEDULE_OF_PERIOD = 10;

    private void startJob() {
        ComponentName mServiceComponent = new ComponentName(this, UpdateWidgetService.class);
        JobInfo.Builder builder = new JobInfo.Builder(jobId, mServiceComponent);
        builder.setRequiredNetworkType(JobInfo.NETWORK_TYPE_NONE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            builder.setMinimumLatency(SCHEDULE_OF_PERIOD);
        } else {
            builder.setPeriodic(SCHEDULE_OF_PERIOD);
        }
        JobScheduler jobScheduler = (JobScheduler)getSystemService(Context.JOB_SCHEDULER_SERVICE);
        Objects.requireNonNull(jobScheduler).schedule(builder.build());
//        Toast.makeText(this, "Job Service started", Toast.LENGTH_SHORT).show();
    }

    private void setFavorite() {
        if (isFavorite) {
            menuItem.getItem(0).setIcon(ContextCompat.getDrawable(this, R.drawable.ic_favorite_black_24dp));
        } else {
            menuItem.getItem(0).setIcon(ContextCompat.getDrawable(this, R.drawable.ic_favorite_white_24dp));
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        favoriteHelper.close();
        if (disposable!= null){
            disposable.dispose();
        }
    }
}

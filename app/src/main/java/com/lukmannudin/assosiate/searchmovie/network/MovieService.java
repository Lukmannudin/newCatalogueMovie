package com.lukmannudin.assosiate.searchmovie.network;


import com.lukmannudin.assosiate.searchmovie.dao.Model.Movie;
import com.lukmannudin.assosiate.searchmovie.dao.Model.MovieTrending;
import com.lukmannudin.assosiate.searchmovie.dao.NowPlayingResponse;
import com.lukmannudin.assosiate.searchmovie.dao.Response;

import java.util.Locale;

import io.reactivex.Single;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface MovieService {

    @GET("trending/movie/week")
    Single<Response<MovieTrending>> getMovieTrending(
            @Query("api_key") String api_key
    );

    @GET("search/movie")
    Single<Response<MovieTrending>> searchMovie(
            @Query("api_key") String api_key,
            @Query("query") String query
    );

    @GET("movie/{movieId}")
    Single<Movie> getMovie(
            @Path("movieId") int movieId,
            @Query("api_key") String api_key
    );

    @GET("movie/now_playing")
    Single<NowPlayingResponse> getNowPlaying(
            @Query("api_key") String api_key
    );

    @GET("movie/upcoming")
    Single<NowPlayingResponse> getUpComing(
            @Query("api_key") String api_key
    );

}

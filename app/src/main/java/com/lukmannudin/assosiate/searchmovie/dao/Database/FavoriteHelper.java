package com.lukmannudin.assosiate.searchmovie.dao.Database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.lukmannudin.assosiate.searchmovie.dao.Model.Genre;
import com.lukmannudin.assosiate.searchmovie.dao.Model.Movie;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static android.provider.BaseColumns._ID;
import static com.lukmannudin.assosiate.searchmovie.dao.Database.DatabaseContract.FavoriteColumns.GENRES;
import static com.lukmannudin.assosiate.searchmovie.dao.Database.DatabaseContract.FavoriteColumns.IMAGE_PATH;
import static com.lukmannudin.assosiate.searchmovie.dao.Database.DatabaseContract.FavoriteColumns.LANGUAGE;
import static com.lukmannudin.assosiate.searchmovie.dao.Database.DatabaseContract.FavoriteColumns.OVERVIEW;
import static com.lukmannudin.assosiate.searchmovie.dao.Database.DatabaseContract.FavoriteColumns.POPULARITY;
import static com.lukmannudin.assosiate.searchmovie.dao.Database.DatabaseContract.FavoriteColumns.RATING;
import static com.lukmannudin.assosiate.searchmovie.dao.Database.DatabaseContract.FavoriteColumns.RELEASE;
import static com.lukmannudin.assosiate.searchmovie.dao.Database.DatabaseContract.FavoriteColumns.TITLE;
import static com.lukmannudin.assosiate.searchmovie.dao.Database.DatabaseContract.TABLE_FAVORITE;

public class FavoriteHelper {
    private static final String DATABASE_TABLE = TABLE_FAVORITE;
    private static DatabaseHelper dataBaseHelper;
    private static FavoriteHelper INSTANCE;
    private static SQLiteDatabase database;
    private static String movieId;

    private FavoriteHelper(Context context) {
        dataBaseHelper = new DatabaseHelper(context);
    }

    public static FavoriteHelper getInstance(Context context) {
        if (INSTANCE == null) {
            synchronized (SQLiteOpenHelper.class) {
                if (INSTANCE == null) {
                    INSTANCE = new FavoriteHelper(context);
                }
            }
        }
        return INSTANCE;
    }

    public void open() throws SQLException {
        database = dataBaseHelper.getWritableDatabase();
    }

    public void close() {
        database.close();
        if (database.isOpen()) {
            database.close();
        }
    }

    public ArrayList<Movie> getAllFavorite() {
        ArrayList<Movie> arrayList = new ArrayList<>();
        Cursor cursor = database.query(DATABASE_TABLE, null,
                null,
                null,
                null,
                null,
                _ID + " ASC",
                null);
        cursor.moveToFirst();
        Movie movie;
        if (cursor.getCount() > 0) {
            do {
                movie = new Movie();
                String sb = cursor.getString(cursor.getColumnIndexOrThrow(GENRES));

                List<String> s = Arrays.asList(sb.split("\\s*,\\s*"));
                List<Genre> gList = new ArrayList<>();
                if (s.size()>0){
                    for (int i=1;i<s.size();i++){
                        gList.add(new Genre(s.get(i)));
                    }
                }

                movie.setId(cursor.getInt(cursor.getColumnIndexOrThrow(_ID)));
                movie.setTitle(cursor.getString(cursor.getColumnIndexOrThrow(TITLE)));
                movie.setPosterPath(cursor.getString(cursor.getColumnIndexOrThrow(IMAGE_PATH)));
                movie.setVoteAverage(Double.valueOf(cursor.getString(cursor.getColumnIndexOrThrow(RATING))));
                movie.setPopularity(Double.valueOf(cursor.getString(cursor.getColumnIndexOrThrow(POPULARITY))));
                movie.setOriginalLanguage(cursor.getString(cursor.getColumnIndexOrThrow(LANGUAGE)));
                movie.setGenres(gList);
                movie.setReleaseDate(cursor.getString(cursor.getColumnIndexOrThrow(RELEASE)));
                movie.setOverview(cursor.getString(cursor.getColumnIndexOrThrow(OVERVIEW)));
                arrayList.add(movie);
                cursor.moveToNext();
            } while (!cursor.isAfterLast());
        }
        cursor.close();
        return arrayList;
    }

    public Boolean favoriteState(int movieId){
        ArrayList<Movie> arrayList = new ArrayList<>();
        Cursor cursor = database.query(DATABASE_TABLE, null,
                _ID + " = ?",
                new String[] { String.valueOf(movieId) },
                null,
                null,
                _ID + " ASC",
                null);

        cursor.moveToFirst();
        return cursor.getCount() > 0;
    }

    public long insertFavorite(Movie movie) {
        ContentValues args = new ContentValues();
        args.put(_ID, movie.getId());
        args.put(TITLE, movie.getTitle());
        args.put(IMAGE_PATH, movie.getPosterPath());
        args.put(RATING, movie.getVoteAverage());
        args.put(POPULARITY, movie.getPopularity());
        args.put(LANGUAGE, movie.getOriginalLanguage());

        String genreSeparated = "";

        for (int i = 0; i < movie.getGenres().size(); i++) {
            genreSeparated = genreSeparated + movie.getGenres().get(i).getName() + ",";
        }

        args.put(GENRES, genreSeparated);
        args.put(RELEASE, movie.getReleaseDate());
        args.put(OVERVIEW, movie.getOverview());
        return database.insert(DATABASE_TABLE, null, args);
    }



    public int deleteFavorite(int id) {
        return database.delete(TABLE_FAVORITE, _ID + " = '" + id + "'", null);
    }
}

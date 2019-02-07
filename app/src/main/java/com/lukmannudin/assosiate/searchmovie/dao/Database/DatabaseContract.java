package com.lukmannudin.assosiate.searchmovie.dao.Database;

import android.provider.BaseColumns;

public class DatabaseContract {
    static String TABLE_FAVORITE = "favorites";

    static final class FavoriteColumns implements BaseColumns {
        static String TITLE = "title";
        static String IMAGE_PATH = "image_path";
        static String RATING = "rating";
        static String POPULARITY = "popularity";
        static String LANGUAGE = "language";
        static String GENRES = "genres";
        static String RELEASE = "release";
        static String OVERVIEW = "overview";
    }
}
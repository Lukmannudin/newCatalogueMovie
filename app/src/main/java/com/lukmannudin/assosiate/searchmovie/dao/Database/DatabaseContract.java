package com.lukmannudin.assosiate.searchmovie.dao.Database;

import android.provider.BaseColumns;

public class DatabaseContract {
    static String TABLE_FAVORITE = "favorites";
    static String TABLE_STATUS = "status";

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

    static String STATUS_RELEASE_FIELD = "status_release";
    static String STATUS_DAILY_FIELD = "status_daily";

    static final class StatusColumns implements BaseColumns {
        static String STATUS_RELEASE_REMINDER = "status_release";
        static String STATUS_DAILY_REMINDER = "status_daily";
        static String TOTAL_ALARM = "total_alarm";
    }
}
package com.lukmannudin.assosiate.searchmovie.dao.Database;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import static android.provider.BaseColumns._ID;
import static com.lukmannudin.assosiate.searchmovie.dao.Database.DatabaseContract.StatusColumns.STATUS_DAILY_REMINDER;
import static com.lukmannudin.assosiate.searchmovie.dao.Database.DatabaseContract.StatusColumns.STATUS_RELEASE_REMINDER;
import static com.lukmannudin.assosiate.searchmovie.dao.Database.DatabaseContract.StatusColumns.TOTAL_ALARM;
import static com.lukmannudin.assosiate.searchmovie.dao.Database.DatabaseContract.TABLE_STATUS;

public class DatabaseHelper extends SQLiteOpenHelper {

    public static String DATABASE_NAME = "dbcataloguemovie";
    private static final int DATABASE_VERSION = 1;
    private static final String SQL_CREATE_TABLE_FAVORITE = String.format("CREATE TABLE %s"
                    + " (" +
                    " %s TEXT NOT NULL," +
                    " %s TEXT NOT NULL," +
                    " %s TEXT NOT NULL," +
                    " %s TEXT NOT NULL," +
                    " %s TEXT NOT NULL," +
                    " %s TEXT NOT NULL," +
                    " %s TEXT NOT NULL," +
                    " %s TEXT NOT NULL," +
                    " %s TEXT NOT NULL)",
            DatabaseContract.TABLE_FAVORITE,
            DatabaseContract.FavoriteColumns._ID,
            DatabaseContract.FavoriteColumns.TITLE,
            DatabaseContract.FavoriteColumns.IMAGE_PATH,
            DatabaseContract.FavoriteColumns.RATING,
            DatabaseContract.FavoriteColumns.POPULARITY,
            DatabaseContract.FavoriteColumns.LANGUAGE,
            DatabaseContract.FavoriteColumns.GENRES,
            DatabaseContract.FavoriteColumns.RELEASE,
            DatabaseContract.FavoriteColumns.OVERVIEW
    );

    private static final String SQL_CREATE_TABLE_STATUS = String.format("CREATE TABLE %s"
                    + " (" +
                    " %s INTEGER NOT NULL," +
                    " %s INTEGER NOT NULL," +
                    " %s INTEGER NOT NULL," +
                    " %s INTEGER NOT NULL)",
            TABLE_STATUS,
            DatabaseContract.FavoriteColumns._ID,
            DatabaseContract.StatusColumns.STATUS_DAILY_REMINDER,
            DatabaseContract.StatusColumns.STATUS_RELEASE_REMINDER,
            DatabaseContract.StatusColumns.TOTAL_ALARM
            );

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        db.execSQL(SQL_CREATE_TABLE_FAVORITE);
        db.execSQL(SQL_CREATE_TABLE_STATUS);

        ContentValues args = new ContentValues();
        args.put(_ID,1);
        args.put(STATUS_DAILY_REMINDER, 0);
        args.put(STATUS_RELEASE_REMINDER, 0);
        args.put(TOTAL_ALARM,0);
        db.insert(TABLE_STATUS,null,args );

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + DatabaseContract.TABLE_FAVORITE);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_STATUS);

        onCreate(db);
    }
}

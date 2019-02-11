package com.lukmannudin.assosiate.searchmovie.dao.Database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.lukmannudin.assosiate.searchmovie.dao.Model.Movie;

import java.util.ArrayList;
import java.util.List;

import static android.provider.BaseColumns._ID;
import static com.lukmannudin.assosiate.searchmovie.dao.Database.DatabaseContract.StatusColumns.STATUS_DAILY_REMINDER;
import static com.lukmannudin.assosiate.searchmovie.dao.Database.DatabaseContract.StatusColumns.STATUS_RELEASE_REMINDER;
import static com.lukmannudin.assosiate.searchmovie.dao.Database.DatabaseContract.StatusColumns.TOTAL_ALARM;
import static com.lukmannudin.assosiate.searchmovie.dao.Database.DatabaseContract.TABLE_FAVORITE;
import static com.lukmannudin.assosiate.searchmovie.dao.Database.DatabaseContract.TABLE_STATUS;


public class StatusHelper {
    private static final String DATABASE_TABLE = TABLE_STATUS;
    private static DatabaseHelper dataBaseHelper;
    private static StatusHelper INSTANCE;
    private static SQLiteDatabase database;
    private static String movieId;

    private StatusHelper(Context context) {
        dataBaseHelper = new DatabaseHelper(context);
    }

    public static StatusHelper getInstance(Context context) {
        if (INSTANCE == null) {
            synchronized (SQLiteOpenHelper.class) {
                if (INSTANCE == null) {
                    INSTANCE = new StatusHelper(context);
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

    public List<String> getAllStatus() {
        ArrayList<String> arrayList = new ArrayList<>();
        Cursor cursor = database.query(DATABASE_TABLE, null,
                null,
                null,
                null,
                null,
                null,
                null);
        cursor.moveToFirst();
        if (cursor.getCount() > 0) {
            do {
                arrayList.add(0, cursor.getString(cursor.getColumnIndexOrThrow(STATUS_DAILY_REMINDER)));
                arrayList.add(1, cursor.getString(cursor.getColumnIndexOrThrow(STATUS_RELEASE_REMINDER)));
                arrayList.add(2, cursor.getString(cursor.getColumnIndexOrThrow(TOTAL_ALARM)));
                cursor.moveToNext();
            } while (!cursor.isAfterLast());
        }
        cursor.close();
        return arrayList;
    }

    public Boolean favoriteState(int movieId) {
        ArrayList<Movie> arrayList = new ArrayList<>();
        Cursor cursor = database.query(DATABASE_TABLE, null,
                _ID + " = 1",
                null,
                null,
                null,
                _ID + " ASC",
                null);

        cursor.moveToFirst();
        return cursor.getCount() > 0;
    }

    public long insertStatus() {
        ContentValues args = new ContentValues();
        args.put(_ID, 1);
        args.put(STATUS_DAILY_REMINDER, 0);
        args.put(STATUS_RELEASE_REMINDER, 0);
        return database.insert(DATABASE_TABLE, null, args);
    }

    public long updateStatus(List<String> status) {
        Log.i("cekStatusDatabase", "DAILY:" + status.get(0) + " RELEASE:" + status.get(1) + " ALARM TOTAL:"+status.get(2));
        ContentValues args = new ContentValues();
        args.put(STATUS_DAILY_REMINDER, status.get(0));
        args.put(STATUS_RELEASE_REMINDER, status.get(1));
        args.put(TOTAL_ALARM, status.get(2));
        return database.update(DATABASE_TABLE, args, _ID + "=1", null);
    }


    public int deleteFavorite(int id) {
        return database.delete(TABLE_FAVORITE, _ID + " = '" + id + "'", null);
    }
}

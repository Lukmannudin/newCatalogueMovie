package com.lukmannudin.assosiate.searchmovie.Provider;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.lukmannudin.assosiate.searchmovie.dao.Database.FavoriteHelper;

import static com.lukmannudin.assosiate.searchmovie.dao.Database.DatabaseContract.AUTHORITY;
import static com.lukmannudin.assosiate.searchmovie.dao.Database.DatabaseContract.FavoriteColumns.CONTENT_URI;
import static com.lukmannudin.assosiate.searchmovie.dao.Database.DatabaseContract.TABLE_FAVORITE;

public class CatalogueMovieProvider extends ContentProvider {
    private static final int FAVORITE = 1;
    private static final int FAVORITE_ID = 2;
    private static final UriMatcher sUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
    private FavoriteHelper favoriteHelper;

    static {
        sUriMatcher.addURI(AUTHORITY, TABLE_FAVORITE, FAVORITE);
        sUriMatcher.addURI(AUTHORITY, TABLE_FAVORITE + "/#", FAVORITE_ID);
    }

    @Override
    public boolean onCreate() {
        favoriteHelper = FavoriteHelper.getInstance(getContext());
        return true;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] strings, @Nullable String s, @Nullable String[] strings1, @Nullable String s1) {
        favoriteHelper.open();
        Cursor cursor;
        switch (sUriMatcher.match(uri)) {
            case FAVORITE:
                cursor = favoriteHelper.queryProvider();
                break;
            case FAVORITE_ID:
                cursor = favoriteHelper.queryByIdProvider(uri.getLastPathSegment());
                break;
            default:
                cursor = null;
                break;
        }
        return cursor;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        return null;
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues contentValues) {
        favoriteHelper.open();
        long added;
        switch (sUriMatcher.match(uri)) {
            case FAVORITE:
                added = favoriteHelper.insertProvider(contentValues);
                break;
            default:
                added = 0;
                break;
        }
        return Uri.parse(CONTENT_URI + "/" + added);
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String s, @Nullable String[] strings) {
        favoriteHelper.open();
        int deleted;
        switch (sUriMatcher.match(uri)) {
            case FAVORITE_ID:
                deleted = favoriteHelper.deleteProvider(uri.getLastPathSegment());
                break;
            default:
                deleted = 0;
                break;
        }
        return deleted;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues contentValues, @Nullable String s, @Nullable String[] strings) {
        favoriteHelper.open();
        int updated;
        switch (sUriMatcher.match(uri)) {
            case FAVORITE_ID:
                updated = favoriteHelper.updateProvider(uri.getLastPathSegment(), contentValues);
                break;
            default:
                updated = 0;
                break;
        }
        return updated;
    }
}

package com.lukmannudin.assosiate.searchmovie.main.Favorites;

import android.database.Cursor;

interface LoadFavoritesCallback {
    void postExecute(Cursor movies);
}

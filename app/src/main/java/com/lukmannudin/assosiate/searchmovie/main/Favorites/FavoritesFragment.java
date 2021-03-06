package com.lukmannudin.assosiate.searchmovie.main.Favorites;


import android.database.Cursor;
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
import android.widget.ProgressBar;

import com.lukmannudin.assosiate.searchmovie.Helper.MappingHelper;
import com.lukmannudin.assosiate.searchmovie.R;
import com.lukmannudin.assosiate.searchmovie.Utils;
import com.lukmannudin.assosiate.searchmovie.dao.Database.FavoriteHelper;
import com.lukmannudin.assosiate.searchmovie.dao.Model.Movie;

import java.util.ArrayList;
import java.util.List;

import static com.lukmannudin.assosiate.searchmovie.Helper.MappingHelper.mapCursorToArrayList;
import static com.lukmannudin.assosiate.searchmovie.dao.Database.DatabaseContract.FavoriteColumns.CONTENT_URI;

/**
 * A simple {@link Fragment} subclass.
 */
public class FavoritesFragment extends Fragment {
    private ProgressBar loading;
    RecyclerView movieRecyclerView;
    private static final String KEY_MOVIES = "KEY_MOVIES";
    private ArrayList<Movie> data = new ArrayList<>();
    FavoritesAdapter adapter;
    List<Movie> data2;
    private FavoriteHelper favoriteHelper;
    private int pageId;
    private static final String EXTRA_STATE = "EXTRA_STATE";


    public FavoritesFragment() {
        // Required empty public constructor
    }

    public static FavoritesFragment newInstance(int pageId) {
        FavoritesFragment fragment = new FavoritesFragment();
        Bundle args = new Bundle();
        args.putInt(Utils.page, pageId);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState != null) {
            data = savedInstanceState.getParcelableArrayList(KEY_MOVIES);
            data2 = new ArrayList<>(data);
        }

        if (getArguments() != null) {
            pageId = getArguments().getInt(Utils.page);
        }


    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        outState.putParcelableArrayList(KEY_MOVIES, data);
        super.onSaveInstanceState(outState);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_now_playng, container, false);
        movieRecyclerView = view.findViewById(R.id.rvMovie);
        loading = view.findViewById(R.id.mainProggressbar);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
        movieRecyclerView.setLayoutManager(layoutManager);
        if (data2 != null) {
            adapter = new FavoritesAdapter(getContext(), data2, pageId);
            movieRecyclerView.setAdapter(adapter);
            adapter.notifyDataSetChanged();
        } else {
            processData();
        }
        return view;
    }

    private void processData() {
        try {
            Cursor mCursor = getActivity().getApplicationContext().getContentResolver().query(CONTENT_URI,null,null,null,null);
            assert mCursor != null;
            List<Movie> list = MappingHelper.mapCursorToArrayList(mCursor);
            data.addAll(list);
            adapter = new FavoritesAdapter(getContext(), data,pageId);
            movieRecyclerView.setAdapter(adapter);
            adapter.notifyDataSetChanged();
        } catch (Exception e){
            Log.i("error",e.getLocalizedMessage());
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
//        favoriteHelper.close();
    }


}

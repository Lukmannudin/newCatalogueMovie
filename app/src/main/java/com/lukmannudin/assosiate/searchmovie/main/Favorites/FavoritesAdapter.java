package com.lukmannudin.assosiate.searchmovie.main.Favorites;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.card.MaterialCardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.lukmannudin.assosiate.searchmovie.R;
import com.lukmannudin.assosiate.searchmovie.Utils;
import com.lukmannudin.assosiate.searchmovie.dao.Model.Movie;
import com.lukmannudin.assosiate.searchmovie.dao.ResultsItem;
import com.lukmannudin.assosiate.searchmovie.detail.MovieDetail;

import java.util.List;

public class FavoritesAdapter extends RecyclerView.Adapter<FavoritesAdapter.MovieViewHolder> {
    private List<Movie> results;
    private Context context;
    private int pageId;

    public FavoritesAdapter(Context context, List<Movie> results,int pageId) {
        this.context = context;
        this.results = results;
        this.pageId = pageId;
    }

    @NonNull
    @Override
    public MovieViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.movie_list, viewGroup, false);
        return new MovieViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final MovieViewHolder movieViewHolder, final int i) {
        Glide.with(context)
                .load("https://image.tmdb.org/t/p/w185/" + results.get(i).getPosterPath())
                .into(movieViewHolder.posterImage);

        movieViewHolder.title.setText(results.get(i).getTitle());
        movieViewHolder.description.setText(results.get(i).getOverview());

        movieViewHolder.mcv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(movieViewHolder.itemView.getContext(), MovieDetail.class);
                intent.putExtra("movieId", results.get(i).getId());
                intent.putExtra("movieTitle", results.get(i).getTitle());
                intent.putExtra(Utils.page,pageId);
                movieViewHolder.itemView.getContext().startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return results.size();
    }

    class MovieViewHolder extends RecyclerView.ViewHolder {
        private ImageView posterImage;
        private TextView title, description;
        private MaterialCardView mcv;

        MovieViewHolder(@NonNull View itemView) {
            super(itemView);
            posterImage = itemView.findViewById(R.id.ivPosterImage);
            title = itemView.findViewById(R.id.tvTitleMovie);
            description = itemView.findViewById(R.id.tvMovieDescription);
            mcv = itemView.findViewById(R.id.rv_movie_item);
        }
    }
}

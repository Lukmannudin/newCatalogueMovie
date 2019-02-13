package com.lukmannudin.assosiate.searchmovie.main.search_fragment;

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
import com.lukmannudin.assosiate.searchmovie.dao.Model.MovieTrending;
import com.lukmannudin.assosiate.searchmovie.detail.MovieDetail;

import java.util.List;

public class SearchAdapter extends RecyclerView.Adapter<SearchAdapter.MovieViewHolder> {
    private List<MovieTrending> movieTrendingList;
    private Context context;


    @NonNull
    @Override
    public MovieViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        this.context = parent.getContext();
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.movie_list, parent, false);
        return new MovieViewHolder(view);
    }

    SearchAdapter(List<MovieTrending> movieTrendingList) {
        this.movieTrendingList = movieTrendingList;
    }

    @Override
    public void onBindViewHolder(@NonNull final MovieViewHolder holder, final int position) {
        Glide.with(context)
                .load("https://image.tmdb.org/t/p/w185/" + movieTrendingList.get(position).getPosterPath())
                .into(holder.posterImage);
        holder.title.setText(movieTrendingList.get(position).getTitle());
        holder.description.setText(movieTrendingList.get(position).getOverview());

        holder.mcv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(holder.itemView.getContext(), MovieDetail.class);
                intent.putExtra("movieId", movieTrendingList.get(position).getId());
                intent.putExtra("movieTitle", movieTrendingList.get(position).getTitle());
                holder.itemView.getContext().startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return movieTrendingList.size();
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
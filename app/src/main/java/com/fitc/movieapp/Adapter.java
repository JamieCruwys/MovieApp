package com.fitc.movieapp;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.fitc.movieapp.api.Movie;

import java.util.ArrayList;
import java.util.List;

public class Adapter extends RecyclerView.Adapter<Adapter.MovieViewHolder> {

    private List<Movie> movies = new ArrayList();
    private int rowLayout;
    private Context context;


    public static class MovieViewHolder extends RecyclerView.ViewHolder {
        LinearLayout moviesLayout;
        TextView movieTitle;
        TextView data;
        TextView movieDescription;
        TextView rating;


        public MovieViewHolder(View v) {
            super(v);
            moviesLayout = (LinearLayout) v.findViewById(R.id.movies_layout);
            movieTitle = (TextView) v.findViewById(R.id.title);
            data = (TextView) v.findViewById(R.id.subtitle);
            movieDescription = (TextView) v.findViewById(R.id.description);
            rating = (TextView) v.findViewById(R.id.rating);
        }
    }

    public Adapter(int rowLayout, Context context) {
        this.rowLayout = rowLayout;
        this.context = context;
    }

    public void setData(List<Movie> movies) {
        this.movies = movies;
        this.notifyDataSetChanged();
    }

    @Override
    public Adapter.MovieViewHolder onCreateViewHolder(ViewGroup parent,
                                                      int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(rowLayout, parent, false);
        return new MovieViewHolder(view);
    }


    @Override
    public void onBindViewHolder(MovieViewHolder holder, final int position) {
        holder.movieTitle.setText(movies.get(position).title);
        holder.data.setText(movies.get(position).releaseDate);
        holder.movieDescription.setText(movies.get(position).overview);

        Double voteAverage = movies.get(position).voteAverage;

        holder.rating.setText(voteAverage != null ? voteAverage.toString(): null);
    }

    @Override
    public int getItemCount() {
        return movies.size();
    }
}
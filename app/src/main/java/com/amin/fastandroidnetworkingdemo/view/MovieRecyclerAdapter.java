package com.amin.fastandroidnetworkingdemo.view;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.amin.fastandroidnetworkingdemo.R;
import com.amin.fastandroidnetworkingdemo.constant.AppConstants;
import com.amin.fastandroidnetworkingdemo.model.Movie;
import com.bumptech.glide.Glide;

import java.util.List;

public class MovieRecyclerAdapter extends RecyclerView.Adapter<MovieRecyclerAdapter.Holder> {
    private List<Movie> movies;
    private Context context;

    public MovieRecyclerAdapter(List<Movie> movies, Context context) {
        this.movies = movies;
        this.context = context;
    }

    @NonNull
    @Override
    public Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.movie_item_layout, null);
        return new Holder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Holder holder, int position) {
        Movie movie = movies.get(position);

        holder.textViewTitle.setText(movie.getTitle());
        holder.textViewLanguage.setText(context.getString(R.string.language) + movie.getOriginalLanguage());
        holder.textViewRate.setText(context.getString(R.string.rate) + movie.getVoteAverage().toString());
        holder.textViewReleaseDate.setText(context.getString(R.string.relased) + movie.getReleaseDate());

        String imagePath = AppConstants.IMAGE_BASE_PATH + movie.getPosterPath();
        Glide.with(context)
                .load(imagePath).centerCrop()
                .placeholder(R.drawable.loading)
                .into(holder.imageViewMovie);
    }

    @Override
    public int getItemCount() {
        return movies.size();
    }

    public class Holder extends RecyclerView.ViewHolder {
        TextView textViewTitle;
        TextView textViewLanguage;
        TextView textViewRate;
        TextView textViewReleaseDate;
        ImageView imageViewMovie;

        public Holder(@NonNull View itemView) {
            super(itemView);

            textViewLanguage = itemView.findViewById(R.id.textViewLanguage);
            textViewReleaseDate = itemView.findViewById(R.id.textViewReleaseDate);
            textViewTitle = itemView.findViewById(R.id.textViewTitle);
            textViewRate = itemView.findViewById(R.id.textViewRate);
            imageViewMovie = itemView.findViewById(R.id.imageViewMovie);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent movieIntent = new Intent(context, MovieActivity.class);
                    if (getAdapterPosition() != -1) {
                        movieIntent.putExtra("movie", movies.get(getAdapterPosition()));
                        context.startActivity(movieIntent);
                    }
                }
            });

        }
    }
}

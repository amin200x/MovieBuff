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
import com.amin.fastandroidnetworkingdemo.model.TV;
import com.bumptech.glide.Glide;

import java.util.List;

public class TVRecyclerAdapter extends RecyclerView.Adapter<TVRecyclerAdapter.Holder> {
    private List<TV> tvs;
    private Context context;

    public TVRecyclerAdapter(List<TV> tvs, Context context) {
        this.tvs = tvs;
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
        TV tv = tvs.get(position);

        holder.textViewTitle.setText(tv.getName());
        holder.textViewLanguage.setText(context.getString(R.string.language) + tv.getOriginalLanguage());
        holder.textViewRate.setText(context.getString(R.string.rate) + tv.getVoteAverage().toString());
        holder.textViewReleaseDate.setText(context.getString(R.string.relased) + tv.getFirstAirDate());

        String imagePath = AppConstants.IMAGE_BASE_PATH + tv.getPosterPath();
        Glide.with(context)
                .load(imagePath).centerCrop()
                .placeholder(R.drawable.loading)
                .into(holder.imageViewMovie);
    }

    @Override
    public int getItemCount() {
        return tvs.size();
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
                    movieIntent.putExtra("tv", tvs.get(getAdapterPosition()));
                    context.startActivity(movieIntent);
                }
            });

        }
    }
}

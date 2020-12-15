package com.amin.fastandroidnetworkingdemo.view;

import android.os.Bundle;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.coordinatorlayout.widget.CoordinatorLayout;

import com.amin.fastandroidnetworkingdemo.R;
import com.amin.fastandroidnetworkingdemo.constant.AppConstants;
import com.amin.fastandroidnetworkingdemo.model.Movie;
import com.amin.fastandroidnetworkingdemo.model.MovieGenre;
import com.amin.fastandroidnetworkingdemo.model.TV;
import com.amin.fastandroidnetworkingdemo.model.TVGenre;
import com.amin.fastandroidnetworkingdemo.viewmodel.MovieGenreViewModel;
import com.amin.fastandroidnetworkingdemo.viewmodel.TVGenreViewModel;
import com.bumptech.glide.Glide;
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.appbar.CollapsingToolbarLayout;

import java.util.ArrayList;
import java.util.List;


public class MovieActivity extends AppCompatActivity {

    private ImageView imageView;
    private TextView textViewOverview;
    private TextView textViewLang;
    private TextView textViewReleased;
    private TextView textViewGenre;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie);
        Toolbar toolbar = findViewById(R.id.toolBarMovie);
        setSupportActionBar(toolbar);
        setTitle("");
        imageView = findViewById(R.id.imageViewMovieCollapse);

        textViewOverview = findViewById(R.id.textViewOverview);
        textViewLang = findViewById(R.id.textViewLang);
        textViewReleased = findViewById(R.id.textViewReleased);
        textViewGenre = findViewById(R.id.textViewGenre);

        if (getIntent().getParcelableExtra("movie") != null) {
            showMovie();
        } else if (getIntent().getParcelableExtra("tv") != null) {
            showTv();
        }

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        AppBarLayout appBarLayout = findViewById(R.id.appBar);
        CollapsingToolbarLayout collapsingToolbarLayout = findViewById(R.id.collapsingToolbar);
        LinearLayout linearLayout = findViewById(R.id.linearLayoutContent);
        CoordinatorLayout.LayoutParams params = new CoordinatorLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        appBarLayout.addOnOffsetChangedListener(new AppBarLayout.BaseOnOffsetChangedListener() {
            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {

                if (verticalOffset == 0) {
                    params.setMargins(0, collapsingToolbarLayout.getLayoutParams().height + 1100, 0, 0);

                } else {
                    params.setMargins(0, 250, 0, 0);

                }

                linearLayout.setLayoutParams(params);
            }
        });


    }

    private void showMovie() {
        List<MovieGenre> genres = new ArrayList<>(MainActivity.getMovieGenreList());

        Movie movie = getIntent().getParcelableExtra("movie");
        getSupportActionBar().setTitle(movie.getTitle());
        Glide.with(MovieActivity.this)
                .load(AppConstants.IMAGE_BASE_PATH + movie.getPosterPath()).centerCrop()
                .placeholder(R.drawable.loading)
                .into(imageView);

        if (genres!=null) {
            for (int i = 0; i < genres.size(); i++)
                if (!movie.getGenreIds().contains(genres.get(i).getId()))
                    genres.remove(i);
            String genreNames = "";
            for (MovieGenre genre : genres)
                genreNames += genre.getName() + ", ";
            textViewGenre.setText(getString(R.string.genres) + genreNames);
        }
        textViewOverview.setText(movie.getOverview());
        textViewLang.setText(getString(R.string.language) + movie.getOriginalLanguage());
        textViewReleased.setText(getString(R.string.relased) + movie.getReleaseDate());


    }

    private void showTv() {

        List<TVGenre> genres = new ArrayList<>(MainActivity.getTvGenreList());
        TV tv = getIntent().getParcelableExtra("tv");
        getSupportActionBar().setTitle(tv.getName());
        Glide.with(MovieActivity.this)
                .load(AppConstants.IMAGE_BASE_PATH + tv.getPosterPath()).centerCrop()
                .placeholder(R.drawable.loading)
                .into(imageView);

        for (int i = 0; i < genres.size(); i++) {
            if (!tv.getGenreIds().contains(genres.get(i).getId()))
                genres.remove(i);
        }
        String genreNames = "";
        for (TVGenre genre : genres) {
            genreNames += genre.getName() + ", ";
        }
        textViewOverview.setText(tv.getOverview());
        textViewLang.setText(getString(R.string.language) + tv.getOriginalLanguage());
        textViewReleased.setText(getString(R.string.relased) + tv.getFirstAirDate());
        textViewGenre.setText(getString(R.string.genres) + genreNames);

    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return super.onSupportNavigateUp();
    }
}
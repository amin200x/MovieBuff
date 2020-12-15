package com.amin.fastandroidnetworkingdemo.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.amin.fastandroidnetworkingdemo.model.MovieGenre;
import com.amin.fastandroidnetworkingdemo.repository.MovieGenreRepository;

import java.util.List;

public class MovieGenreViewModel extends AndroidViewModel {
    private MovieGenreRepository repository;

    public MovieGenreViewModel(@NonNull Application application) {
        super(application);
        repository = new MovieGenreRepository(application);
    }

    public MutableLiveData<List<MovieGenre>> getLiveDataMovieGenres(String key) {
        return repository.getLiveDataMovieGenres(key);

    }

}

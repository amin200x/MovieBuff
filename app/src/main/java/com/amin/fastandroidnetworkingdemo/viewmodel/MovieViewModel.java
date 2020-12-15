package com.amin.fastandroidnetworkingdemo.viewmodel;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.amin.fastandroidnetworkingdemo.model.Movie;
import com.amin.fastandroidnetworkingdemo.repository.MoviesRepository;

import java.util.List;

public class MovieViewModel extends AndroidViewModel {
    private MoviesRepository repository;

    public MovieViewModel(@NonNull Application application) {
        super(application);
        repository = new MoviesRepository(application);
    }

/*
    public MutableLiveData<List<Movie>> getLiveDataNowPlayingMovie() {
        return repository.getLiveDataNowPlayingMovie();
    }

    public MutableLiveData<List<Movie>> getLiveDataTopRatedMovie() {
        return repository.getLiveDataTopRatedMovie();

    }

    public MutableLiveData<List<Movie>> getLiveDataPopularMovie() {
        return repository.getLiveDataPopularMovie();

    }

    public MutableLiveData<List<Movie>> getLiveDataUpComingMovie() {
        return repository.getLiveDataUpComingMovie();

    } */
    public MutableLiveData<List<Movie>> getLiveDataMovies(String key, int page) {
        Log.d("AppTag01", key);
        return repository.getLiveDataMovies(key, page);

    }
    public void initialize(){
        repository.initialize();
    }
}

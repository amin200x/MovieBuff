package com.amin.fastandroidnetworkingdemo.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.amin.fastandroidnetworkingdemo.model.MovieGenre;
import com.amin.fastandroidnetworkingdemo.model.TVGenre;
import com.amin.fastandroidnetworkingdemo.repository.MovieGenreRepository;
import com.amin.fastandroidnetworkingdemo.repository.TVGenreRepository;

import java.util.List;

public class TVGenreViewModel extends AndroidViewModel {
    private TVGenreRepository repository;

    public TVGenreViewModel(@NonNull Application application) {
        super(application);
        repository = new TVGenreRepository(application);
    }

    public MutableLiveData<List<TVGenre>> getLiveDataTVGenres(String key) {
        return repository.getLiveDataTVGenres(key);

    }

}

package com.amin.fastandroidnetworkingdemo.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.amin.fastandroidnetworkingdemo.model.TV;
import com.amin.fastandroidnetworkingdemo.repository.TVRepository;

import java.util.List;

public class TVViewModel extends AndroidViewModel {
    private TVRepository repository;

    public TVViewModel(@NonNull Application application) {
        super(application);
        repository = new TVRepository(application);
    }

/*
    public MutableLiveData<List<TV>> getLiveDataAiringToday() {
        return repository.getLiveDataAiringToday();
    }

    public MutableLiveData<List<TV>> getLiveDataTopRatedTV() {
        return repository.getLiveDataTopRatedTV();

    }

    public MutableLiveData<List<TV>> getLiveDataPopularTV() {
        return repository.getLiveDataPopularTV();

    }

    public MutableLiveData<List<TV>> getLiveDataOnTheAir() {
        return repository.getLiveDataOnTheAir();

    } */

    public MutableLiveData<List<TV>> getLiveDataTVs(String key, int page) {
        return repository.getLiveDataTVs(key, page);
    }


    public void initialize() {
        repository.initialize();
    }
}

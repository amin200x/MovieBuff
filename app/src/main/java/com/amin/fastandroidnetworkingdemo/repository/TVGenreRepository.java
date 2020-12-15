package com.amin.fastandroidnetworkingdemo.repository;

import android.app.Application;
import android.util.Log;

import androidx.lifecycle.MutableLiveData;

import com.amin.fastandroidnetworkingdemo.R;
import com.amin.fastandroidnetworkingdemo.constant.AppConstants;
import com.amin.fastandroidnetworkingdemo.model.TVGenre;
import com.amin.fastandroidnetworkingdemo.model.TVGenres;
import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.gsonparserfactory.GsonParserFactory;
import com.rxandroidnetworking.RxAndroidNetworking;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import rx.Observable;
import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class TVGenreRepository {

    private Application application;
    private Observable<TVGenres> observable;
    private MutableLiveData<List<TVGenre>> liveDataTVGenres = new MutableLiveData<>();
    private List<TVGenre> tvGenres = new ArrayList<>();


    public TVGenreRepository(Application application) {
        this.application = application;

        OkHttpClient okHttpClient = new OkHttpClient().newBuilder()
                .connectTimeout(60, TimeUnit.SECONDS)
                .readTimeout(60, TimeUnit.SECONDS)
                .writeTimeout(60, TimeUnit.SECONDS)
                .build();

        AndroidNetworking.initialize(application, okHttpClient);
        AndroidNetworking.setParserFactory(new GsonParserFactory());
    }

    private void getMovieGenres(String search) {
        observable = getObservable(search);
        observable.subscribeOn(Schedulers.io())
                .subscribeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<TVGenres>() {
                    @Override
                    public void onCompleted() {
                       liveDataTVGenres.postValue(tvGenres);
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.d("AppTagMenu",e.getMessage());
                        e.printStackTrace();

                    }

                    @Override
                    public void onNext(TVGenres genres) {
                        tvGenres.addAll(genres.getTvGenres());
                        Log.d("AppTagMenu", tvGenres.get(1).getName());


                    }
                });

    }

    private Observable<TVGenres> getObservable(String search) {
        return RxAndroidNetworking
                .get(AppConstants.BASE_URL + search)
                .addQueryParameter("api_key", application.getString(R.string.api_key))
                .setPriority(Priority.LOW)
                .build()
                .getObjectObservable(TVGenres.class);
    }

    public MutableLiveData<List<TVGenre>> getLiveDataTVGenres(String key) {
        getMovieGenres(key);
        return liveDataTVGenres;
    }


}

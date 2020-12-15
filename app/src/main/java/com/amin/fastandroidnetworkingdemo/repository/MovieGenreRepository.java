package com.amin.fastandroidnetworkingdemo.repository;

import android.app.Application;
import android.util.Log;

import androidx.lifecycle.MutableLiveData;

import com.amin.fastandroidnetworkingdemo.R;
import com.amin.fastandroidnetworkingdemo.constant.AppConstants;
import com.amin.fastandroidnetworkingdemo.model.MovieGenre;
import com.amin.fastandroidnetworkingdemo.model.MovieGenres;
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

public class MovieGenreRepository {

    private Application application;
    private Observable<MovieGenres> observable;
    private MutableLiveData<List<MovieGenre>> liveDataMovieGenres = new MutableLiveData<>();
    private List<MovieGenre> movieGenres = new ArrayList<>();


    public MovieGenreRepository(Application application) {
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
                .subscribe(new Observer<MovieGenres>() {
                    @Override
                    public void onCompleted() {
                       liveDataMovieGenres.postValue(movieGenres);
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.d("AppTagMenu",e.getMessage());
                        e.printStackTrace();

                    }

                    @Override
                    public void onNext(MovieGenres genres) {
                        movieGenres.addAll(genres.getMovieGenres());
                        Log.d("AppTagMenu",movieGenres.get(1).getName());


                    }
                });

    }

    private Observable<MovieGenres> getObservable(String search) {
        return RxAndroidNetworking
                .get(AppConstants.BASE_URL + search)
                .addQueryParameter("api_key", application.getString(R.string.api_key))
                .setPriority(Priority.LOW)
                .build()
                .getObjectObservable(MovieGenres.class);
    }

    public MutableLiveData<List<MovieGenre>> getLiveDataMovieGenres(String key) {
        getMovieGenres(key);

        return liveDataMovieGenres;
    }


}

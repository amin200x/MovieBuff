package com.amin.fastandroidnetworkingdemo.repository;

import android.app.Application;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.widget.Toast;

import androidx.lifecycle.MutableLiveData;

import com.amin.fastandroidnetworkingdemo.R;
import com.amin.fastandroidnetworkingdemo.constant.AppConstants;
import com.amin.fastandroidnetworkingdemo.model.Movie;
import com.amin.fastandroidnetworkingdemo.model.MovieDBResponse;
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

public class MoviesRepository {

    private Application application;
    private MovieDBResponse movieDBResponse;
    private Observable<MovieDBResponse> observable;

    private MutableLiveData<List<Movie>> liveDataPopularMovie = new MutableLiveData<>();
    private MutableLiveData<List<Movie>> liveDataTopRatedMovie = new MutableLiveData<>();
    private MutableLiveData<List<Movie>> liveDataUpComingMovie = new MutableLiveData<>();
    private MutableLiveData<List<Movie>> liveDataNowPlayingMovie = new MutableLiveData<>();

    private List<Movie> popularMovies = new ArrayList<>();
    private List<Movie> topRatedMovies = new ArrayList<>();
    private List<Movie> upComingMovies = new ArrayList<>();
    private List<Movie> nowPlayingMovies = new ArrayList<>();
    private Handler handle;


    public MoviesRepository(Application application) {
        this.application = application;

        OkHttpClient okHttpClient = new OkHttpClient().newBuilder()
                .connectTimeout(60, TimeUnit.SECONDS)
                .readTimeout(60, TimeUnit.SECONDS)
                .writeTimeout(60, TimeUnit.SECONDS)
                .build();

        AndroidNetworking.initialize(application, okHttpClient);
        AndroidNetworking.setParserFactory(new GsonParserFactory());
        handle = new Handler(Looper.getMainLooper());
    }

    private void getMovies(String search, int page) {
        observable = getObservable(search, page);
        observable.subscribeOn(Schedulers.io())
                .subscribeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<MovieDBResponse>() {
                    @Override
                    public void onCompleted() {
                        loadData(search);
                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(MovieDBResponse movieResponse) {
                        movieDBResponse = movieResponse;
                        //  Log.d("AppTag1", movieResponse.getMovies().get(2).getTitle());

                    }
                });

    }

    private void loadData(String search) {
        showMessage(movieDBResponse.getPage(), movieDBResponse.getTotalPages());
        popularMovies.addAll(popularMovies.size(), movieDBResponse.getMovies());
        liveDataPopularMovie.postValue(popularMovies);
        /*switch (search) {
            case AppConstants.POPULAR_MOVIES:
                showMessage(movieDBResponse.getPage(), movieDBResponse.getTotalPages());
                popularMovies.addAll(popularMovies.size(), movieDBResponse.getMovies());
                liveDataPopularMovie.postValue(popularMovies);
                break;
            case AppConstants.NOW_PLAYING_MOVIES:
                nowPlayingMovies.addAll(nowPlayingMovies.size(), movieDBResponse.getMovies());
                liveDataNowPlayingMovie.postValue(nowPlayingMovies);
                break;
            case AppConstants.TOP_RATED_MOVIES:
                topRatedMovies.addAll(topRatedMovies.size(), movieDBResponse.getMovies());
                liveDataTopRatedMovie.postValue(topRatedMovies);
                break;
            case AppConstants.UP_COMING_MOVIES:
                upComingMovies.addAll(upComingMovies.size(), movieDBResponse.getMovies());
                liveDataUpComingMovie.postValue(upComingMovies);
                break;

        }*/
    }

    private Observable<MovieDBResponse> getObservable(String search, int page) {
        return RxAndroidNetworking
                .get(AppConstants.BASE_URL + search)
                .addQueryParameter("api_key", application.getString(R.string.api_key))
                .addQueryParameter("page", String.valueOf(page))
                .setPriority(Priority.LOW)
                .build()
                .getObjectObservable(MovieDBResponse.class);
    }

/*
    public MutableLiveData<List<Movie>> getLiveDataNowPlayingMovie() {

        if (liveDataNowPlayingMovie.getValue() == null) {
            getMovies(AppConstants.NOW_PLAYING_MOVIES, 1);

        } else {
            if (movieDBResponse.getPage() < movieDBResponse.getTotalPages())
                getMovies(AppConstants.NOW_PLAYING_MOVIES, movieDBResponse.getPage() + 1);

        }


        return liveDataNowPlayingMovie;
    }

    public MutableLiveData<List<Movie>> getLiveDataTopRatedMovie() {
        if (liveDataTopRatedMovie.getValue() == null) {
            getMovies(AppConstants.TOP_RATED_MOVIES, 1);

        } else {
            if (movieDBResponse.getPage() < movieDBResponse.getTotalPages())
                getMovies(AppConstants.TOP_RATED_MOVIES, movieDBResponse.getPage() + 1);

        }

        return liveDataTopRatedMovie;
    }

    public MutableLiveData<List<Movie>> getLiveDataPopularMovie() {
        if (liveDataPopularMovie.getValue() == null) {
            getMovies(AppConstants.POPULAR_MOVIES, 1);
            Log.d("AppTagF", 0 + "");
        } else {
            if (movieDBResponse.getPage() < movieDBResponse.getTotalPages()) {
                getMovies(AppConstants.POPULAR_MOVIES, movieDBResponse.getPage() + 1);
                Log.d("AppTagF", movieDBResponse.getPage() + "");
            }

        }

        return liveDataPopularMovie;
    }

    public MutableLiveData<List<Movie>> getLiveDataUpComingMovie() {
        if (liveDataUpComingMovie.getValue() == null) {
            getMovies(AppConstants.UP_COMING_MOVIES, 1);
        } else {
            if (movieDBResponse.getPage() < movieDBResponse.getTotalPages())
                getMovies(AppConstants.UP_COMING_MOVIES, movieDBResponse.getPage() + 1);
        }

        return liveDataUpComingMovie;
    }*/

    public MutableLiveData<List<Movie>> getLiveDataMovies(String key, int page) {
        if (movieDBResponse == null || page <= movieDBResponse.getTotalPages())
            getMovies(key, page);
        Log.d("AppTag02", key);
        return liveDataPopularMovie;
    }

    private void showMessage(int page, int totalPage) {
        final StringBuilder sb = new StringBuilder();
        sb.append(application.getString(R.string.page))
                .append(page)
                .append(application.getString(R.string.totalPage))
                .append(totalPage);
        handle.postDelayed(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(application, sb.toString(), Toast.LENGTH_LONG).show();
            }
        }, 1000);

    }

    public void initialize(){
        popularMovies.clear();
    }

}

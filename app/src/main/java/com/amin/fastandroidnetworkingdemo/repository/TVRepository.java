package com.amin.fastandroidnetworkingdemo.repository;

import android.app.Application;
import android.os.Handler;
import android.os.Looper;
import android.widget.Toast;

import androidx.lifecycle.MutableLiveData;

import com.amin.fastandroidnetworkingdemo.R;
import com.amin.fastandroidnetworkingdemo.constant.AppConstants;
import com.amin.fastandroidnetworkingdemo.model.TV;
import com.amin.fastandroidnetworkingdemo.model.TVDBResponse;
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

public class TVRepository {

    private Application application;
    private TVDBResponse tvDBResponse;

    private Observable<TVDBResponse> observable;

    private MutableLiveData<List<TV>> liveDataPopularTV = new MutableLiveData<>();
    private MutableLiveData<List<TV>> liveDataTopRatedTV = new MutableLiveData<>();
    private MutableLiveData<List<TV>> liveDataOnTheAir = new MutableLiveData<>();
    private MutableLiveData<List<TV>> liveDataAiringToday = new MutableLiveData<>();

    private List<TV> popularTVs = new ArrayList<>();
    private List<TV> topRatedTVs = new ArrayList<>();
    private List<TV> onTheAirTVs = new ArrayList<>();
    private List<TV> airingTodayTVs = new ArrayList<>();
    private Handler handle;

    public TVRepository(Application application) {
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

    private void getTvs(String search, int page) {
        observable = getObservable(search, page);
        observable.subscribeOn(Schedulers.io())
                .subscribeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<TVDBResponse>() {
                    @Override
                    public void onCompleted() {
                        loadData(search);
                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                    }

                    @Override
                    public void onNext(TVDBResponse tvdbResponse) {
                        tvDBResponse = tvdbResponse;
                        //  Log.d("AppTag1", movieResponse.getMovies().get(2).getTitle());

                    }
                });

    }

    private void loadData(String search) {
        showMessage(tvDBResponse.getPage(), tvDBResponse.getTotalPages());
        popularTVs.addAll(popularTVs.size(), tvDBResponse.getTvs());
        liveDataPopularTV.postValue(popularTVs);

       /* switch (search) {
            case AppConstants.POPULAR_TV:
                popularTVs.addAll(popularTVs.size(), tvDBResponse.getTvs());
                liveDataPopularTV.postValue(popularTVs);
                break;
            case AppConstants.ON_THE_AIR_TV:
                onTheAirTVs.addAll(onTheAirTVs.size(), tvDBResponse.getTvs());
                liveDataOnTheAir.postValue(onTheAirTVs);
                break;
            case AppConstants.AIRING_TODAY_TV:
                airingTodayTVs.addAll(airingTodayTVs.size(), tvDBResponse.getTvs());
                liveDataAiringToday.postValue(airingTodayTVs);
                break;
            case AppConstants.TOP_RATED_TV:
                topRatedTVs.addAll(topRatedTVs.size(), tvDBResponse.getTvs());
                liveDataTopRatedTV.postValue(topRatedTVs);
                break;

        }*/
    }

    private Observable<TVDBResponse> getObservable(String search, int page) {
        return RxAndroidNetworking
                .get(AppConstants.BASE_URL + search)
                .addQueryParameter("api_key", application.getString(R.string.api_key))
                .addQueryParameter("page", String.valueOf(page))
                .setPriority(Priority.LOW)
                .build()
                .getObjectObservable(TVDBResponse.class);
    }

/*
    public MutableLiveData<List<TV>> getLiveDataOnTheAir() {
        if (liveDataOnTheAir.getValue() == null) {
            tvDBResponse = null;
            getTvs(AppConstants.ON_THE_AIR_TV, 1);
        } else {
            if (tvDBResponse.getPage() < tvDBResponse.getTotalPages())
                getTvs(AppConstants.ON_THE_AIR_TV, tvDBResponse.getPage() + 1);
        }

        return liveDataOnTheAir;
    }

    public MutableLiveData<List<TV>> getLiveDataAiringToday() {
        if (liveDataAiringToday.getValue() == null) {
            tvDBResponse = null;
            getTvs(AppConstants.AIRING_TODAY_TV, 1);
        } else {
            if (tvDBResponse.getPage() < tvDBResponse.getTotalPages())
                getTvs(AppConstants.AIRING_TODAY_TV, tvDBResponse.getPage() + 1);
        }


        return liveDataAiringToday;
    }

    public MutableLiveData<List<TV>> getLiveDataPopularTV() {
        if (liveDataPopularTV.getValue() == null) {
            tvDBResponse = null;
            getTvs(AppConstants.POPULAR_TV, 1);
        } else {
            if (tvDBResponse.getPage() < tvDBResponse.getTotalPages())
                getTvs(AppConstants.POPULAR_TV, tvDBResponse.getPage() + 1);
        }

        return liveDataPopularTV;
    }

    public MutableLiveData<List<TV>> getLiveDataTopRatedTV() {
        if (liveDataTopRatedTV.getValue() == null) {
            tvDBResponse = null;
            getTvs(AppConstants.TOP_RATED_TV, 1);
        } else {
            if (tvDBResponse.getPage() < tvDBResponse.getTotalPages())
                getTvs(AppConstants.TOP_RATED_TV, tvDBResponse.getPage() + 1);
        }

        return liveDataTopRatedTV;
    } */


    public MutableLiveData<List<TV>> getLiveDataTVs(String key, int page) {
        if (tvDBResponse == null || page <= tvDBResponse.getTotalPages())
            getTvs(key, page);
        return liveDataPopularTV;
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
        }, 500);

    }

    public void initialize() {
        popularTVs.clear();
    }
}

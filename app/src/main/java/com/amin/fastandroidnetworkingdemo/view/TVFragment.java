package com.amin.fastandroidnetworkingdemo.view;

import android.content.Context;
import android.content.res.Configuration;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.amin.fastandroidnetworkingdemo.R;
import com.amin.fastandroidnetworkingdemo.model.FilterListener;
import com.amin.fastandroidnetworkingdemo.model.SortListener;
import com.amin.fastandroidnetworkingdemo.model.TV;
import com.amin.fastandroidnetworkingdemo.viewmodel.TVViewModel;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class TVFragment extends Fragment implements SortListener
        , TextWatcher, FilterListener {

    private TVRecyclerAdapter adapter;
    private List<TV> tvs = new ArrayList<>();
    private MutableLiveData<List<TV>> liveDataTV;
    private TVViewModel tvViewModel;
    private MainActivity mainActivity;
    private SwipeRefreshLayout refreshLayout;
    private int page = 1;
    private String selectKey;
    private boolean isSearch = false;
    private int genreId = -1;

    public TVFragment(String selectKey) {
        this.selectKey = selectKey;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.tv_fragment_layout, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        RecyclerView recyclerView = view.findViewById(R.id.recyclerViewTv);
        adapter = new TVRecyclerAdapter(tvs, getActivity());
        GridLayoutManager layoutManager;
        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
            layoutManager = new GridLayoutManager(getActivity(), 3);
        } else {
            layoutManager = new GridLayoutManager(getActivity(), 2);
        }

        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adapter);

        tvViewModel = new TVViewModel(getActivity().getApplication());
        if (liveDataTV == null) {
            select(selectKey, page);
            page++;
        }

        refreshLayout = view.findViewById(R.id.swipeRefreshLayoutTv);
        refreshLayout.setRefreshing(true);
        loadTVs(liveDataTV);


        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refreshLayout.setRefreshing(true);
                if (tvViewModel == null || liveDataTV.getValue() == null) {
                    select(selectKey, page);
                    //page++;

                }
            }
        });

        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (!recyclerView.canScrollVertically(RecyclerView.FOCUS_DOWN)) {
                    if (!isSearch) {
                        select(selectKey, page);
                        page++;
                    }
                }


            }
        });
        recyclerView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                isSearch = false;
                return false;
            }
        });
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        mainActivity = ((MainActivity) context);
        mainActivity.setSortListener(this);
        mainActivity.setFilterListener(this);

    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser) {
            mainActivity.setSortListener(this);
            mainActivity.setFilterListener(this);
        }
    }

    private void loadTVs(MutableLiveData<List<TV>> liveDataTV) {
        liveDataTV.removeObservers(mainActivity);
        liveDataTV.observe(getActivity(), new Observer<List<TV>>() {
            @Override
            public void onChanged(List<TV> tvList) {
                tvs.clear();
                if (genreId > -1) {
                    for (TV tv : tvList) {
                        if (tv.getGenreIds().contains(genreId)) {
                            tvs.add(tv);
                        }
                    }
                } else {
                    tvs.addAll(tvList);
                }

                adapter.notifyDataSetChanged();
                refreshLayout.setRefreshing(false);
            }
        });
    }


    public void select(String key, int page) {
        liveDataTV = tvViewModel.getLiveDataTVs(key, page);
        loadTVs(liveDataTV);
    }

    @Override
    public void sort(SortType sortType) {
        if (liveDataTV.getValue() != null) {
            List<TV> TVsTemp = liveDataTV.getValue();
            switch (sortType) {
                case SortByNameAZ:
                    sortByNameAZ(TVsTemp);
                    break;
                case SortByNameZA:
                    sortByNameZA(TVsTemp);
                    break;
                case SortByReleasedDateAsc:
                    sortByReleasedDateAsc(TVsTemp);
                    break;
                case SortByReleasedDateDesc:
                    sortByReleasedDateDesc(TVsTemp);
                    break;
                case SortByRateAsc:
                    sortByRateAsc(TVsTemp);
                    break;
                case SortByRateDesc:
                    sortByRateDesc(TVsTemp);
                    break;
            }
            liveDataTV.postValue(TVsTemp);
        }

    }

    private void sortByRateDesc(List<TV> TVsTemp) {
        Collections.sort(TVsTemp, new Comparator<TV>() {
            @Override
            public int compare(TV m1, TV m2) {
                return m1.getVoteAverage().compareTo(m2.getVoteAverage());
            }
        });
        Collections.reverse(TVsTemp);
    }

    private void sortByRateAsc(List<TV> TVsTemp) {
        Collections.sort(TVsTemp, new Comparator<TV>() {
            @Override
            public int compare(TV m1, TV m2) {
                return m1.getVoteAverage().compareTo(m2.getVoteAverage());
            }
        });
    }

    private void sortByReleasedDateDesc(List<TV> TVsTemp) {
        Collections.sort(TVsTemp, new Comparator<TV>() {
            @Override
            public int compare(TV m1, TV m2) {
                return m1.getFirstAirDate().compareToIgnoreCase(m2.getFirstAirDate());
            }
        });
        Collections.reverse(TVsTemp);
    }

    private void sortByReleasedDateAsc(List<TV> TVsTemp) {
        Collections.sort(TVsTemp, new Comparator<TV>() {
            @Override
            public int compare(TV m1, TV m2) {
                return m1.getFirstAirDate().compareToIgnoreCase(m2.getFirstAirDate());
            }
        });
    }

    private void sortByNameZA(List<TV> TVsTemp) {
        Collections.sort(TVsTemp, new Comparator<TV>() {
            @Override
            public int compare(TV m1, TV m2) {
                return m1.getName().compareToIgnoreCase(m2.getName());
            }
        });
        Collections.reverse(TVsTemp);
    }

    private void sortByNameAZ(List<TV> TVsTemp) {
        Collections.sort(TVsTemp, new Comparator<TV>() {
            @Override
            public int compare(TV m1, TV m2) {
                return m1.getName().compareToIgnoreCase(m2.getName());
            }
        });
    }

    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        isSearch = true;
    }

    @Override
    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void afterTextChanged(Editable editable) {
        if (liveDataTV != null) {
            List<TV> TVsTemp = new ArrayList<>();
            if (liveDataTV.getValue() != null) {
                for (TV tv : liveDataTV.getValue()) {
                    if (tv.getName() != null)
                        if (tv.getName().toLowerCase().contains(editable.toString().toLowerCase()))
                            TVsTemp.add(tv);
                }
                MutableLiveData<List<TV>> liveData = new MutableLiveData<>();
                liveData.postValue(TVsTemp);
                loadTVs(liveData);
            }
        }

    }
    @Override
    public void filter(int genreId) {
        this.genreId = genreId;
        if (genreId > -1) {
            if (liveDataTV != null) {
                List<TV> moviesTemp = new ArrayList<>();
                if (liveDataTV.getValue() != null) {
                    for (TV tv : liveDataTV.getValue()) {
                        if (tv.getGenreIds() != null)
                            if (tv.getGenreIds().contains(genreId))
                                moviesTemp.add(tv);
                    }
                    MutableLiveData<List<TV>> liveData = new MutableLiveData<>();
                    liveData.postValue(moviesTemp);
                    loadTVs(liveData);

                }
            }
        }else {
            loadTVs(liveDataTV);
        }
    }

}

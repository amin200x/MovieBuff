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
import com.amin.fastandroidnetworkingdemo.constant.AppConstants;
import com.amin.fastandroidnetworkingdemo.model.FilterListener;
import com.amin.fastandroidnetworkingdemo.model.Movie;
import com.amin.fastandroidnetworkingdemo.model.SortListener;
import com.amin.fastandroidnetworkingdemo.viewmodel.MovieViewModel;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class MovieFragment extends Fragment implements SortListener
        , TextWatcher, FilterListener {

    private MovieRecyclerAdapter adapter;
    private List<Movie> movies = new ArrayList<>();
    private MutableLiveData<List<Movie>> liveDataMovie;
    private MovieViewModel movieViewModel;
    private MainActivity mainActivity;
    private SwipeRefreshLayout refreshLayout;
    private String selectKey;
    private int page = 1;
    private boolean isSearch = false;
    private int genreId = -1;

    public MovieFragment() {
        this.selectKey = AppConstants.POPULAR_MOVIES;
    }

    public MovieFragment(String selectKey) {
        this.selectKey = selectKey;
    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.movie_fragment_layout, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        RecyclerView recyclerView = view.findViewById(R.id.recyclerView);
        adapter = new MovieRecyclerAdapter(movies, getActivity());
        GridLayoutManager layoutManager;
        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
            layoutManager = new GridLayoutManager(getActivity(), 3);
        } else {
            layoutManager = new GridLayoutManager(getActivity(), 2);
        }

        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adapter);

        movieViewModel = new MovieViewModel(getActivity().getApplication());
        if (liveDataMovie == null) {
            select(selectKey, page);
            page++;
        }


        refreshLayout = view.findViewById(R.id.swipeRefreshLayout);
        refreshLayout.setRefreshing(true);
        loadMovies(liveDataMovie);


        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refreshLayout.setRefreshing(true);
                select(selectKey, page);
                 page++;


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

    private void loadMovies(MutableLiveData<List<Movie>> liveDataMovie) {
        if (liveDataMovie != null)
            liveDataMovie.removeObservers(mainActivity);
        liveDataMovie.observe(getActivity(), new Observer<List<Movie>>() {
            @Override
            public void onChanged(List<Movie> movieList) {
                movies.clear();
                if (genreId > -1) {
                    for (Movie movie : movieList) {
                        if (movie.getGenreIds().contains(genreId)) {
                            movies.add(movie);
                        }
                    }
                } else {
                    movies.addAll(movieList);
                }
                adapter.notifyDataSetChanged();
                refreshLayout.setRefreshing(false);

            }
        });
    }

    public void select(String key, int page) {
        liveDataMovie = movieViewModel.getLiveDataMovies(key, page);
        loadMovies(liveDataMovie);
    }

    @Override
    public void sort(SortType sortType) {
        if (liveDataMovie.getValue() != null) {
            List<Movie> moviesTemp = liveDataMovie.getValue();
            switch (sortType) {
                case SortByNameAZ:
                    sortByNameAZ(moviesTemp);
                    break;
                case SortByNameZA:
                    sortByNameZA(moviesTemp);
                    break;
                case SortByReleasedDateAsc:
                    sortByReleasedDateAsc(moviesTemp);
                    break;
                case SortByReleasedDateDesc:
                    sortByReleasedDateDesc(moviesTemp);
                    break;
                case SortByRateAsc:
                    sortByRateAsc(moviesTemp);
                    break;
                case SortByRateDesc:
                    sortByRateDesc(moviesTemp);
                    break;
            }
            liveDataMovie.postValue(moviesTemp);
        }

    }

    private void sortByRateDesc(List<Movie> moviesTemp) {
        Collections.sort(moviesTemp, new Comparator<Movie>() {
            @Override
            public int compare(Movie m1, Movie m2) {
                return m1.getVoteAverage().compareTo(m2.getVoteAverage());
            }
        });
        Collections.reverse(moviesTemp);
    }

    private void sortByRateAsc(List<Movie> moviesTemp) {
        Collections.sort(moviesTemp, new Comparator<Movie>() {
            @Override
            public int compare(Movie m1, Movie m2) {
                return m1.getVoteAverage().compareTo(m2.getVoteAverage());
            }
        });
    }

    private void sortByReleasedDateDesc(List<Movie> moviesTemp) {
        Collections.sort(moviesTemp, new Comparator<Movie>() {
            @Override
            public int compare(Movie m1, Movie m2) {
                return m1.getReleaseDate().compareToIgnoreCase(m2.getReleaseDate());
            }
        });
        Collections.reverse(moviesTemp);
    }

    private void sortByReleasedDateAsc(List<Movie> moviesTemp) {
        Collections.sort(moviesTemp, new Comparator<Movie>() {
            @Override
            public int compare(Movie m1, Movie m2) {
                return m1.getReleaseDate().compareToIgnoreCase(m2.getReleaseDate());
            }
        });
    }

    private void sortByNameZA(List<Movie> moviesTemp) {
        Collections.sort(moviesTemp, new Comparator<Movie>() {
            @Override
            public int compare(Movie m1, Movie m2) {
                return m1.getTitle().compareToIgnoreCase(m2.getTitle());
            }
        });
        Collections.reverse(moviesTemp);
    }

    private void sortByNameAZ(List<Movie> moviesTemp) {
        Collections.sort(moviesTemp, new Comparator<Movie>() {
            @Override
            public int compare(Movie m1, Movie m2) {
                return m1.getTitle().compareToIgnoreCase(m2.getTitle());
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
        if (liveDataMovie != null) {
            List<Movie> moviesTemp = new ArrayList<>();
            if (liveDataMovie.getValue() != null) {
                for (Movie movie : liveDataMovie.getValue()) {
                    if (movie.getTitle() != null)
                        if (movie.getTitle().toLowerCase().contains(editable.toString().toLowerCase()))
                            moviesTemp.add(movie);
                }
                MutableLiveData<List<Movie>> liveData = new MutableLiveData<>();
                liveData.postValue(moviesTemp);
                loadMovies(liveData);

            }
        }

    }


    @Override
    public void filter(int genreId) {
        this.genreId = genreId;
        if (genreId > -1) {
            if (liveDataMovie != null) {
                List<Movie> moviesTemp = new ArrayList<>();
                if (liveDataMovie.getValue() != null) {
                    for (Movie movie : liveDataMovie.getValue()) {
                        if (movie.getGenreIds() != null)
                            if (movie.getGenreIds().contains(genreId))
                                moviesTemp.add(movie);
                    }
                    MutableLiveData<List<Movie>> liveData = new MutableLiveData<>();
                    liveData.postValue(moviesTemp);
                    loadMovies(liveData);

                }
            }
        } else
            loadMovies(liveDataMovie);
    }
}

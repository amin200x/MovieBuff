package com.amin.fastandroidnetworkingdemo.view;

import android.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.Observer;

import com.amin.fastandroidnetworkingdemo.R;
import com.amin.fastandroidnetworkingdemo.constant.AppConstants;
import com.amin.fastandroidnetworkingdemo.model.FilterListener;
import com.amin.fastandroidnetworkingdemo.model.MovieGenre;
import com.amin.fastandroidnetworkingdemo.model.SortListener;
import com.amin.fastandroidnetworkingdemo.model.TVGenre;
import com.amin.fastandroidnetworkingdemo.viewmodel.MovieGenreViewModel;
import com.amin.fastandroidnetworkingdemo.viewmodel.TVGenreViewModel;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    enum MenuState {TV, Movie}

    private SortListener sortListener;
    private FilterListener filterListener;
    private TVFragment tvFragment;
    private MovieFragment movieFragment;
    private EditText editTextSearch;
    private FragmentTransaction fragmentTransaction;
    private static List<MovieGenre> movieGenreList = new ArrayList<>();
    private static List<TVGenre> tvGenreList = new ArrayList<>();
    private MenuState menuState = MenuState.Movie;
    private List<Integer> movieGenreMenuItemIds = new ArrayList<>();
    private List<Integer> tvGenreMenuItemIds = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolBar);

        setSupportActionBar(toolbar);
        movieFragment = new MovieFragment(AppConstants.POPULAR_MOVIES);
        tvFragment = new TVFragment(AppConstants.POPULAR_TV);

        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.add(R.id.frameLayout, movieFragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();

        editTextSearch = findViewById(R.id.editTextSearch);
        editTextSearch.addTextChangedListener(movieFragment);

        TVGenre tvGenre = new TVGenre();
        tvGenre.setId(-1);
        tvGenre.setName("All");
        tvGenreList.add(tvGenre);
        tvGenreMenuItemIds.add(-1);

        MovieGenre movieGenre = new MovieGenre();
        movieGenre.setId(-1);
        movieGenre.setName("All");
        movieGenreList.add(movieGenre);
        movieGenreMenuItemIds.add(-1);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.app_menu, menu);
        addMovieGenresItemMenu(menu);
        addTVGenresItemMenu(menu);

        return super.onCreateOptionsMenu(menu);
    }

    private void addMovieGenresItemMenu(Menu menu) {
        MovieGenreViewModel movieGenreViewModel = new MovieGenreViewModel(this.getApplication());
        movieGenreViewModel.getLiveDataMovieGenres(AppConstants.GENRE_MOVIE_LIST)
                .observe(MainActivity.this, new Observer<List<MovieGenre>>() {

                    @Override
                    public void onChanged(List<MovieGenre> movieGenres) {
                        MainActivity.this.movieGenreList.addAll(1, movieGenres);
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                for (int i = 0; i < movieGenreList.size(); i++) {
                                    menu.findItem(R.id.filter)
                                            .getSubMenu().findItem(R.id.movieGenre)
                                            .getSubMenu().add(Menu.NONE, i + 100, Menu.NONE, movieGenreList.get(i).getName());
                                    movieGenreMenuItemIds.add(i + 100);
                                }
                            }
                        });


                    }
                });
    }

    private void addTVGenresItemMenu(Menu menu) {
        TVGenreViewModel tvGenreViewModel = new TVGenreViewModel(this.getApplication());
        tvGenreViewModel.getLiveDataTVGenres(AppConstants.GENRE_TV_LIST)
                .observe(MainActivity.this, new Observer<List<TVGenre>>() {

                    @Override
                    public void onChanged(List<TVGenre> tvGenres) {
                        MainActivity.this.tvGenreList.addAll(1, tvGenres);
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                for (int i = 0; i < tvGenreList.size(); i++) {
                                    menu.findItem(R.id.filter).getSubMenu().findItem(R.id.tvGenre)
                                            .getSubMenu().add(Menu.NONE, i + 200, Menu.NONE, tvGenreList.get(i).getName());
                                    tvGenreMenuItemIds.add(i + 200);
                                }
                            }
                        });


                    }
                });
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        filter(item);
        selectSort(item);
        selectOption(item);
        return super.onOptionsItemSelected(item);
    }

    private boolean filter(MenuItem item) {
        if (movieGenreMenuItemIds.contains(item.getItemId()) && menuState == MenuState.Movie) {
            selectMovieGenre(item);
            Log.d("MenuItem", String.valueOf(item.getTitle()));
            Log.d("MenuItem", String.valueOf(item.getItemId()));
            return true;
        } else if (tvGenreMenuItemIds.contains(item.getItemId()) && menuState == MenuState.TV) {
            selectTVGenre(item);
            Log.d("MenuItem", String.valueOf(item.getTitle()));
            Log.d("MenuItem", String.valueOf(item.getItemId()));
            return true;
        }
        Log.d("MenuItem", menuState.name());
        return super.onOptionsItemSelected(item);

    }


    private void selectMovieGenre(MenuItem item) {
      /*  if (isBetween(itemId, 0, movieGenres.size() - 1)) {
            filterListener.filter(movieGenres.get(itemId).getId());
        }*/

        for (MovieGenre genre : movieGenreList) {
            if (genre.getName().equals(item.getTitle())) {
                filterListener.filter(genre.getId());
                break;
            }
        }
    }

    private void selectTVGenre(MenuItem item) {
       /* int item = itemId - 10; // TV genres id start from 10 for having the index of them must mines 10
        if (isBetween(item, 0, tvGenres.size() - 1)) {
            filterListener.filter(tvGenres.get(item).getId());
        }*/
        for (TVGenre genre : tvGenreList) {
            if (genre.getName().equals(item.getTitle())) {
                filterListener.filter(genre.getId());
                break;
            }
        }
    }

    public static boolean isBetween(int x, int lower, int upper) {
        return lower <= x && x <= upper;
    }

    private boolean selectOption(MenuItem item) {

        switch (item.getItemId()) {
            // Movie group
            case R.id.popularMovies:
                menuState = MenuState.Movie;
                movieFragment = new MovieFragment(AppConstants.POPULAR_MOVIES);
                break;
            case R.id.topRatedMovies:
                menuState = MenuState.Movie;
                movieFragment = new MovieFragment(AppConstants.TOP_RATED_MOVIES);
                break;
            case R.id.nowPlayingMovies:
                menuState = MenuState.Movie;
                movieFragment = new MovieFragment(AppConstants.NOW_PLAYING_MOVIES);
                break;
            case R.id.upComingMovies:
                menuState = MenuState.Movie;
                movieFragment = new MovieFragment(AppConstants.UP_COMING_MOVIES);
                break;

            // Tv group
            case R.id.airingToday:
                menuState = MenuState.TV;
                tvFragment = new TVFragment(AppConstants.AIRING_TODAY_TV);
                break;

            case R.id.onTheAir:
                menuState = MenuState.TV;
                tvFragment = new TVFragment(AppConstants.ON_THE_AIR_TV);
                break;
            case R.id.popularTv:
                menuState = MenuState.TV;
                tvFragment = new TVFragment(AppConstants.POPULAR_TV);
                break;
            case R.id.topRatedTv:
                menuState = MenuState.TV;
                tvFragment = new TVFragment(AppConstants.TOP_RATED_TV);
                break;
        }


        if (menuState == MenuState.TV) {
            showTvFragment();
        } else if (menuState == MenuState.Movie) {
            showMovieFragment();
        }
        return super.onOptionsItemSelected(item);
    }


    private boolean selectSort(MenuItem item) {
        switch (item.getItemId()) {
            // Sort Menu
            case R.id.sortByNameAZ:
                sortListener.sort(SortListener.SortType.SortByNameAZ);
                break;
            case R.id.sortByNameZA:
                sortListener.sort(SortListener.SortType.SortByNameZA);
                break;
            case R.id.sortByDateAsc:
                sortListener.sort(SortListener.SortType.SortByReleasedDateAsc);
                break;
            case R.id.sortByDateDesc:
                sortListener.sort(SortListener.SortType.SortByReleasedDateDesc);
                break;
            case R.id.sortByRateDesc:
                sortListener.sort(SortListener.SortType.SortByRateDesc);
                break;
            case R.id.sortByRateAsc:
                sortListener.sort(SortListener.SortType.SortByRateAsc);
                break;

        }

        return super.onOptionsItemSelected(item);
    }

    private void showTvFragment() {
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frameLayout, tvFragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
        editTextSearch.addTextChangedListener(tvFragment);


    }

    private void showMovieFragment() {
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frameLayout, movieFragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
        editTextSearch.addTextChangedListener(movieFragment);
    }

    @Override
    public void onAttachFragment(Fragment fragment) {
        super.onAttachFragment(fragment);

    }

    public void setSortListener(SortListener sortListener) {
        this.sortListener = sortListener;
    }

    public void setFilterListener(FilterListener filterListener) {
        this.filterListener = filterListener;
    }

    public static List<MovieGenre> getMovieGenreList() {
        return movieGenreList;
    }

    public static List<TVGenre> getTvGenreList() {
        return tvGenreList;
    }
}
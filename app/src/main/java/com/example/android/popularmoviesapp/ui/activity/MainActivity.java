package com.example.android.popularmoviesapp.ui.activity;


import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.android.popularmoviesapp.R;
import com.example.android.popularmoviesapp.utils.MoviesContract;
import com.example.android.popularmoviesapp.utils.RecyclerInsetsDecoration;
import com.example.android.popularmoviesapp.adapter.MoviesAdapter;
import com.example.android.popularmoviesapp.model.MoviesResultData;
import com.example.android.popularmoviesapp.model.SingleMovie;
import com.example.android.popularmoviesapp.utils.ApiInterface;
import com.example.android.popularmoviesapp.utils.AutofitRecyclerView;
import com.example.android.popularmoviesapp.utils.CommonUtils;
import com.example.android.popularmoviesapp.utils.MoviesDatabaseHelper;
import com.example.android.popularmoviesapp.utils.NetworkUtils;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {

    String TAG ="MainActivity";
    MoviesAdapter moviesAdapter;
    ProgressBar mLoadingIndicator;
    String sortingParameter = "popular";
    ArrayList<SingleMovie> movieList = new ArrayList<>();
    CoordinatorLayout coordinatorLayout;
    private static final String LIFECYCLE_MOVIE_CALLBACKS_KEY = "movieList";
    private static final String LIFECYCLE_PAGE_NO_KEY = "page_no";
    private static final String LIFECYCLE_SORTING_PARAMETER_KEY = "sorting_parameter";
    private static final String LIFECYCLE_ACTIONBAR_TITLE = "actionbar_title";
    private static final int ID_FAVORITE_MOVIE_LOADER = 57;

    SQLiteDatabase db;
    AutofitRecyclerView moviesRecyclerView;
    Snackbar snackbar;
    Toast mCurrentToast;
    String actionBarTitle = "Popular Movies";

    MoviesDatabaseHelper dbHelper;
    int page_no;
    private boolean loadingMore;
    ActionBar actionBar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        loadingMore = false;
        setUpViews();
        actionBar = getSupportActionBar();
        dbHelper = new MoviesDatabaseHelper(this);
        db = dbHelper.getWritableDatabase();


        if (savedInstanceState == null || !savedInstanceState.containsKey(LIFECYCLE_MOVIE_CALLBACKS_KEY) ||
                !savedInstanceState.containsKey(LIFECYCLE_PAGE_NO_KEY) ||
                !savedInstanceState.containsKey(LIFECYCLE_SORTING_PARAMETER_KEY) ||
                !savedInstanceState.containsKey(LIFECYCLE_ACTIONBAR_TITLE)) {

            loadMoviesData(sortingParameter);
        } else {
            movieList = savedInstanceState.getParcelableArrayList(LIFECYCLE_MOVIE_CALLBACKS_KEY);
            page_no = savedInstanceState.getInt(LIFECYCLE_PAGE_NO_KEY);
            sortingParameter = savedInstanceState.getString(LIFECYCLE_SORTING_PARAMETER_KEY);
            actionBar.setTitle(savedInstanceState.getString(LIFECYCLE_ACTIONBAR_TITLE));
            moviesAdapter.setMoviesData(movieList);
        }

    }


    // initialize and setup views including the recycler scroll listener
    private void setUpViews() {

        coordinatorLayout = (CoordinatorLayout) findViewById(R.id.coordinator_layout);
        mLoadingIndicator = (ProgressBar) findViewById(R.id.pb_loading_indicator);

        //recycler setup
        moviesRecyclerView = (AutofitRecyclerView) findViewById(R.id.rv_movies);
        moviesAdapter = new MoviesAdapter(this, movieList);
        moviesRecyclerView.addItemDecoration(new RecyclerInsetsDecoration(this));
        addScrollListener();
        moviesRecyclerView.setAdapter(moviesAdapter);


    }

    //scroll listener to load more movies when end of list is reached
    private void addScrollListener() {
        moviesRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                int visibleItemCount = moviesRecyclerView.getLayoutManager().getChildCount();
                int totalItemCount = moviesRecyclerView.getLayoutManager().getItemCount();
                int pastVisibleItems = ((GridLayoutManager) moviesRecyclerView.getLayoutManager())
                        .findFirstVisibleItemPosition();

                if ((visibleItemCount + pastVisibleItems) >= totalItemCount && !loadingMore && !sortingParameter.contentEquals(NetworkUtils.SORT_FAVORITE))
                    onLoadMoreData();

            }
        });
    }


    // method to get initial list of movies using the retrofit lib.
    public void getMoviesList(String sortingParameter) {
        page_no = 1;
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://api.themoviedb.org/3/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        ApiInterface request = retrofit.create(ApiInterface.class);
        request.getMovies(sortingParameter, NetworkUtils.API_KEY, String.valueOf(page_no)).enqueue(new Callback<MoviesResultData>() {
            @Override
            public void onResponse(Call<MoviesResultData> call, Response<MoviesResultData> response) {

                if (response.isSuccessful()) {

                    MoviesResultData data = response.body();
                    movieList = data.getResults();
                    initiateMovieList(movieList);
                    Log.d("MainActivity", "loaded from API");
                } else {
                    showErrorMessage();

                }
            }

            @Override
            public void onFailure(Call<MoviesResultData> call, Throwable t) {
                showErrorMessage();
                Log.d("MainActivity", t.toString());

            }
        });
    }



    private void doLoadMoreMoviesFromServer(String sortingParameter, int page_no) {

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://api.themoviedb.org/3/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        ApiInterface request = retrofit.create(ApiInterface.class);


        request.getMovies(sortingParameter, NetworkUtils.API_KEY, String.valueOf(page_no)).enqueue(new Callback<MoviesResultData>() {
            @Override
            public void onResponse(Call<MoviesResultData> call, Response<MoviesResultData> response) {

                if (response.isSuccessful()) {

                    MoviesResultData data = response.body();
                    movieList = data.getResults();

                    updateMovieList(movieList);
//                    moviesRecyclerView.invalidate();
//                    moviesAdapter.setMoviesData(movieList);


                    showMoviesDataView();
                    Log.d("MainActivity", "loaded from API");
                } else {
                    showErrorMessage();

                }
            }

            @Override
            public void onFailure(Call<MoviesResultData> call, Throwable t) {

                showErrorMessage();
                Log.d("MainActivity", t.toString());

            }
        });
    }


    private void onLoadMoreData() {
        if (CommonUtils.isNetworkAvailable(this)) {

            if (!sortingParameter.contentEquals(NetworkUtils.SORT_FAVORITE)) {
                doLoadMoreMoviesFromServer(sortingParameter, page_no);

            }
        } else
            showToast(getString(R.string.no_network_text));
    }


    private void updateMovieList(ArrayList<SingleMovie> results) {
        if (results != null) {

            page_no++;
            movieList.addAll(results);
            moviesAdapter.appendMovies(results);
        }
    }

    private void initiateMovieList(ArrayList<SingleMovie> results) {
        if (results != null) {
            page_no++;
            showMoviesDataView();
            movieList = results;
            moviesAdapter.setMoviesData(movieList);
        }
    }


    private void showMoviesDataView() {
        /* First, make sure the error is invisible */

        mLoadingIndicator.setVisibility(View.INVISIBLE);
        /* Then, make sure the weather data is visible */
        moviesRecyclerView.setVisibility(View.VISIBLE);
    }


    private void loadMoviesData(String sortingParameter) {
        mLoadingIndicator.setVisibility(View.VISIBLE);
        moviesRecyclerView.setVisibility(View.INVISIBLE);
//        new FetchMoviesTask().execute();

        getMoviesList(sortingParameter);

    }

    private void setFavoriteData() {
//        movieList = dbHelper.getAllFavorites();
//        moviesAdapter.setMoviesData(movieList);
//        showMoviesDataView();
        getSupportLoaderManager().initLoader(ID_FAVORITE_MOVIE_LOADER, null, this);
    }


    private void showErrorMessage() {
        /* First, hide the currently visible data */
        mLoadingIndicator.setVisibility(View.INVISIBLE);

        /* Then, show the error */
        snackbar = Snackbar.make(coordinatorLayout, "No Internet Connection", Snackbar.LENGTH_INDEFINITE);
        snackbar.setAction("RETRY", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadMoviesData(sortingParameter);
            }
        });
        snackbar.show();
    }


    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putParcelableArrayList(LIFECYCLE_MOVIE_CALLBACKS_KEY, movieList);
        outState.putInt(LIFECYCLE_PAGE_NO_KEY, page_no);
        outState.putString(LIFECYCLE_SORTING_PARAMETER_KEY, sortingParameter);
        outState.putString(LIFECYCLE_ACTIONBAR_TITLE, actionBarTitle);
        super.onSaveInstanceState(outState);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the main; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);


        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_popular) {

            sortingParameter = NetworkUtils.SORT_POPULAR;
            loadMoviesData(sortingParameter);
            actionBarTitle = "Popular Movies";
            actionBar.setTitle(actionBarTitle);

        } else if (id == R.id.action_toprated) {

            sortingParameter = NetworkUtils.SORT_TOP_RATED;
            loadMoviesData(sortingParameter);
            actionBarTitle = "Top Rated Movies";
            actionBar.setTitle(actionBarTitle);

        } else if (id == R.id.action_favorites) {
            if (snackbar != null) {
                snackbar.dismiss();
            }
            sortingParameter = NetworkUtils.SORT_FAVORITE;
            setFavoriteData();
            actionBarTitle = "Favorite Movies";
            actionBar.setTitle(actionBarTitle);
        }


        return super.onOptionsItemSelected(item);
    }

    void showToast(String text) {
        if (mCurrentToast != null) {
            mCurrentToast.cancel();
        }
        mCurrentToast = Toast.makeText(this, text, Toast.LENGTH_LONG);
        mCurrentToast.show();

    }


    @Override
    public Loader<Cursor> onCreateLoader(int id, final Bundle loaderArgs) {

        return new AsyncTaskLoader<Cursor>(this) {

            // Initialize a Cursor, this will hold all the task data
            Cursor mMovieData = null;

            // onStartLoading() is called when a loader first starts loading data
            @Override
            protected void onStartLoading() {
                if (mMovieData != null) {
                    // Delivers any previously loaded data immediately
                    deliverResult(mMovieData);
                } else {
                    // Force a new load
                    forceLoad();
                }
            }

            // loadInBackground() performs asynchronous loading of data
            @Override
            public Cursor loadInBackground() {
                // Will implement to load data

                // Query and load all task data in the background; sort by priority
                // [Hint] use a try/catch block to catch any errors in loading data

                try {
                    return getContentResolver().query(MoviesContract.MoviesEntry.CONTENT_URI,
                            null,
                            null,
                            null,
                            null);

                } catch (Exception e) {
                    Log.e(TAG, "Failed to asynchronously load data.");
                    e.printStackTrace();
                    return null;
                }
            }

            // deliverResult sends the result of the load, a Cursor, to the registered listener
            public void deliverResult(Cursor data) {
                mMovieData = data;
                super.deliverResult(data);
            }
        };

    }





        @Override
        public void onLoadFinished (Loader <Cursor> loader, Cursor data){
            ArrayList<SingleMovie> movie_list_from_cursor =new ArrayList<>();
            if (data!=null) {
                if (data.moveToFirst()) {
                    do {
                        SingleMovie movie = new SingleMovie();
                        movie.setId(data.getInt((data.getColumnIndex(MoviesContract.MoviesEntry._ID))));
                        movie.setTitle((data.getString(data.getColumnIndex(MoviesContract.MoviesEntry.COLUMN_TITLE))));
                        movie.setOverview((data.getString(data.getColumnIndex(MoviesContract.MoviesEntry.COLUMN_OVERVIEW))));
                        movie.setVoteAverage((data.getDouble(data.getColumnIndex(MoviesContract.MoviesEntry.COLUMN_RATING))));
                        movie.setReleaseDate((data.getString(data.getColumnIndex(MoviesContract.MoviesEntry.COLUMN_RELEASE_DATE))));
                        movie.setId((data.getInt(data.getColumnIndex(MoviesContract.MoviesEntry.COLUMN_MOVIE_ID))));
                        movie.setFavorite((data.getInt(data.getColumnIndex(MoviesContract.MoviesEntry.COLUMN_FAVOURITE))));
                        movie.setPosterPath((data.getString(data.getColumnIndex(MoviesContract.MoviesEntry.COLUMN_POSTER_PATH))));
                        movie.setBackdropPath((data.getString(data.getColumnIndex(MoviesContract.MoviesEntry.COLUMN_BACKGROUND_IMAGE_PATH))));
                        movie.setOriginalLanguage((data.getString(data.getColumnIndex(MoviesContract.MoviesEntry.COLUMN_LANGUAGE))));
                        movie.setGenreIdStrings((data.getString(data.getColumnIndex(MoviesContract.MoviesEntry.COLUMN_GENRE_ARRAY_STRING))));

                        movie_list_from_cursor.add(movie);
                    } while (data.moveToNext());
                }
                movieList = movie_list_from_cursor;
                moviesAdapter.setMoviesData(movieList);
                showMoviesDataView();

            }else{
                showToast("Cursor is empty");

            }
        }

        @Override
        public void onLoaderReset (Loader <Cursor> loader) {

        }


    }

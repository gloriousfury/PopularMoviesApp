package com.example.android.popularmoviesapp;

import android.content.res.Resources;
import android.graphics.Rect;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.util.TypedValue;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;


import com.example.android.popularmoviesapp.model.MoviesResultData;
import com.example.android.popularmoviesapp.model.SingleMovie;
import com.example.android.popularmoviesapp.utilities.ApiInterface;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {

    RecyclerView mRecyclerView;
    MoviesAdapter moviesAdapter;
    ProgressBar mLoadingIndicator;
    String sortingParameter = "popular";
    List<SingleMovie> movieList = new ArrayList<>();
    CoordinatorLayout coordinatorLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        coordinatorLayout = (CoordinatorLayout) findViewById(R.id.coordinator_layout);
        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerview);
        mRecyclerView.setHasFixedSize(true);

        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(this, 2);
        mRecyclerView.setLayoutManager(layoutManager);
        moviesAdapter = new MoviesAdapter(this, movieList);
        mRecyclerView.addItemDecoration(new GridSpacingItemDecoration(2, dpToPx(5), true));
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());

        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mRecyclerView.setAdapter(moviesAdapter);

        mLoadingIndicator = (ProgressBar) findViewById(R.id.pb_loading_indicator);

        /* Once all of our views are setup, we can load the weather data. */
        loadMoviesData(sortingParameter);


    }


    private void showMoviesDataView() {
        /* First, make sure the error is invisible */

        mLoadingIndicator.setVisibility(View.INVISIBLE);
        /* Then, make sure the weather data is visible */
        mRecyclerView.setVisibility(View.VISIBLE);
    }


    private void loadMoviesData(String sortingParameter) {
        mLoadingIndicator.setVisibility(View.VISIBLE);
        mRecyclerView.setVisibility(View.INVISIBLE);
//        new FetchMoviesTask().execute();

        getMoviesList(sortingParameter);
    }


    private void showErrorMessage() {
        /* First, hide the currently visible data */
        mLoadingIndicator.setVisibility(View.INVISIBLE);
        mRecyclerView.setVisibility(View.INVISIBLE);
        /* Then, show the error */
        Snackbar snackbar = Snackbar.make(coordinatorLayout, "No Internet Connection", Snackbar.LENGTH_INDEFINITE);
        snackbar.setAction("RETRY", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadMoviesData(sortingParameter);
            }
        });
        snackbar.show();
    }

    public void getMoviesList(String sortingParameter) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://api.themoviedb.org/3/movie/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        ApiInterface request = retrofit.create(ApiInterface.class);
        String moviesRequestUrl = sortingParameter + getResources().getString(R.string.movie_db_api_query);
        request.getMovieList(moviesRequestUrl).enqueue(new Callback<MoviesResultData>() {
            @Override
            public void onResponse(Call<MoviesResultData> call, Response<MoviesResultData> response) {

                if (response.isSuccessful()) {

                    MoviesResultData data = response.body();
                    movieList = data.getResults();

                    mRecyclerView.invalidate();
                    moviesAdapter.setMoviesData(movieList);


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

    private int dpToPx(int dp) {
        Resources r = getResources();
        return Math.round(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, r.getDisplayMetrics()));
    }


    //gridview decoration
    private class GridSpacingItemDecoration extends RecyclerView.ItemDecoration {

        private int spanCount;
        private int spacing;
        private boolean includeEdge;

        //gridview decoration

        private GridSpacingItemDecoration(int spanCount, int spacing, boolean includeEdge) {
            this.spanCount = spanCount;
            this.spacing = spacing;
            this.includeEdge = includeEdge;
        }

        @Override
        public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
            int position = parent.getChildAdapterPosition(view); // item position
            int column = position % spanCount; // item column

            if (includeEdge) {
                outRect.left = spacing - column * spacing / spanCount; // spacing - column * ((1f / spanCount) * spacing)
                outRect.right = (column + 1) * spacing / spanCount; // (column + 1) * ((1f / spanCount) * spacing)

                if (position < spanCount) { // top edge
                    outRect.top = spacing;
                }
                outRect.bottom = spacing; // item bottom
            } else {
                outRect.left = column * spacing / spanCount; // column * ((1f / spanCount) * spacing)
                outRect.right = spacing - (column + 1) * spacing / spanCount; // spacing - (column + 1) * ((1f /    spanCount) * spacing)
                if (position >= spanCount) {
                    outRect.top = spacing; // item top
                }
            }
        }
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
            sortingParameter = "popular";
            loadMoviesData(sortingParameter);

        } else if (id == R.id.action_toprated) {

            sortingParameter = "top_rated";
            loadMoviesData(sortingParameter);

        }


        return super.onOptionsItemSelected(item);
    }


}

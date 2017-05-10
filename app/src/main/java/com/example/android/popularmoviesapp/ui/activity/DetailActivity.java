package com.example.android.popularmoviesapp.ui.activity;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.AppCompatActivity;

import android.support.v7.widget.Toolbar;

import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;

import android.widget.TextView;
import android.widget.Toast;

import com.example.android.popularmoviesapp.R;
import com.example.android.popularmoviesapp.model.SingleMovie;

import com.example.android.popularmoviesapp.ui.fragment.OverviewFragment;
import com.example.android.popularmoviesapp.ui.fragment.ReviewFragment;
import com.example.android.popularmoviesapp.ui.fragment.TrailersFragment;
import com.example.android.popularmoviesapp.utils.MoviesContract;
import com.example.android.popularmoviesapp.utils.MoviesDatabaseHelper;
import com.squareup.picasso.Picasso;


public class DetailActivity extends AppCompatActivity implements View.OnClickListener {


    String backdrop_path, get_title, get_language, get_ratings, get_releasedate, release_date, get_genre_string;
    public static String get_overview;
    public static int id;
    SingleMovie movie;
    DetailsSectionPagerAdapter detailsSectionPagerAdpter;
    TabLayout tablayout;
    ViewPager viewPager;
    ImageView backdropImg;
    TextView title, language_date, ratings, genre;
    View favorite;
    SQLiteDatabase db;
    Toast mCurrentToast;
    FloatingActionButton fab;

    MoviesDatabaseHelper dbHelper;
    NestedScrollView scrollView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);


        //setup collapsing toolbar
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {

            getSupportActionBar().setHomeButtonEnabled(true);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            CollapsingToolbarLayout collapsingToolbar = (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar);
            collapsingToolbar.setTitle(" ");

        }


        setUpViews();


    }

    private void setUpViews() {


        backdropImg = (ImageView) findViewById(R.id.img_backdrop);
        fab = (FloatingActionButton) findViewById(R.id.addtofavorites);
        fab.setOnClickListener(this);

        title = (TextView) findViewById(R.id.tv_movie_title);
        language_date = (TextView) findViewById(R.id.tv_movie_language_date);
//        overview = (TextView) findViewById(R.id.tv_movie_overview);
        ratings = (TextView) findViewById(R.id.tv_movie_ratings);
        genre = (TextView) findViewById(R.id.tv_movie_genres);
        scrollView = (NestedScrollView) findViewById(R.id.nested_scrollview);
        if (scrollView != null) {
            scrollView.setFillViewport(true);
        }


        tablayout = (TabLayout) findViewById(R.id.movie_details_tab);
        viewPager = (ViewPager) findViewById(R.id.movie_details_ontainer);
        dbHelper = new MoviesDatabaseHelper(this);

        detailsSectionPagerAdpter = new DetailsSectionPagerAdapter(getSupportFragmentManager());

        viewPager.setAdapter(detailsSectionPagerAdpter);

        tablayout.setupWithViewPager(viewPager);


        //recieve and display data gotten from previous activity
        Intent getMovieData = getIntent();
        movie = getMovieData.getParcelableExtra("movieItem");

        if (movie != null) {

//            poster_path = "http://image.tmdb.org/t/p/" + "w185/" + movie.getPosterPath();
            backdrop_path = "http://image.tmdb.org/t/p/" + "w342/" + movie.getBackdropPath();
//            Picasso.with(this).load(poster_path).into(posterImg);
            Picasso.with(this).load(backdrop_path).into(backdropImg);
            id = movie.getId();
            get_title = movie.getTitle();
            get_language = movie.getOriginalLanguage();
            get_releasedate = movie.getReleaseDate();
            get_overview = movie.getOverview();
            get_ratings = "" + movie.getVoteAverage();
            get_genre_string = movie.getGenreIdStrings();
            genre.setText(get_genre_string);


            release_date = get_releasedate.substring(0, 4);

            title.setText(get_title);
            language_date.setText(release_date + " | " + get_language.toUpperCase());


            ratings.setText(get_ratings);


        }


        if (movieIsStored()) {

            fab.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.ic_star_favorite_24dp));


        } else {
            fab.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.ic_star_normal_24dp));
        }


    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {

            case R.id.addtofavorites:
                if (movieIsStored()) {
                    dbHelper.removeFavorites(movie.getId());
                    fab.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.ic_star_normal_24dp));
                    showToast("Removed from favorites");

                } else {
                    fab.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.ic_star_favorite_24dp));
                    showToast("Added to favorites");
                    dbHelper.addFavorites(movie);


                }

                break;


        }
    }

    private boolean movieIsStored() {
        db = dbHelper.getReadableDatabase();
        String whereClause = MoviesContract.MoviesEntry.COLUMN_MOVIE_ID + "=?";
        String[] whereArgs = {String.valueOf(id)};
        Cursor cursor = db.query(
                MoviesContract.MoviesEntry.TABLE_NAME,
                null,
                whereClause,
                whereArgs,
                null,
                null,
                null
        );

        if (cursor.getCount() == 0) {
//            Toast.makeText(this,"Movie is not Stored",Toast.LENGTH_LONG).show();
            cursor.close();
            return false;

        } else {
            cursor.close();
//            Toast.makeText(this,"Movie is Stored",Toast.LENGTH_LONG).show();
            return true;

        }


    }


    private class DetailsSectionPagerAdapter extends FragmentPagerAdapter {

        private DetailsSectionPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            if (position == 0) {
                return OverviewFragment.newInstance();
            } else if (position == 1) {
                return TrailersFragment.newInstance();
            } else if (position == 2) {
                return ReviewFragment.newInstance();
            } else {
                return OverviewFragment.newInstance();
            }
        }

        @Override
        public int getCount() {
            // Show 3 total pages.
            return 3;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return "Overview";
                case 1:
                    return "Trailers";
                case 2:
                    return "Reviews";

            }
            return null;
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                // API 5+ solution
                onBackPressed();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    void showToast(String text) {
        if (mCurrentToast != null) {
            mCurrentToast.cancel();
        }
        mCurrentToast = Toast.makeText(this, text, Toast.LENGTH_LONG);
        mCurrentToast.show();

    }

}

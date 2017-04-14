package com.example.android.popularmoviesapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class DetailActivity extends AppCompatActivity {

    ImageView posterImg;
    ImageView backdropImg;
    TextView title, language, overview, ratings, genre;

    final String POSTER_PATH = "poster_path";
    final String OVERVIEW = "overview";
    final String RELEASE_DATE = "release_date";
    final String GENRES = "genres";
    final String ORIGINAL_LANGUAGE = "original_language";
    final String TITLE = "title";
    final String BACKDROP_PATH = "backdrop_path";
    final String VOTE_AVERAGE = "vote_average";
    ArrayList<Integer> genre_array;
    String poster_path, backdrop_path, get_title, get_language, get_overview, get_ratings, get_releasedate, release_date;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if(getSupportActionBar() != null) {

            getSupportActionBar().setHomeButtonEnabled(true);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            CollapsingToolbarLayout collapsingToolbar = (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar);
            collapsingToolbar.setTitle(" ");

        }


        posterImg = (ImageView) findViewById(R.id.img_movie_poster);
        backdropImg = (ImageView) findViewById(R.id.img_backdrop);

        title = (TextView) findViewById(R.id.tv_movie_title);
        language = (TextView) findViewById(R.id.tv_movie_language);
        overview = (TextView) findViewById(R.id.tv_movie_overview);
        ratings = (TextView) findViewById(R.id.tv_movie_ratings);
        genre = (TextView) findViewById(R.id.tv_movie_genres);

        Intent getMovieData = getIntent();
        if (getMovieData != null) {

            poster_path = "http://image.tmdb.org/t/p/" + "w185/" + getMovieData.getStringExtra(POSTER_PATH);
            backdrop_path = "http://image.tmdb.org/t/p/" + "w342/" + getMovieData.getStringExtra(BACKDROP_PATH);
            Picasso.with(this).load(poster_path).into(posterImg);
            Picasso.with(this).load(backdrop_path).into(backdropImg);
            get_title = getMovieData.getStringExtra(TITLE);
            get_language = getMovieData.getStringExtra(ORIGINAL_LANGUAGE);
            get_overview = getMovieData.getStringExtra(OVERVIEW);
            get_ratings = getMovieData.getStringExtra(VOTE_AVERAGE);
            genre_array = getMovieData.getIntegerArrayListExtra(GENRES);
            get_releasedate = getMovieData.getStringExtra(RELEASE_DATE);
            release_date = " (" + get_releasedate.substring(0, 4) + ")";

            title.setText(get_title + release_date);
            language.setText(get_language);
            overview.setText(get_overview);
            ratings.setText(get_ratings);
            getMovieGenre(genre_array, genre);


        }


    }

    // genre_ids
    public static void getMovieGenre(List<Integer> genre_array, TextView genreTextView) {
        // genre_ids
        StringBuilder builder = new StringBuilder();
        StringBuilder genreToBePassedBuilder = new StringBuilder();

        Map<Integer, String> genreMap = new HashMap<Integer, String>() {
            {
                put(28, "Action");
                put(12, "Adventure");
                put(16, "Animation");
                put(35, "Comedy");
                put(80, "Crime");
                put(99, "Documentary");
                put(18, "Drama");
                put(10751, "Family");
                put(14, "Fantasy");
                put(10769, "Foreign");
                put(36, "History");
                put(27, "Horror");
                put(10402, "Music");
                put(9648, "Mystery");
                put(10749, "Romance");
                put(878, "Science Fiction");
                put(10770, "TV Movie");
                put(53, "Thriller");
                put(10752, "War");
                put(37, "Western");
            }
        };

        String genreString = "";
        int genre_int = 0;
        for (int str : genre_array) {
            genre_int += str;
            for (Map.Entry<Integer, String> entry : genreMap.entrySet()) {
                int key = entry.getKey();
                String value = entry.getValue();
                if (genre_int == key) {
                    builder.append(value + "  |  ");
                    genreToBePassedBuilder.append(value + "");
                    genreString += builder;


                }
            }
        }
        genreString = genreString.length() > 0 ? genreString.substring(0,
                genreString.length() - 2) : genreString;
        genreTextView.setText(genreString);
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

}

package com.example.android.popularmoviesapp.utils;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.android.popularmoviesapp.model.SingleMovie;

import java.util.ArrayList;

/**
 * Created by OLORIAKE KEHINDE on 4/29/2017.
 */

public class MoviesDatabaseHelper extends SQLiteOpenHelper {

    // Logcat tag
    private static final String LOG = MoviesDatabaseHelper.class.getName();

    // Database Version
    private static final int DATABASE_VERSION = 1;

    // Database Name
    private static final String DATABASE_NAME = "movies_database";


    // Table Create Statements
    // Todo table create statement
    private static final String CREATE_TABLE_MOVIES = "CREATE TABLE "
            + MoviesContract.MoviesEntry.TABLE_NAME + "("
            + MoviesContract.MoviesEntry._ID + " INTEGER PRIMARY KEY,"
            + MoviesContract.MoviesEntry.COLUMN_TITLE + " TEXT,"
            + MoviesContract.MoviesEntry.COLUMN_OVERVIEW + " TEXT,"
            + MoviesContract.MoviesEntry.COLUMN_POSTER_PATH + " TEXT,"
            + MoviesContract.MoviesEntry.COLUMN_BACKGROUND_IMAGE_PATH + " TEXT,"
            + MoviesContract.MoviesEntry.COLUMN_LANGUAGE + " TEXT,"
            + MoviesContract.MoviesEntry.COLUMN_RATING + " INTEGER,"
            + MoviesContract.MoviesEntry.COLUMN_RELEASE_DATE + " TEXT,"
            + MoviesContract.MoviesEntry.COLUMN_GENRE_ARRAY_STRING + " TEXT,"
            + MoviesContract.MoviesEntry.COLUMN_MOVIE_ID + " INTEGER,"
            + MoviesContract.MoviesEntry.COLUMN_FAVOURITE + " INTEGER"
             + ")";

    public MoviesDatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }


    @Override
    public void onCreate(SQLiteDatabase db) {

        // creating required tables
        db.execSQL(CREATE_TABLE_MOVIES);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // on upgrade drop older tables
        db.execSQL("DROP TABLE IF EXISTS " + MoviesContract.MoviesEntry.TABLE_NAME);
        // create new tables
        onCreate(db);
    }

    // ------------------------ "movies" table methods ----------------//

    /**
     * Creating a movie
     */
    public long addFavorites(SingleMovie movie) {

        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(MoviesContract.MoviesEntry.COLUMN_TITLE, movie.getTitle());
        values.put(MoviesContract.MoviesEntry.COLUMN_OVERVIEW, movie.getOverview());
        values.put(MoviesContract.MoviesEntry.COLUMN_POSTER_PATH, movie.getPosterPath());
        values.put(MoviesContract.MoviesEntry.COLUMN_BACKGROUND_IMAGE_PATH, movie.getBackdropPath());
        values.put(MoviesContract.MoviesEntry.COLUMN_RATING, movie.getVoteAverage());
        values.put(MoviesContract.MoviesEntry.COLUMN_RELEASE_DATE, movie.getReleaseDate());
        values.put(MoviesContract.MoviesEntry.COLUMN_LANGUAGE, movie.getOriginalLanguage());
        values.put(MoviesContract.MoviesEntry.COLUMN_MOVIE_ID, movie.getId());
        values.put(MoviesContract.MoviesEntry.COLUMN_GENRE_ARRAY_STRING, movie.getGenreIdStrings());
        values.put(MoviesContract.MoviesEntry.COLUMN_FAVOURITE, movie.getFavorite());
        // insert row
        long movie_id = db.insert(MoviesContract.MoviesEntry.TABLE_NAME, null, values);
        return movie_id;
    }

    public boolean removeFavorites(Integer movieId) {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete(MoviesContract.MoviesEntry.TABLE_NAME,MoviesContract.MoviesEntry.COLUMN_MOVIE_ID + "=" + movieId, null) > 0;
    }


    public ArrayList<SingleMovie> getAllFavorites() {
        ArrayList<SingleMovie> movie_list = new ArrayList<>();


        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c =  db.query(
                MoviesContract.MoviesEntry.TABLE_NAME,
                null,
                null,
                null,
                null,
                null,
                null
        );

        // looping through all rows and adding to list
        if (c.moveToFirst()) {
            do {


                SingleMovie movie= new SingleMovie();
                movie.setId(c.getInt((c.getColumnIndex(MoviesContract.MoviesEntry._ID))));
                movie.setTitle((c.getString(c.getColumnIndex(MoviesContract.MoviesEntry.COLUMN_TITLE))));
                movie.setOverview((c.getString(c.getColumnIndex(MoviesContract.MoviesEntry.COLUMN_OVERVIEW))));
                movie.setVoteAverage((c.getDouble(c.getColumnIndex(MoviesContract.MoviesEntry.COLUMN_RATING))));
                movie.setReleaseDate((c.getString(c.getColumnIndex(MoviesContract.MoviesEntry.COLUMN_RELEASE_DATE))));
                movie.setId((c.getInt(c.getColumnIndex(MoviesContract.MoviesEntry.COLUMN_MOVIE_ID))));
                movie.setFavorite((c.getInt(c.getColumnIndex(MoviesContract.MoviesEntry.COLUMN_FAVOURITE))));
                movie.setPosterPath((c.getString(c.getColumnIndex(MoviesContract.MoviesEntry.COLUMN_POSTER_PATH))));
                movie.setBackdropPath((c.getString(c.getColumnIndex(MoviesContract.MoviesEntry.COLUMN_BACKGROUND_IMAGE_PATH))));
                movie.setOriginalLanguage((c.getString(c.getColumnIndex(MoviesContract.MoviesEntry.COLUMN_LANGUAGE))));
                movie.setGenreIdStrings((c.getString(c.getColumnIndex(MoviesContract.MoviesEntry.COLUMN_GENRE_ARRAY_STRING))));


                movie_list.add(movie);
            } while (c.moveToNext());
        }

        return movie_list;
    }


}

package com.example.android.popularmoviesapp.adapter;


import android.content.Context;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android.popularmoviesapp.R;
import com.example.android.popularmoviesapp.model.SingleMovie;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * Created by Jedidiah on 06/04/2017.
 */

public class MovieAdapterGrid extends BaseAdapter {

    private Context mContext;
    private List<SingleMovie> movieArrayList;
    public ArrayList<String> genreStringArray;

    public MovieAdapterGrid(Context mContext, List<SingleMovie> movieArrayList) {
        this.mContext = mContext;
        this.movieArrayList = movieArrayList;
    }

    @Override
    public int getCount() {
        return movieArrayList.size();
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View convertView, ViewGroup viewGroup) {
        final SingleMovie movie = movieArrayList.get(i);
        String title = movie.getTitle();
        String poster_img_path = "http://image.tmdb.org/t/p/" + "w185/" + movie.getPosterPath();

        // view holder pattern
        if (convertView == null) {

            convertView = LayoutInflater.from(mContext).inflate(R.layout.movies_list_item, viewGroup, false);
            final TextView movieTitle = (TextView) convertView.findViewById(R.id.tv_movie_title);
            final TextView movieGenres = (TextView) convertView.findViewById(R.id.tv_movie_genres);
            final ImageView moviePoster = (ImageView) convertView.findViewById(R.id.img_movie_poster);
            final ViewHolder viewHolder = new ViewHolder(movieTitle, movieGenres, moviePoster);
            convertView.setTag(viewHolder);
        }


        final ViewHolder viewHolder = (ViewHolder) convertView.getTag();
        viewHolder.movieTitle.setText(title);
        Picasso.with(mContext).load(poster_img_path).into(viewHolder.imgPoster);



        if(movie.getGenreIds()!=null) {
//            ArrayList<Integer> genreArrays = movie.getGenreIds();
//            genreStringArray = getMovieGenre(genreArrays);
//            movie.setGenreIdStrings(genreStringArray);
//            viewHolder.movieGenre.setText(String.valueOf(movie.getVoteAverage())+" rating");
        }


        return convertView;
    }

    public void setMovieData(List<SingleMovie> movieData) {
        movieArrayList = movieData;
        notifyDataSetChanged();
    }

    // Your "view holder" that holds references to each subview
    private class ViewHolder {
        private final TextView movieTitle;
        private final TextView movieGenre;
        private final ImageView imgPoster;


        public ViewHolder(TextView movieTitle, TextView movieGenre, ImageView imgPoster) {
            this.movieTitle = movieTitle;
            this.movieGenre = movieGenre;
            this.imgPoster = imgPoster;

        }
    }


    // genre_ids
    public static ArrayList<String> getMovieGenre(List<Integer> genre_array) {


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

        String genreString;
        int genre_int;
        ArrayList<String> genreStringArray = new ArrayList<>();

        for (int i = 0; i < genre_array.size(); i++) {
            genre_int = genre_array.get(i);
            genreString = genreMap.get(genre_int);
            genreStringArray.add(genreString);
        }

        return genreStringArray;


    }
}
/*
 * Copyright (C) 2016 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.example.android.popularmoviesapp;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;


import com.example.android.popularmoviesapp.model.SingleMovie;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class MoviesAdapter extends RecyclerView.Adapter<MoviesAdapter.ViewHolder> {
    Context context;
    private List<SingleMovie> movies_list;
    private final String POSTER_PATH = "poster_path";
    private final String OVERVIEW = "overview";
    private final String RELEASE_DATE = "release_date";
    private final String GENRES = "genres";
    private final String ORIGINAL_LANGUAGE = "original_language";
    final String TITLE = "title";
    final String BACKDROP_PATH = "backdrop_path";
    final String VOTE_AVERAGE = "vote_average";
    String genre_data;

    public MoviesAdapter(Context context, List<SingleMovie> ProfileList) {
        this.context = context;
        this.movies_list = ProfileList;


    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public final TextView movieTitle;
        public final TextView movieGenres;
        public final ImageView moviePoster;

        public ViewHolder(View view) {
            super(view);
            view.setClickable(true);
            view.setOnClickListener(this);
            movieTitle = (TextView) view.findViewById(R.id.tv_movie_title);
            movieGenres = (TextView) view.findViewById(R.id.tv_movie_genres);
            moviePoster = (ImageView) view.findViewById(R.id.img_movie_poster);

        }

        @Override
        public void onClick(View v) {

            String poster_path = movies_list.get(getAdapterPosition()).getPosterPath();
            String overview = movies_list.get(getAdapterPosition()).getOverview();
            String release_date = movies_list.get(getAdapterPosition()).getReleaseDate();

            String original_language = movies_list.get(getAdapterPosition()).getOriginalLanguage();
            String title = movies_list.get(getAdapterPosition()).getTitle();
            String backdrop_path = movies_list.get(getAdapterPosition()).getBackdropPath();
            String vote_average = String.valueOf(movies_list.get(getAdapterPosition()).getVoteAverage());


            Intent openDetailActivity = new Intent(context, DetailActivity.class);
            openDetailActivity.putExtra(POSTER_PATH, poster_path);
            openDetailActivity.putExtra(OVERVIEW, overview);
            openDetailActivity.putExtra(GENRES, genre_data);
            openDetailActivity.putExtra(RELEASE_DATE, release_date);
            openDetailActivity.putExtra(ORIGINAL_LANGUAGE, original_language);
            openDetailActivity.putExtra(TITLE, title);
            openDetailActivity.putExtra(BACKDROP_PATH, backdrop_path);
            openDetailActivity.putExtra(VOTE_AVERAGE, vote_average);
            openDetailActivity.putIntegerArrayListExtra(GENRES, movies_list.get(getAdapterPosition()).getGenreIds());
            context.startActivity(openDetailActivity);

        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.movies_list_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        String title = movies_list.get(position).getTitle();
        String poster_img_path = "http://image.tmdb.org/t/p/" + "w185/" + movies_list.get(position).getPosterPath();
       getMovieGenre(movies_list.get(position).getGenreIds(), holder.movieGenres);

        holder.movieTitle.setText(title);

        Picasso.with(context).load(poster_img_path).into(holder.moviePoster);

    }

    @Override
    public int getItemCount() {
        return movies_list.size();
    }

    public static void getMovieGenre( List<Integer> genre_array, TextView genreTextView){

        StringBuilder builder = new StringBuilder();
       ;

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

        String genreString= "";
        int genre_int = 0;

        for (int str : genre_array) {
            genre_int += str  ;
            for (Map.Entry<Integer, String> entry : genreMap.entrySet()) {
                int key = entry.getKey();
                String value = entry.getValue();
                if (genre_int == key) {
                    builder.append(value + ", ");
                    genreString += builder;


                }
            }

        }
        genreString = genreString.length() > 0 ? genreString.substring(0,
                genreString.length() - 2) : genreString;
        genreTextView.setText(genreString);



    }


    public void setMoviesData(List<SingleMovie> moviesData) {
        movies_list = moviesData;
        notifyDataSetChanged();
    }

}


package com.example.android.popularmoviesapp.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;


import com.example.android.popularmoviesapp.R;
import com.example.android.popularmoviesapp.model.SingleMovie;
import com.example.android.popularmoviesapp.ui.activity.DetailActivity;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class MoviesAdapter extends RecyclerView.Adapter<MoviesAdapter.ViewHolder> {
    Context context;
    private List<SingleMovie> movies_list;
//    private RecyclerViewClickListener mOnClickListener;
    public String genreArrayString;
    String genreString;


    public MoviesAdapter(Context context,
                         List<SingleMovie> MovieList
                         ) {
        this.context = context;
        this.movies_list = MovieList;


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
        public void onClick(View view) {
            SingleMovie movie = movies_list.get(getAdapterPosition());

            Intent intent = new Intent(context, DetailActivity.class);
            intent.putExtra("movieItem", movie);
            context.startActivity(intent);

//            int clickedPosition = getAdapterPosition();
//            mOnClickListener.onListItemClick(clickedPosition);
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
        double ratings = movies_list.get(position).getVoteAverage();
        String poster_img_path = "http://image.tmdb.org/t/p/" + "w185/" + movies_list.get(position).getPosterPath();


        if (movies_list.get(position).getGenreIds() != null) {
            ArrayList<Integer> genreArrays = movies_list.get(position).getGenreIds();
            genreArrayString = getMovieGenre(genreArrays);
            movies_list.get(position).setGenreIdStrings(genreArrayString);
        }

        holder.movieTitle.setText(title);
        holder.movieGenres.setText(ratings + " rating");

        Picasso.with(context).load(poster_img_path).into(holder.moviePoster);

    }

    @Override
    public int getItemCount() {
        return movies_list.size();
    }


    public void setMoviesData(List<SingleMovie> moviesData) {
        movies_list = moviesData;
        notifyDataSetChanged();
    }

    public void appendMovies(ArrayList<SingleMovie> movieList) {
        movies_list.addAll(movieList);
        notifyDataSetChanged();
    }

    // genre_ids
    private static String getMovieGenre(List<Integer> genre_array) {


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

        String genreString ="";
        int genre_int;
        ArrayList<String> genreStringArray = new ArrayList<>();

        for (int i = 0; i < genre_array.size(); i++) {
            genre_int = genre_array.get(i);
            genreString = genreMap.get(genre_int);
            genreStringArray.add(genreString);
        }


        if (genreStringArray.size() > 0) {

           genreString = "";
            if (genreStringArray != null) {

                if (genreStringArray.size() == 1) {

                    genreString = genreStringArray.get(0);

                } else {

                    for (int k = 0; k < 2; k++) {

                        genreString += genreStringArray.get(k) + "  |  ";
                    }

                }
            }
        }

        return genreString;


    }
}
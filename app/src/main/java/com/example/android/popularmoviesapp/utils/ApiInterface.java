package com.example.android.popularmoviesapp.utils;


import com.example.android.popularmoviesapp.model.MoviesResultData;
import com.example.android.popularmoviesapp.model.ReviewData;
import com.example.android.popularmoviesapp.model.TrailerData;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;
import retrofit2.http.Url;

public interface ApiInterface {

    @GET
    Call<MoviesResultData> getMovieList(@Url String url);

    @GET
    Call<TrailerData> getTrailerList(@Url String url);

    @GET
    Call<ReviewData> getReviewList(@Url String url);

    @GET("movie/{sort_type}")
    Call<MoviesResultData> getMovies(@Path("sort_type") String sort_type,
                                  @Query("api_key") String api_key,
                                  @Query("page") String page);

}
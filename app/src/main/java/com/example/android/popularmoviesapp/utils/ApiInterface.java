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


    @GET("{id}/videos")
    Call<TrailerData> getTrailers(@Path("id") String id,
                                @Query("api_key") String api_key);

    @GET("{id}/reviews")
    Call<ReviewData> getReviews(@Path("id") String id,
                                @Query("api_key") String api_key);


    @GET("movie/{sort_type}")
    Call<MoviesResultData> getMovies(@Path("sort_type") String sort_type,
                                     @Query("api_key") String api_key,
                                     @Query("page") String page);

}
package com.example.android.popularmoviesapp.utilities;


import com.example.android.popularmoviesapp.model.MoviesResultData;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;
import retrofit2.http.Url;

public interface ApiInterface {

    @GET
    Call<MoviesResultData> getMovieList(@Url String url);


    @GET
    Call<MoviesResultData> getMovieList1(@Path("sort") String sortParameter, @Query("api_key") String apiKey);
}
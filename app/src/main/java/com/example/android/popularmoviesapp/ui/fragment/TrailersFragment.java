package com.example.android.popularmoviesapp.ui.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import android.widget.ProgressBar;

import android.widget.TextView;

import com.example.android.popularmoviesapp.R;
import com.example.android.popularmoviesapp.adapter.ReviewAdapter;
import com.example.android.popularmoviesapp.adapter.TrailerAdapter;
import com.example.android.popularmoviesapp.model.ReviewItem;
import com.example.android.popularmoviesapp.model.TrailerData;
import com.example.android.popularmoviesapp.model.TrailerItem;
import com.example.android.popularmoviesapp.ui.activity.DetailActivity;
import com.example.android.popularmoviesapp.utils.ApiInterface;
import com.example.android.popularmoviesapp.utils.NetworkUtils;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by OLORIAKE KEHINDE on 11/16/2016.
 */

public class TrailersFragment extends Fragment implements View.OnClickListener {


    public static TrailersFragment newInstance() {
        TrailersFragment fragment = new TrailersFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


    ImageView refreshImage;
    TextView errorMessage;
    RecyclerView recyclerView;
    ReviewAdapter reviewAdapter;
    List<ReviewItem> reviewItemList = new ArrayList<>();

    int movieId;
    List<TrailerItem> trailerList;
    ProgressBar trailerProgressBar;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_trailers, container, false);
        trailerProgressBar = (ProgressBar) v.findViewById(R.id.pb_trailer_loader);
        refreshImage = (ImageView) v.findViewById(R.id.refresh);
        refreshImage.setOnClickListener(this);
        errorMessage = (TextView) v.findViewById(R.id.error_message);

        recyclerView = (RecyclerView) v.findViewById(R.id.rv_trailer);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager mlayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(mlayoutManager);
        reviewAdapter = new ReviewAdapter(getContext(), reviewItemList);
        recyclerView.setAdapter(reviewAdapter);


        movieId = DetailActivity.id;


        getTrailers();

        return v;
    }

    private void getTrailers() {
        trailerProgressBar.setVisibility(View.VISIBLE);
        refreshImage.setVisibility(View.INVISIBLE);
        errorMessage.setVisibility(View.INVISIBLE);
        getTrailerList(movieId);
    }


    public void getTrailerList(int id) {
        trailerProgressBar.setVisibility(View.VISIBLE);
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://api.themoviedb.org/3/movie/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        ApiInterface request = retrofit.create(ApiInterface.class);
        String movieId = String.valueOf(id);

        request.getTrailers(movieId, NetworkUtils.API_KEY).enqueue(new Callback<TrailerData>() {
            @Override
            public void onResponse(Call<TrailerData> call, Response<TrailerData> response) {

                if (response.isSuccessful()) {

                    TrailerData data = response.body();
                    trailerList = data.getTrailerItems();

                    TrailerAdapter trailerAdapter = new TrailerAdapter(getActivity(), trailerList);
                    recyclerView.setAdapter(trailerAdapter);
                    trailerProgressBar.setVisibility(View.INVISIBLE);
                    Log.d("Detail Activity", "loaded from API");
                } else {
                    trailerProgressBar.setVisibility(View.INVISIBLE);
                    errorMessage.setText("There are no trailers available here");

                }
            }

            @Override
            public void onFailure(Call<TrailerData> call, Throwable t) {
                trailerProgressBar.setVisibility(View.INVISIBLE);
                refreshImage.setVisibility(View.VISIBLE);
                errorMessage.setVisibility(View.VISIBLE);

                Log.d("DetailActivity", t.toString());

            }
        });
    }


    @Override
    public void onClick(View v) {

        switch (v.getId()){
            case R.id.refresh:

                getTrailers();

                break;



        }


    }


}

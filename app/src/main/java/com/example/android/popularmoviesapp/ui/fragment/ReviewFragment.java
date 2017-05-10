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
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.android.popularmoviesapp.R;
import com.example.android.popularmoviesapp.adapter.ReviewAdapter;
import com.example.android.popularmoviesapp.model.ReviewItem;
import com.example.android.popularmoviesapp.model.ReviewData;
import com.example.android.popularmoviesapp.ui.activity.DetailActivity;
import com.example.android.popularmoviesapp.utils.ApiInterface;

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

public class ReviewFragment extends Fragment implements View.OnClickListener {


    public static ReviewFragment newInstance() {
        ReviewFragment fragment = new ReviewFragment();
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
    ProgressBar progressBar;
    int movieId;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_review, container, false);
        progressBar = (ProgressBar) v.findViewById(R.id.pb_reviewloader);
        refreshImage = (ImageView) v.findViewById(R.id.refresh);
        refreshImage.setOnClickListener(this);
        errorMessage = (TextView) v.findViewById(R.id.error_message);

        recyclerView = (RecyclerView) v.findViewById(R.id.rv_reviews);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager mlayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(mlayoutManager);
        reviewAdapter = new ReviewAdapter(getContext(), reviewItemList);
        recyclerView.setAdapter(reviewAdapter);


        movieId = DetailActivity.id;


        getReviews();

        return v;
    }

    private void getReviews() {
        progressBar.setVisibility(View.VISIBLE);
        refreshImage.setVisibility(View.INVISIBLE);
        errorMessage.setVisibility(View.INVISIBLE);
        progressBar.setVisibility(View.VISIBLE);
        getTrailerList(movieId);
    }


    public void getTrailerList(int id) {
//        trailerProgressBar.setVisibility(View.VISIBLE);
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://api.themoviedb.org/3/movie/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        ApiInterface request = retrofit.create(ApiInterface.class);
        String reviewRequestUrl = String.valueOf(id) + "/reviews" + "?api_key=19c74bbbb4523f79afde0756f8174166";

        request.getReviewList(reviewRequestUrl).enqueue(new Callback<ReviewData>() {
            @Override
            public void onResponse(Call<ReviewData> call, Response<ReviewData> response) {

                if (response.isSuccessful()) {

                    ReviewData data = response.body();
                    reviewItemList = data.getReviewItem();

                    ReviewAdapter reviewAdapter = new ReviewAdapter(getContext(), reviewItemList);
                    recyclerView.setAdapter(reviewAdapter);
                    progressBar.setVisibility(View.INVISIBLE);
                    Log.d("Detail Activity", "loaded from API");
                } else {
                    progressBar.setVisibility(View.INVISIBLE);
                    errorMessage.setText("There are no trailers available here");

                }
            }

            @Override
            public void onFailure(Call<ReviewData> call, Throwable t) {
                progressBar.setVisibility(View.INVISIBLE);
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

                getReviews();

                break;



        }


    }


}

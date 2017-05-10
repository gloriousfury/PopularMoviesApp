package com.example.android.popularmoviesapp.ui.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android.popularmoviesapp.R;
import com.example.android.popularmoviesapp.model.SingleMovie;
import com.example.android.popularmoviesapp.ui.activity.DetailActivity;


/**
 * Created by OLORIAKE KEHINDE on 11/16/2016.
 */

public class OverviewFragment extends Fragment implements View.OnClickListener {

    TextView overview;

    public static OverviewFragment newInstance() {
        OverviewFragment fragment = new OverviewFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_overview, container, false);

        overview = (TextView) v.findViewById(R.id.tv_movie_overview);

        getMovieData();

        return v;
    }

    public void getMovieData() {
        Intent getData = getActivity().getIntent();
        SingleMovie movie = getData.getParcelableExtra("movieItem");


        if (movie != null) {
            String get_overview = movie.getOverview();
            overview.setText(get_overview);


        }
    }

    @Override
    public void onClick(View v) {


    }


}

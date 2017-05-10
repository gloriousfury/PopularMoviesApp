package com.example.android.popularmoviesapp.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.customtabs.CustomTabsIntent;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.android.popularmoviesapp.R;
import com.example.android.popularmoviesapp.model.TrailerItem;
import com.example.android.popularmoviesapp.ui.activity.DetailActivity;
import com.squareup.picasso.Picasso;


import java.util.List;


public class TrailerAdapter extends RecyclerView.Adapter<TrailerAdapter.ViewHolder> {
    private List<TrailerItem> itemlist;
    Context context;


    private static final String TAG = "CustomAdapter";
    private RecyclerView.OnItemTouchListener onItemTouchListener;


    public TrailerAdapter(Context context, List<TrailerItem> itemlist) {
        this.context = context;
        this.itemlist = itemlist;

    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        TextView trailer_title;
        View menu_color;
        ImageView trailerThumbnail;

        public ViewHolder(View view) {
            super(view);
            trailer_title = (TextView) view.findViewById(R.id.trailer_title);
            trailerThumbnail = (ImageView) view.findViewById(R.id.trailer_thumbnail);
            view.setClickable(true);
            view.setOnClickListener(this);


        }


        @Override
        public void onClick(View view) {

            buildandLaunchUri(itemlist.get(getAdapterPosition()).getKey());


    }

    private void buildandLaunchUri(String query) {
        Uri builtUri = Uri.parse("https://www.youtube.com").buildUpon()
                .appendPath("watch")
                .appendQueryParameter("v", query)
                .build();

//            Uri uri = Uri.parse("https://segunfamisa.com").withAppendedPath("w");

// create an intent builder
        CustomTabsIntent.Builder intentBuilder = new CustomTabsIntent.Builder();

// Begin customizing
// set toolbar colors
        intentBuilder.setToolbarColor(ContextCompat.getColor(context, R.color.colorPrimary));
        intentBuilder.setSecondaryToolbarColor(ContextCompat.getColor(context, R.color.colorPrimaryDark));

// set start and exit animations
//            intentBuilder.setStartAnimations(context, R.anim.slide_in_right, R.anim.slide_out_left);
        intentBuilder.setExitAnimations(context, android.R.anim.slide_in_left,
                android.R.anim.slide_out_right);

// build custom tabs intent
        CustomTabsIntent customTabsIntent = intentBuilder.build();

// launch the url
        customTabsIntent.launchUrl( ((Activity) context), builtUri);
    }



    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.trailers_list_item, viewGroup, false);
        return new ViewHolder(view);


    }


    boolean isOdd(int val)

    {
        return (val & 0x01) != 0;
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, final int position) {
        String thumbnail_url = "https://img.youtube.com/vi/" + (itemlist.get(position).getKey())+ "/mqdefault.jpg";

        Picasso.with(context).load(thumbnail_url).into(viewHolder.trailerThumbnail);
        viewHolder.trailer_title.setText(itemlist.get(position).getName());

    }


    @Override
    public int getItemCount() {
        if(itemlist!= null) {
            return itemlist.size();
        }else return 0;
    }


}
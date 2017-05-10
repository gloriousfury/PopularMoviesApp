package com.example.android.popularmoviesapp.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.android.popularmoviesapp.R;
import com.example.android.popularmoviesapp.model.ReviewItem;


import java.util.List;


public class ReviewAdapter extends RecyclerView.Adapter<ReviewAdapter.ViewHolder> {
    private List<ReviewItem> itemlist;
    Context context;




    public ReviewAdapter(Context context, List<ReviewItem> itemlist) {
        this.context = context;
        this.itemlist = itemlist;

    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView review_author, review_content;


        public ViewHolder(View view) {
            super(view);
            review_author = (TextView) view.findViewById(R.id.review_author);
            review_content = (TextView) view.findViewById(R.id.review_content);


        }




    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.review_list_item, viewGroup, false);
        return new ViewHolder(view);


    }


    boolean isOdd(int val)

    {
        return (val & 0x01) != 0;
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, final int position) {

        viewHolder.review_author.setText(itemlist.get(position).getAuthor());
        viewHolder.review_content.setText(itemlist.get(position).getContent());

    }


    @Override
    public int getItemCount() {
        if(itemlist!= null) {
            return itemlist.size();
        }else return 0;
    }


}
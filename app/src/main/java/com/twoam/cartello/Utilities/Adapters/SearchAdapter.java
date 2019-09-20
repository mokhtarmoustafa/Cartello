package com.twoam.cartello.Utilities.Adapters;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.twoam.cartello.Model.Search;
import com.twoam.cartello.R;
import com.twoam.cartello.Utilities.General.AppConstants;
import com.twoam.cartello.View.SearchResultActivity;

import java.util.ArrayList;

/**
 * Created by Mokhtar on 6/30/2019.
 */

public class SearchAdapter
        extends RecyclerView.Adapter<SearchAdapter.MyViewHolder> {

    private LayoutInflater inflater;
    private ArrayList<Search> searchList;
    private Context context;

    public SearchAdapter(Context ctx, ArrayList<Search> searchList) {

        inflater = LayoutInflater.from(ctx);
        context = ctx;
        this.searchList = searchList;
    }

    @Override
    public SearchAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = inflater.inflate(R.layout.search_layout, parent, false);
        MyViewHolder holder = new MyViewHolder(view);

        return holder;
    }

    @Override
    public void onBindViewHolder(SearchAdapter.MyViewHolder holder, int position) {
//use glide
        holder.itemName.setText(searchList.get(position).getName());

        holder.itemName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
// open search result activity
                Intent intent = new Intent(context, SearchResultActivity.class).putExtra(AppConstants.SEARCH_DATA,searchList.get(position).getName());
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return searchList.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {

        TextView itemName;

        public MyViewHolder(View itemView) {
            super(itemView);

            itemName = itemView.findViewById(R.id.tvItemName);

        }

    }
}

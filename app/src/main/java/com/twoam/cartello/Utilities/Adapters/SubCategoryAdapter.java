package com.twoam.cartello.Utilities.Adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.twoam.cartello.Model.SubCategory;
import com.twoam.cartello.R;

import java.util.ArrayList;

/**
 * Created by Mokhtar on 6/30/2019.
 */

public class SubCategoryAdapter
        extends RecyclerView.Adapter<SubCategoryAdapter.MyViewHolder> {

    private LayoutInflater inflater;
    private ArrayList<SubCategory> imageModelArrayList;
    private Context context;

    public SubCategoryAdapter(Context ctx, ArrayList<SubCategory> imageModelArrayList) {

        inflater = LayoutInflater.from(ctx);
        context=ctx;
        this.imageModelArrayList = imageModelArrayList;
    }

    @Override
    public SubCategoryAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = inflater.inflate(R.layout.recycler_sub_category, parent, false);
        MyViewHolder holder = new MyViewHolder(view);

        return holder;
    }

    @Override
    public void onBindViewHolder(SubCategoryAdapter.MyViewHolder holder, int position) {
//use glide
        Glide.with(context).load(imageModelArrayList.get(position).getImage())
                .apply(RequestOptions.placeholderOf(R.drawable.item))
                .thumbnail( 0.1f )
                .into(holder.itemImage);
        holder.itemName.setText(imageModelArrayList.get(position).getName());
    }

    @Override
    public int getItemCount() {
        return imageModelArrayList.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {

        TextView itemName;
        ImageView itemImage;

        public MyViewHolder(View itemView) {
            super(itemView);

            itemName =  itemView.findViewById(R.id.tvItemName);
            itemImage = itemView.findViewById(R.id.ivItemImage);

        }

    }
}

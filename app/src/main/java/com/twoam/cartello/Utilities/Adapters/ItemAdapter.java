package com.twoam.cartello.Utilities.Adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.twoam.cartello.Model.Item;
import com.twoam.cartello.R;

import java.util.ArrayList;

/**
 * Created by Mokhtar on 6/30/2019.
 */

public class ItemAdapter
        extends RecyclerView.Adapter<ItemAdapter.MyViewHolder> {

    private LayoutInflater inflater;
    private ArrayList<Item> imageModelArrayList;

    public ItemAdapter(Context ctx, ArrayList<Item> imageModelArrayList) {

        inflater = LayoutInflater.from(ctx);
        this.imageModelArrayList = imageModelArrayList;
    }

    @Override
    public ItemAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = inflater.inflate(R.layout.recycler_item, parent, false);
        MyViewHolder holder = new MyViewHolder(view);

        return holder;
    }

    @Override
    public void onBindViewHolder(ItemAdapter.MyViewHolder holder, int position) {

        holder.itemImage.setImageResource(imageModelArrayList.get(position).getItemImage());
        holder.itemName.setText(imageModelArrayList.get(position).getName());
        holder.itemOldPrice.setText(imageModelArrayList.get(position).getOldPrice());
        holder.itemNewPrice.setText(imageModelArrayList.get(position).getNewPrice());

    }

    @Override
    public int getItemCount() {
        return imageModelArrayList.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {

        TextView itemName;
        ImageView itemImage;
        TextView itemOldPrice;
        TextView itemNewPrice;

        public MyViewHolder(View itemView) {
            super(itemView);

            itemName =  itemView.findViewById(R.id.tvItemName);
            itemImage = itemView.findViewById(R.id.ivItemImage);
            itemOldPrice =  itemView.findViewById(R.id.tvOldPrice);
            itemNewPrice =  itemView.findViewById(R.id.tvNewPrice);

        }

    }
}

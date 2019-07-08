package com.twoam.cartello.Utilities.Adapters;

import android.app.FragmentManager;
import android.content.Context;
import android.graphics.Paint;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.twoam.cartello.Model.Product;
import com.twoam.cartello.R;
import com.twoam.cartello.Utilities.General.AppController;

import java.util.ArrayList;

/**
 * Created by Mokhtar on 6/30/2019.
 */

public class ProductAdapter
        extends RecyclerView.Adapter<ProductAdapter.MyViewHolder> {

    private LayoutInflater inflater;
    private ArrayList<Product> imageModelArrayList;
    private Product product;


    public ProductAdapter(Context ctx, ArrayList<Product> imageModelArrayList) {

        inflater = LayoutInflater.from(ctx);
        this.imageModelArrayList = imageModelArrayList;
    }

    @Override
    public ProductAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = inflater.inflate(R.layout.product_layout, parent, false);

        MyViewHolder holder = new MyViewHolder(view);

        return holder;
    }

    @Override
    public void onBindViewHolder(ProductAdapter.MyViewHolder holder, int position) {

        holder.itemImage.setImageResource(R.drawable.ic_cart);
        holder.itemName.setText(imageModelArrayList.get(position).getName());
        holder.itemOldPrice.setText(imageModelArrayList.get(position).getOldPrice() + " " + AppController.getContext().getString(R.string.currency));
        holder.itemOldPrice.setPaintFlags(holder.itemOldPrice.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
        holder.itemNewPrice.setText(imageModelArrayList.get(position).getNewPrice() + " " + AppController.getContext().getString(R.string.currency));

    }

    @Override
    public int getItemCount() {
        return imageModelArrayList.size();
    }

    @Override
    public long getItemId(int position) {
        return super.getItemId(position);
    }


    class MyViewHolder extends RecyclerView.ViewHolder {

        TextView itemName;
        ImageView itemImage;
        TextView itemOldPrice;
        TextView itemNewPrice;

        public MyViewHolder(View itemView) {
            super(itemView);

            itemName = itemView.findViewById(R.id.tvItemName);
            itemImage = itemView.findViewById(R.id.ivItemImage);
            itemOldPrice = itemView.findViewById(R.id.tvOldPrice);
            itemNewPrice = itemView.findViewById(R.id.tvNewPrice);


            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    // get position
                    int pos = getAdapterPosition();

                    // check if item still exists
                    if (pos != RecyclerView.NO_POSITION) {
                        product= imageModelArrayList.get(pos);
                        Toast.makeText(v.getContext(), "You clicked " + product.getName(), Toast.LENGTH_SHORT).show();

                    }
                }
            });
        }

    }
}

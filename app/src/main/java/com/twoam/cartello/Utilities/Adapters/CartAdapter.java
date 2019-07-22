package com.twoam.cartello.Utilities.Adapters;

import android.content.Context;
import android.graphics.Paint;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.twoam.cartello.Model.Product;
import com.twoam.cartello.R;
import com.twoam.cartello.Utilities.General.AppController;

import java.util.ArrayList;

/**
 * Created by Mokhtar on 6/30/2019.
 */

public class CartAdapter
        extends RecyclerView.Adapter<CartAdapter.MyViewHolder> {

    private LayoutInflater inflater;
    private ArrayList<Product> productsList;
    private Product product;
    private Context context;


    public CartAdapter(Context ctx, ArrayList<Product> productsList) {
        context = ctx;
        inflater = LayoutInflater.from(ctx);
        this.productsList = productsList;
    }

    @Override
    public CartAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = inflater.inflate(R.layout.cart_product_layout, parent, false);

        MyViewHolder holder = new MyViewHolder(view);

        return holder;
    }

    @Override
    public void onBindViewHolder(CartAdapter.MyViewHolder holder, int position) {

        Glide.with(context).load(productsList.get(position).getImage())
                .apply(RequestOptions.placeholderOf(R.drawable.item))
                .thumbnail(0.1f)
                .into(holder.productImage);

        holder.productName.setText(productsList.get(position).getName());

        holder.tvPrice.setText(productsList.get(position).getPrice() + " " + AppController.getContext().getString(R.string.currency));
        holder.tvValue.setText(productsList.get(position).getQuantity());

    }

    @Override
    public int getItemCount() {
        return productsList.size();
    }

    @Override
    public long getItemId(int position) {
        return super.getItemId(position);
    }


    class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        ImageView productImage;
        TextView productName;
        TextView tvPrice;
        ImageView ivDelete;
        ImageView addItem;
        TextView subItem;
        TextView tvValue;


        public MyViewHolder(View itemView) {
            super(itemView);

            productImage = itemView.findViewById(R.id.ivProductImage);
            productName = itemView.findViewById(R.id.tvProductName);
            tvPrice = itemView.findViewById(R.id.tvPrice);
            ivDelete = itemView.findViewById(R.id.ivDelete);
            subItem = itemView.findViewById(R.id.subItem);
            tvValue = itemView.findViewById(R.id.tvValue);

            addItem = itemView.findViewById(R.id.addItem);


            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    // get position
                    int pos = getAdapterPosition();

                    // check if item still exists
                    if (pos != RecyclerView.NO_POSITION) {
                        CartAdapter.this.product = productsList.get(pos);
                        Toast.makeText(v.getContext(), "You clicked " + CartAdapter.this.product.getName(), Toast.LENGTH_SHORT).show();

                    }
                }
            });
        }

        @Override
        public void onClick(View v) {

            // get position
            int pos = getAdapterPosition();
            // check if item still exists
            if (pos != RecyclerView.NO_POSITION) {
                product = productsList.get(pos);
                Toast.makeText(v.getContext(), "You clicked " + product.getName(), Toast.LENGTH_SHORT).show();

            }

            switch (v.getId()) {
                case R.id.ivProductImage:
                    //todo open product details Fragment
                    break;
                case R.id.ivDelete:
                    productsList.remove(product);
                    break;
                case R.id.subItem:
                    if(product.getQuantity()==0)
                    {
                        productsList.remove(product);
                    }
                    if (product.getQuantity() > 0) {
                        Integer quantity=Integer.parseInt(String.valueOf(product.getQuantity()))-1;
                        product.setQuantity(quantity);
                        tvValue.setText( quantity);
                    }

                    break;
                case R.id.addItem:
                    Integer quantity=Integer.parseInt(String.valueOf(product.getQuantity()))+1;
                    product.setQuantity(quantity);
                    tvValue.setText( quantity);
                    break;


            }
        }

    }
}

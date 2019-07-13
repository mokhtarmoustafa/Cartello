package com.twoam.cartello.Utilities.Adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.twoam.cartello.Model.Order;
import com.twoam.cartello.R;
import com.twoam.cartello.Utilities.General.AppController;

import java.util.ArrayList;

/**
 * Created by Mokhtar on 6/30/2019.
 */

public class OrderAdapter
        extends RecyclerView.Adapter<OrderAdapter.MyViewHolder> {

    private LayoutInflater inflater;
    private ArrayList<Order> orderList;
    private Order order;


    public OrderAdapter(Context ctx, ArrayList<Order> orderList) {

        inflater = LayoutInflater.from(ctx);
        this.orderList = orderList;
    }

    @Override
    public OrderAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = inflater.inflate(R.layout.order_layout, parent, false);

        MyViewHolder holder = new MyViewHolder(view);

        return holder;
    }

    @Override
    public void onBindViewHolder(OrderAdapter.MyViewHolder holder, int position) {


        holder.orderID.setText(orderList.get(position).getId());
        holder.orderDate.setText(orderList.get(position).getCreated_at());
        holder.totalAmount.setText(orderList.get(position).getPaid_amount() + " " + AppController.getContext().getString(R.string.currency));
        holder.paymentType.setText(orderList.get(position).getPayment_method());

    }

    @Override
    public int getItemCount() {
        return orderList.size();
    }

    @Override
    public long getItemId(int position) {
        return super.getItemId(position);
    }


    class MyViewHolder extends RecyclerView.ViewHolder {

        TextView orderID;
        TextView orderDate;
        TextView totalAmount;
        TextView paymentType;

        public MyViewHolder(View itemView) {
            super(itemView);

            orderID = itemView.findViewById(R.id.tvOrderIDValue);
            orderDate = itemView.findViewById(R.id.tvPaymentTypeValue);
            totalAmount = itemView.findViewById(R.id.tvTotalAmountValue);
            paymentType = itemView.findViewById(R.id.tvPaymentTypeValue);


            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    // get position
                    int pos = getAdapterPosition();

                    // check if item still exists
                    if (pos != RecyclerView.NO_POSITION) {
                        order = orderList.get(pos);
                        Toast.makeText(v.getContext(), "You clicked " + order.getNotes(), Toast.LENGTH_SHORT).show();

                    }
                }
            });
        }

    }
}

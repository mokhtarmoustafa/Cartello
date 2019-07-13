package com.twoam.cartello.Utilities.Adapters;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;
import com.twoam.cartello.Model.Address;
import com.twoam.cartello.R;
import com.twoam.cartello.View.EditAddressProfileActivity;

import java.util.ArrayList;

/**
 * Created by Mokhtar on 6/30/2019.
 */

public class AddressAdapter
        extends RecyclerView.Adapter<AddressAdapter.MyViewHolder> {

    private LayoutInflater inflater;
    private ArrayList<Address> addressList;
    private Address address;
    private Context context;


    public AddressAdapter(Context ctx, ArrayList<Address> addressList) {

        inflater = LayoutInflater.from(ctx);
        context = ctx;
        this.addressList = addressList;
    }

    @Override
    public AddressAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = inflater.inflate(R.layout.address_layout, parent, false);

        MyViewHolder holder = new MyViewHolder(view);

        return holder;
    }

    @Override
    public void onBindViewHolder(AddressAdapter.MyViewHolder holder, int position) {


        holder.tvAddress.setText(addressList.get(position).getAddress());
        holder.tvAddressType.setText(addressList.get(position).getAddressType());
    }

    @Override
    public int getItemCount() {
        return addressList.size();
    }

    @Override
    public long getItemId(int position) {
        return super.getItemId(position);
    }


    class MyViewHolder extends RecyclerView.ViewHolder {

        TextView tvAddress;
        TextView tvAddressType;
        TextView tvEdit;

        public MyViewHolder(View itemView) {
            super(itemView);
            tvAddress = itemView.findViewById(R.id.tvAddress);
            tvAddressType = itemView.findViewById(R.id.tvAddressType);
            tvEdit = itemView.findViewById(R.id.tvEdit);


            itemView.setOnClickListener(v -> {

                // get position
                int pos = getAdapterPosition();

                // check if item still exists
                if (pos != RecyclerView.NO_POSITION) {
                    address = addressList.get(pos);
                    Toast.makeText(v.getContext(), "You clicked " + address.getAddress(), Toast.LENGTH_SHORT).show();
                    // open edit address activity
                    context.startActivity(new Intent(context, EditAddressProfileActivity.class)
                            .setFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                            .putExtra("addressId", address.getId()));
                }
            });
        }

    }
}

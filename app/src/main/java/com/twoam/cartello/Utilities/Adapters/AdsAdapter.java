package com.twoam.cartello.Utilities.Adapters;

import android.content.Context;
import android.os.Parcelable;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.twoam.cartello.Model.Ads;
import com.twoam.cartello.R;

import java.util.ArrayList;

/**
 * Created by Mokhtar on 6/30/2019.
 */

public class AdsAdapter extends PagerAdapter {

    private ArrayList<Ads> adsList;
    private LayoutInflater inflater;
    private Context context;


    public AdsAdapter(Context context, ArrayList<Ads> adsList) {
        this.context = context;
        this.adsList = adsList;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }

    @Override
    public int getCount() {
        return adsList.size();
    }

    @Override
    public Object instantiateItem(ViewGroup view, int position) {
        View imageLayout = inflater.inflate(R.layout.ads_layout, view, false);
        final ImageView imageView = imageLayout
                .findViewById(R.id.image);



        Glide.with(context).load(adsList.get(position).getImage())
                .apply(RequestOptions.placeholderOf(R.drawable.item))
                .into(imageView);

        view.addView(imageLayout, 0);

        return imageLayout;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view.equals(object);
    }

    @Override
    public Parcelable saveState() {
        return null;
    }
}

package com.twoam.cartello.Utilities.Adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

/**
 * Created by Mokhtar on 6/30/2019.
 */

public class CategoryAdapter extends FragmentStatePagerAdapter {
    int mNumOfTabs;
    public CategoryAdapter(FragmentManager fm, int NumOfTabs) {
        super(fm);
        this.mNumOfTabs = NumOfTabs;
    }

    @Override
    public Fragment getItem(int position) {
        return DynamicFragment.newInstance(position);

    }

    @Override
    public int getCount() {
        return mNumOfTabs;
    }
}
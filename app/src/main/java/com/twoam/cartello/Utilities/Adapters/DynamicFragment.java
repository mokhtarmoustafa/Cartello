package com.twoam.cartello.Utilities.Adapters;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.twoam.cartello.R;

/**
 * Created by Mokhtar on 6/30/2019.
 */

public class DynamicFragment extends Fragment {
    View view;

    public static DynamicFragment newInstance(int val) {
        DynamicFragment fragment = new DynamicFragment();
        Bundle args = new Bundle();
        args.putInt("someInt", val);
        fragment.setArguments(args);
        return fragment;
    }

    int val;
    TextView c;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
//        view = inflater.inflate(R.layout.fragment_list, container, false);
//        val = getArguments().getInt("someInt", 0);
//        c = view.findViewById(R.id.tvData);
//        c.setText("" + val);
        return view;
    }
}

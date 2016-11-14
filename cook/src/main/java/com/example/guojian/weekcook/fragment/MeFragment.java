package com.example.guojian.weekcook.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.guojian.weekcook.R;


/**
 * A simple {@link Fragment} subclass.
 */
public class MeFragment extends Fragment {
    private String TAG = "guojianMe_CookDemo";

    public MeFragment() {
        // Required empty public constructor
    }


    private TextView mTextViewMe;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_me,container,false);
        //mTextViewMe = (TextView) view.findViewById(R.id.tv_me);
        return view;
    }


}

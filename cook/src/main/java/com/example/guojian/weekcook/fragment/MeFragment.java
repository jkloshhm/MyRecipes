package com.example.guojian.weekcook.fragment;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.guojian.weekcook.R;
import com.example.guojian.weekcook.activity.CollectionActivity;
import com.example.guojian.weekcook.dao.MyDBServiceUtils;


/**
 * A simple {@link Fragment} subclass.
 */
public class MeFragment extends Fragment {
    private String TAG = "guojianMe_CookDemo";
    private MyDBServiceUtils mService;
    private Context mContext;
    private TextView mTextViewMe;
    private LinearLayout mMycollectionLinearLayout;
    public MeFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mContext = getContext();
        View view = inflater.inflate(R.layout.fragment_me, container, false);
        mMycollectionLinearLayout = (LinearLayout) view.findViewById(R.id.ll_my_collection);
        //mTextViewMe = (TextView) view.findViewById(R.id.tv_me);
        mMycollectionLinearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, CollectionActivity.class);
                mContext.startActivity(intent);

            }
        });

        return view;
    }


}

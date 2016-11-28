package com.example.guojian.weekcook.fragment;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.guojian.weekcook.R;
import com.example.guojian.weekcook.activity.CollectionActivity;
import com.example.guojian.weekcook.activity.MyInformationActivity;
import com.example.guojian.weekcook.activity.MySettingsActivity;


/**
 * A simple {@link Fragment} subclass.
 */
public class MeFragment extends Fragment {
    private String TAG = "guojianMe_CookDemo";
    private Context mContext;

    public MeFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mContext = getContext();
        View view = inflater.inflate(R.layout.fragment_me, container, false);
        LinearLayout mMyCollectionLinearLayout = (LinearLayout) view.findViewById(R.id.ll_my_collection_me);
        LinearLayout mMyInformationLinearLayout = (LinearLayout) view.findViewById(R.id.ll_my_information_me);
        LinearLayout mMySettingsLinearLayout = (LinearLayout) view.findViewById(R.id.ll_my_settings_me);
        LinearLayout mMyShareAppLinearLayout = (LinearLayout) view.findViewById(R.id.ll_share_app);
        mMyCollectionLinearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent mIntent = new Intent(mContext, CollectionActivity.class);
                mContext.startActivity(mIntent);
            }
        });
        mMyInformationLinearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent mIntent = new Intent(mContext, MyInformationActivity.class);
                mContext.startActivity(mIntent);
            }
        });
        mMySettingsLinearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent mIntent = new Intent(mContext, MySettingsActivity.class);
                mContext.startActivity(mIntent);
            }
        });
        mMyShareAppLinearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(mContext, "暂无分享~", Toast.LENGTH_SHORT).show();
            }
        });

        return view;
    }

}

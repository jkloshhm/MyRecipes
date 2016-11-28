package com.example.guojian.weekcook.fragment;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.guojian.weekcook.R;
import com.example.guojian.weekcook.activity.CollectionActivity;
import com.example.guojian.weekcook.activity.MyInformationActivity;
import com.example.guojian.weekcook.activity.MySettingsActivity;


/**
 * A simple {@link Fragment} subclass.
 */
public class MeFragment extends Fragment implements View.OnClickListener {
    private String TAG = "guojianMe_CookDemo";
    private Context mContext;
    private SharedPreferences mSharedPreferences;
    public MeFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mContext = getContext();
        View view = inflater.inflate(R.layout.fragment_me, container, false);
        ImageView mMyTitleImg = (ImageView) view.findViewById(R.id.iv_my_title_img);
        TextView mMyname = (TextView) view.findViewById(R.id.tv_me_name);
        LinearLayout mMyCollectionLinearLayout = (LinearLayout) view.findViewById(R.id.ll_my_collection_me);
        LinearLayout mMyInformationLinearLayout = (LinearLayout) view.findViewById(R.id.ll_my_information_me);
        LinearLayout mMySettingsLinearLayout = (LinearLayout) view.findViewById(R.id.ll_my_settings_me);
        LinearLayout mMyShareAppLinearLayout = (LinearLayout) view.findViewById(R.id.ll_share_app);
        //getSharedPreferences("cooking", MODE_PRIVATE);
        mSharedPreferences = mContext.getSharedPreferences("cooking",Context.MODE_PRIVATE);
        mMyname.setText(mSharedPreferences.getString("name","Mary"));
        mMyCollectionLinearLayout.setOnClickListener(this);
        mMyInformationLinearLayout.setOnClickListener(this);
        mMySettingsLinearLayout.setOnClickListener(this);
        mMyShareAppLinearLayout.setOnClickListener(this);

        return view;
    }

    @Override
    public void onClick(View v) {
        Intent mIntent;
        switch (v.getId()) {
            case R.id.ll_my_collection_me:
                mIntent = new Intent(mContext, CollectionActivity.class);
                mContext.startActivity(mIntent);
                break;
            case R.id.ll_my_information_me:
                mIntent = new Intent(mContext, MyInformationActivity.class);
                mContext.startActivity(mIntent);
                break;
            case R.id.ll_my_settings_me:
                mIntent = new Intent(mContext, MySettingsActivity.class);
                mContext.startActivity(mIntent);
                break;
            case R.id.ll_share_app:
                Toast.makeText(mContext, "暂无分享~", Toast.LENGTH_SHORT).show();
                break;
            default:
                break;
        }
    }
}

package com.example.guojian.weekcook.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.guojian.weekcook.R;
import com.example.guojian.weekcook.adapter.MaterialAdapter;
import com.example.guojian.weekcook.adapter.ProcessAdapter;
import com.example.guojian.weekcook.bean.CookBean;
import com.example.guojian.weekcook.bean.MaterialBean;
import com.example.guojian.weekcook.bean.ProcessBean;
import com.example.guojian.weekcook.dao.DBServices;
import com.example.guojian.weekcook.dao.MyDBServiceUtils;
import com.example.guojian.weekcook.utils.ImageLoaderUtil;

import java.util.ArrayList;
import java.util.List;

public class DetailsActivity extends Activity  {
    private static DBServices db;
    private static ArrayList<CookBean> cookBeanlist;
    private static ArrayList<String> cookIdList = new ArrayList<>();
    private static ArrayList<String> cookTagArrayList = new ArrayList<>();
    private String realIp;
    private CookBean cookBean;
    private ImageView mCollectImg, mDetailsImage;
    private CookBean cookBeanSQL;
    private MyDBServiceUtils mService;
    private List<MaterialBean> materialBeanlist;
    private List<ProcessBean> processBeenlist;
    private ListView mListViewMaterial, mListViewProcess;
    private MaterialAdapter mMaterialAdapter;
    private ProcessAdapter mProcessAdapter;
    private LinearLayout mlinearLayout, mCollectLinearLayout, mButtonBack;
    private boolean isRed;
    private TextView mName, mContent, mPeopleNum, mCookingTime, mTag;
    private String TAG = "jkloshhm-----------DetailsActivity------";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.i(TAG, "DetailsActivity____________onCreate()");
        setContentView(R.layout.activity_details);
        initViews();

    }

    @Override
    protected void onResume() {
        Log.i(TAG, "DetailsActivity ____________onResume()");
        super.onResume();
        initDB();
        Intent intent = this.getIntent();
        cookBean = (CookBean) intent.getSerializableExtra("cookBean01");
        setUpViews();
        realIp = cookBean.getReal_ip();
        if (realIp.equals("mary")) {
            mCollectImg.setImageDrawable(getResources().getDrawable(R.mipmap.collection_gray));
            isRed = false;
        } else {
            mCollectImg.setImageDrawable(getResources().getDrawable(R.mipmap.collection_red));
            isRed = true;
        }
        mButtonBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        mCollectLinearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isRed) {//删除
                    mCollectImg.setImageDrawable(getResources().getDrawable(R.mipmap.collection_gray));
                    isRed = false;
                    Toast.makeText(getApplicationContext(), "已取消收藏~", Toast.LENGTH_SHORT).show();
                } else {
                    mCollectImg.setImageDrawable(getResources().getDrawable(R.mipmap.collection_red));
                    isRed = true;
                    Toast.makeText(getApplicationContext(), "收藏成功~", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    protected void onPause() {
        Log.i(TAG, "DetailsActivity ____________onPause()");
        super.onPause();
        if (realIp.equals("mary")) {
            if (isRed) {//保存
                MyDBServiceUtils.saveData(cookBean, db);
                Toast.makeText(getApplicationContext(), "收藏成功 realIp.equals(\"mary\") =true ~~~~~~onPause", Toast.LENGTH_SHORT).show();
            }
        } else {
            if (!isRed) {//删除
                MyDBServiceUtils.delectData(cookBean, db);
                cookBean.setReal_ip("mary");
                Toast.makeText(getApplicationContext(), "已取消收藏~~~realIp.equals(\"mary\") =false~~~~onPause", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void initDB() {
        db = MyDBServiceUtils.getInstance(this);
        cookBeanlist = MyDBServiceUtils.getAllObject(db);
        for (int i = 0; i < cookBeanlist.size(); i++) {
            String real_ip = cookBeanlist.get(i).getReal_ip();
            this.cookIdList.add(real_ip);
        }
    }

    private void setUpViews() {
        mName.setText(cookBean.getName_cook());
        String img_url = cookBean.getPic();
        ImageLoaderUtil.setPicBitmap2(mDetailsImage, img_url);
        mContent.setText(cookBean.getContent());
        mPeopleNum.setText("用餐人数: " + cookBean.getPeoplenum());
        mCookingTime.setText("烹饪时间: " + cookBean.getCookingtime());
        mTag.setText("标签: " + cookBean.getTag_cook());
        materialBeanlist = cookBean.getMaterialBeen();
        mMaterialAdapter = new MaterialAdapter(this, materialBeanlist);
        mListViewMaterial.setAdapter(mMaterialAdapter);
        setListViewHeightBasedOnChildren1(mListViewMaterial);
        processBeenlist = cookBean.getProcessBeen();
        mProcessAdapter = new ProcessAdapter(this, processBeenlist);
        mListViewProcess.setAdapter(mProcessAdapter);
        setListViewHeightBasedOnChildren1(mListViewProcess);

    }

    private void initViews() {
        mButtonBack = (LinearLayout) findViewById(R.id.ll_details_back_to_list);
        mCollectLinearLayout = (LinearLayout) findViewById(R.id.ll_collect_the_cook_data);
        mCollectImg = (ImageView) findViewById(R.id.iv_collection_img);
        mDetailsImage = (ImageView) findViewById(R.id.iv_details_img);
        mName = (TextView) findViewById(R.id.tv_details_cook_name);
        mContent = (TextView) findViewById(R.id.tv_details_cook_content);
        mPeopleNum = (TextView) findViewById(R.id.tv_details_cook_peoplenum);
        mCookingTime = (TextView) findViewById(R.id.tv_details_cook_cookingtime);
        mTag = (TextView) findViewById(R.id.tv_details_cook_tag);
        mListViewMaterial = (ListView) findViewById(R.id.lv_listview_material);
        mListViewProcess = (ListView) findViewById(R.id.lv_listview_process);
        mlinearLayout = (LinearLayout) findViewById(R.id.linear1);
    }

    public void setListViewHeightBasedOnChildren1(ListView listView) {
        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null) {
            return;
        }
        int totalHeight = 0;
        for (int i = 0; i < listAdapter.getCount(); i++) {
            View listItem = listAdapter.getView(i, null, listView);
            listItem.measure(0, 0);
            totalHeight += listItem.getMeasuredHeight();
        }
        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight
                + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
        //params.height += 16;// if without this statement,the listview will be a little short
        listView.setLayoutParams(params);
    }

    @Override
    protected void onRestart() {
        Log.i(TAG, "DetailsActivity ____________onRestart()");
        super.onRestart();
    }

    @Override
    protected void onStart() {
        Log.i(TAG, "DetailsActivity ____________onStart()");
        super.onStart();
    }

    @Override
    protected void onStop() {
        Log.i(TAG, "DetailsActivity ____________onStop()");
        super.onStop();
    }
}

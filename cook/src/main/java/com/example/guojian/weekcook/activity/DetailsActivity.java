package com.example.guojian.weekcook.activity;

import android.app.Activity;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
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
import com.example.guojian.weekcook.dao.CookBeanService;
import com.example.guojian.weekcook.dao.DBServices;
import com.example.guojian.weekcook.utils.ImageLoaderUtil;

import java.io.ByteArrayOutputStream;
import java.io.ObjectOutputStream;
import java.util.List;

public class DetailsActivity extends Activity {
    private CookBeanService mService;
    private DBServices db;
    //private CookBean cookBean;
    private List<MaterialBean> materialBeanlist;
    private List<ProcessBean> processBeenlist;
    private ListView mListViewMaterial, mListViewProcess;
    private MaterialAdapter mMaterialAdapter;
    private ProcessAdapter mProcessAdapter;
    private LinearLayout mlinearLayout, mCollectLinearLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);
        Intent intent = this.getIntent();
        final CookBean cookBean = (CookBean) intent.getSerializableExtra("cookBean01");
        LinearLayout mButtonBack = (LinearLayout) findViewById(R.id.ll_details_back_to_list);
        LinearLayout mCollectLinearLayout = (LinearLayout) findViewById(R.id.ll_collect_the_cook_data);
        TextView mTitleName = (TextView) findViewById(R.id.tv_details_title_name);
        ImageView mDetailsImage = (ImageView) findViewById(R.id.iv_details_img);
        TextView mName = (TextView) findViewById(R.id.tv_details_cook_name);
        TextView mContent = (TextView) findViewById(R.id.tv_details_cook_content);
        TextView mPeopleNum = (TextView) findViewById(R.id.tv_details_cook_peoplenum);
        TextView mPrepareTime = (TextView) findViewById(R.id.tv_details_cook_preparetime);
        TextView mCookingTime = (TextView) findViewById(R.id.tv_details_cook_cookingtime);
        TextView mTag = (TextView) findViewById(R.id.tv_details_cook_tag);
        mListViewMaterial = (ListView) findViewById(R.id.lv_listview_material);
        mListViewProcess = (ListView) findViewById(R.id.lv_listview_process);
        mlinearLayout = (LinearLayout) findViewById(R.id.linear1);


        mTitleName.setText(cookBean.getName_cook());
        mName.setText(cookBean.getName_cook());
        String img_url = cookBean.getPic();
        ImageLoaderUtil.setPicBitmap2(mDetailsImage, img_url);
        mContent.setText(cookBean.getContent());
        mPeopleNum.setText("用餐人数：" + cookBean.getPeoplenum());
        mPrepareTime.setText("准备时间：" + cookBean.getPreparetime());
        mCookingTime.setText("做饭时间：" + cookBean.getCookingtime());
        mTag.setText("标签：" + cookBean.getTag());

        //mListViewMaterial.addHeaderView(header);
        materialBeanlist = cookBean.getMaterialBeen();
        mMaterialAdapter = new MaterialAdapter(this, materialBeanlist);
        mListViewMaterial.setAdapter(mMaterialAdapter);
        setListViewHeightBasedOnChildren1(mListViewMaterial);
        //setListViewHeightBasedOnChildren(linearLayout,mListViewMaterial);
        processBeenlist = cookBean.getProcessBeen();
        mProcessAdapter = new ProcessAdapter(this, processBeenlist);
        mListViewProcess.setAdapter(mProcessAdapter);
        setListViewHeightBasedOnChildren1(mListViewProcess);
        //setListViewHeightBasedOnChildren(linearLayout,mListViewProcess);
        mButtonBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        //mService = new CookBeanService(this);
        db = new DBServices(this);
        mCollectLinearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveData(cookBean);
                Toast.makeText(getApplicationContext(), "收藏成功", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void saveData(CookBean cookBean) {
        ByteArrayOutputStream arrayOutputStream = new ByteArrayOutputStream();
        try {
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(arrayOutputStream);
            objectOutputStream.writeObject(cookBean);
            objectOutputStream.flush();
            byte data[] = arrayOutputStream.toByteArray();
            objectOutputStream.close();
            arrayOutputStream.close();
            SQLiteDatabase database = db.getWritableDatabase();
            database.execSQL("insert into Test (person) values(?)", new Object[] { data });
            //database.insert()
            database.close();
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }


    public void setListViewHeightBasedOnChildren1(ListView listView) {
        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null) {
            return;
        }
        int totalHeight = 0;
        for (int i = 0; i < listAdapter.getCount(); i++) {
            View listItem = listAdapter.getView(i, null, listView);
            listItem.measure(0, 0);  //<span style="font-family: Helvetica, Tahoma, Arial, sans-serif; font-size: 14px; line-height: 25px; text-align: left; ">ÔÚ»¹Ã»ÓÐ¹¹œšView Ö®Ç°ÎÞ·šÈ¡µÃViewµÄ¶È¿í¡£&nbsp;</span><span style="font-family: Helvetica, Tahoma, Arial, sans-serif; font-size: 14px; line-height: 25px; text-align: left; ">ÔÚŽËÖ®Ç°ÎÒÃÇ±ØÐëÑ¡ measure Ò»ÏÂ.&nbsp;</span><br style="font-family: Helvetica, Tahoma, Arial, sans-serif; font-size: 14px; line-height: 25px; text-align: left; ">
            totalHeight += listItem.getMeasuredHeight();
        }
        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight
                + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
        //params.height += 16;// if without this statement,the listview will be a little short
        //listView.getDividerHeight()»ñÈ¡×ÓÏîŒä·Öžô·ûÕŒÓÃµÄžß¶È
        //params.height×îºóµÃµœÕûžöListViewÍêÕûÏÔÊŸÐèÒªµÄžß¶È
        //((ViewGroup.MarginLayoutParams) params).setMargins(10, 10, 10, 10); // 可删除
        listView.setLayoutParams(params);
    }

}

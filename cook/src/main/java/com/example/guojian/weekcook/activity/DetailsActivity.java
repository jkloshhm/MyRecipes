package com.example.guojian.weekcook.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.ScrollView;
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
import com.example.guojian.weekcook.utils.GetBitmapFromSdCardUtil;
import com.example.guojian.weekcook.utils.ImageLoaderUtil;
import com.example.guojian.weekcook.utils.ScreenShotUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class DetailsActivity extends Activity {
    public static String DCIMCamera_PATH = Environment
            .getExternalStorageDirectory() + "/Cooking/ScreenShotImg/";
    private static DBServices db;
    private static ArrayList<CookBean> cookBeanlist;
    private static ArrayList<String> cookIdList = new ArrayList<>();
    private static ProgressDialog dialog;
    Context context;
    private String realIp;
    private CookBean cookBean;
    private ImageView mCollectImg, mDetailsImage, mShareImg;
    private List<MaterialBean> materialBeanlist;
    private List<ProcessBean> processBeenlist;
    private ListView mListViewMaterial, mListViewProcess;
    private MaterialAdapter mMaterialAdapter;
    private ProcessAdapter mProcessAdapter;
    private LinearLayout mlinearLayout, mCollectLinearLayout, mButtonBack, mShareLinearLayout;
    private ScrollView mScrollView;
    private boolean isRed;
    private TextView mName, mContent, mPeopleNum, mCookingTime, mTag;
    private String TAG = "jkloshhm-----------DetailsActivity------";
    private Button btn_refresh;
    private Handler refrsh_handler;

    //是否取消收藏
    public void setCancleColltion() {
        //提示对话框
        AlertDialog builder = new AlertDialog.Builder(this).create();
        builder.setView(getLayoutInflater().inflate(R.layout.alert_dialog_view, null));
        builder.setButton(DialogInterface.BUTTON_POSITIVE, "移除", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                mCollectImg.setImageDrawable(getResources().getDrawable(R.mipmap.collection_gray));
                isRed = false;
                Toast.makeText(DetailsActivity.this, "已取消收藏~", Toast.LENGTH_SHORT).show();
            }
        });
        builder.setButton(DialogInterface.BUTTON_NEGATIVE, "取消", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.show();
        Button pButton = builder.getButton(DialogInterface.BUTTON_POSITIVE);
        pButton.setTextColor(getResources().getColor(R.color.red_theme));
        Button nButton = builder.getButton(DialogInterface.BUTTON_NEGATIVE);
        nButton.setTextColor(getResources().getColor(R.color.gray));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = getApplicationContext();

        Log.i(TAG, "DetailsActivity____________onCreate()");
        setContentView(R.layout.activity_details);
        initViews();

        mShareLinearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                for (int i = 0; i < mScrollView.getChildCount(); i++) {
                    mScrollView.getChildAt(i).setBackgroundColor(Color.parseColor("#ffffff"));

                }
                Log.i(TAG, "点击了分享~~~~");
                dialog = new ProgressDialog(DetailsActivity.this);
                dialog.setMessage("截屏中，请稍等...");
                dialog.show();
                //开始执行AsyncTask，并传入某些数据
                new ScreenShotTask().execute("New Text");
                /*ScreenShot.savePic(ScreenShot.compressImage(ScreenShot
                        .getBitmapByView(mScrollView)));
                Toast.makeText(DetailsActivity.this, "open file", Toast.LENGTH_SHORT).show();
                String fname = ScreenShot.savePic(ScreenShot.getBitmapByView(mScrollView));
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setDataAndType(Uri.parse("file://"+fname), "image");
                Toast.makeText(DetailsActivity.this, "open file", Toast.LENGTH_SHORT).show();*/
            }
        });
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
                    setCancleColltion();
                } else {
                    mCollectImg.setImageDrawable(getResources().getDrawable(R.mipmap.collection_red));
                    isRed = true;
                    Toast.makeText(DetailsActivity.this, "收藏成功~", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    protected void onPause() {
        Log.i(TAG, "DetailsActivity ____________onPause()");
        super.onPause();
        if (realIp.equals("mary")) {
            if (isRed) {//保存
                MyDBServiceUtils.saveData(cookBean, db);
            }
        } else {
            if (!isRed) {//删除
                MyDBServiceUtils.delectData(cookBean, db);
                cookBean.setReal_ip("mary");
            }
        }
    }

    private void initDB() {
        db = MyDBServiceUtils.getInstance(this);
        cookBeanlist = MyDBServiceUtils.getAllObject(db);
        for (int i = 0; i < cookBeanlist.size(); i++) {
            String real_ip = cookBeanlist.get(i).getReal_ip();
            cookIdList.add(real_ip);
        }
    }

    private void setUpViews() {
        mName.setText(cookBean.getName_cook());
        String img_url = cookBean.getPic();
        ImageLoaderUtil.setPicBitmap2(mDetailsImage, img_url);
        String mContentString = cookBean.getContent().replace("<br />", "");
        mContent.setText(mContentString);
        String mPeopleNumString = "用餐人数: " + cookBean.getPeoplenum();
        mPeopleNum.setText(mPeopleNumString);
        String mCookingTimeString = "烹饪时间: " + cookBean.getCookingtime();
        mCookingTime.setText(mCookingTimeString);
        String mTagString = "标签: " + cookBean.getTag_cook();
        mTag.setText(mTagString);
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
        mShareImg = (ImageView) findViewById(R.id.iv_share_the_cook_data);
        mScrollView = (ScrollView) findViewById(R.id.scrollView_details);
        mShareLinearLayout = (LinearLayout) findViewById(R.id.ll_share_the_cook_data);
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

    private class ScreenShotTask extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... params) {
            try {
                if (GetBitmapFromSdCardUtil.hasSdcard()) {
                    String fileName = ScreenShotUtils.savePic(ScreenShotUtils.getScrollViewBitmap(mScrollView));
                    shareMsg("DetailsActivity",null,null,fileName);
                    //线程睡眠5秒，模拟耗时操作，这里面的内容Android系统会自动为你启动一个新的线程执行
                    //Thread.sleep(5000);
                } else {
                    Toast.makeText(getApplicationContext(), "无SD卡,存储失败!", Toast.LENGTH_SHORT).show();
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
            return params[0];
        }

        @Override
        protected void onPostExecute(String result) {
            //更新UI的操作，这里面的内容是在UI线程里面执行的
            dialog.dismiss();
            Toast.makeText(getApplicationContext(), "截图已保存", Toast.LENGTH_SHORT).show();
        }

    }

    /**
     * 分享功能
     *
     * @param activityTitle
     *            Activity的名字
     * @param msgTitle
     *            消息标题
     * @param msgText
     *            消息内容
     * @param imgPath
     *            图片路径，不分享图片则传null
     */
    public void shareMsg(String activityTitle, String msgTitle, String msgText,
                         String imgPath) {
        Intent intent = new Intent(Intent.ACTION_SEND);
        if (imgPath == null || imgPath.equals("")) {
            intent.setType("text/plain"); // 纯文本
        } else {
            File f = new File(imgPath);
            if (f != null && f.exists() && f.isFile()) {
                intent.setType("image/*");
                Uri u = Uri.fromFile(f);
                intent.putExtra(Intent.EXTRA_STREAM, u);
            }
        }
        intent.putExtra(Intent.EXTRA_SUBJECT, msgTitle);
        intent.putExtra(Intent.EXTRA_TEXT, msgText);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(Intent.createChooser(intent, activityTitle));
    }


}

package com.example.guojian.weekcook.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.example.guojian.weekcook.R;
import com.example.guojian.weekcook.adapter.CookListAdapter;
import com.example.guojian.weekcook.bean.CookBean;
import com.example.guojian.weekcook.bean.MaterialBean;
import com.example.guojian.weekcook.bean.ProcessBean;
import com.example.guojian.weekcook.utils.GetJsonUtils;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class CookListActivity extends Activity {
    private static List<CookBean> cookBeanList = new ArrayList<>();
    private static List<MaterialBean> materialBeanList = null;
    private static List<ProcessBean> processBeanList = null;
    private String TAG = "jkloshhm_CookDemo";
    private TextView mNameTextView;
    private ListView mLisview;
    private CookListAdapter mCookListAdapter;
    private LinearLayout mLoadingLinearLayout, mNoMassageLinearLayout, mBackLinearLayout;
    final Handler handlerClass = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            Bundle jsonBundle = msg.getData();
            String classType = jsonBundle.getString("classType");
            String jsonErrorMessage = jsonBundle.getString("errorMessage");
            String jsonData = jsonBundle.getString("stringBody");
            //Log.i(TAG, "--------->>jsonData====" + jsonData);
            Log.i(TAG, "--------->>jsonErrorMessage====" + jsonErrorMessage);
            if (jsonData != null && jsonErrorMessage == null) {
                if (classType != null && classType.equals("GetDataBySearchName")) {//按名称搜索菜谱
                    getDataAndUpdateUI(jsonData);
                } else if (classType != null && classType.equals("GetDataClass")) {//分类名称

                } else if (classType != null && classType.equals("GetDataByClassId")) {//分类名称ID
                    getDataAndUpdateUI(jsonData);
                }
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cook_list);
        mNameTextView = (TextView) findViewById(R.id.tv_cook_name);
        mLisview = (ListView) findViewById(R.id.lv_cook_list);
        mLoadingLinearLayout = (LinearLayout) findViewById(R.id.ll_loading_list);
        mNoMassageLinearLayout = (LinearLayout) findViewById(R.id.ll_no_data_massage);
        mBackLinearLayout = (LinearLayout) findViewById(R.id.ll_back_class_home);
        Intent intent = this.getIntent();
        final String CookType = intent.getStringExtra("CookType");
        final String classId = intent.getStringExtra("classId");
        final String name = intent.getStringExtra("name");
        mNameTextView.setText(name);
        new Thread(new Runnable() {
            @Override
            public void run() {
                if (CookType.equals("GetDataByClassId")) {
                    GetJsonUtils.GetDataByClassId(handlerClass, classId);
                }else if(CookType.equals("GetDataBySearchName")){
                    GetJsonUtils.GetDataBySearchName(handlerClass,name);

                }

            }
        }).start();
        mCookListAdapter = new CookListAdapter(this, cookBeanList);
        mBackLinearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        mLisview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                CookBean cookBean01 = cookBeanList.get(position);
                try {
                    Intent intent = new Intent(CookListActivity.this, DetailsActivity.class);
                    Bundle b = new Bundle();
                    b.putSerializable("cookBean01", cookBean01);
                    intent.putExtras(b);
                    startActivity(intent);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void getDataAndUpdateUI(String data){
        try {
            cookBeanList.clear();
            JSONObject dataJsonObject = new JSONObject(data);
            String result = dataJsonObject.getString("result");
            String status = dataJsonObject.getString("status");
            String statusMsg = dataJsonObject.getString("msg");
            Log.i(TAG, "--------result----->->->->--" + result);
            if (result.equals("") && status.equals("205")
                    && statusMsg.equals("没有信息")) {
                mLoadingLinearLayout.setVisibility(View.GONE);
                mLisview.setVisibility(View.GONE);
                mNoMassageLinearLayout.setVisibility(View.VISIBLE);
            } else {
                JSONObject resultJsonObject = new JSONObject(result);
                JSONArray listJsonArray = resultJsonObject.getJSONArray("list");
                for (int i = 0; i < listJsonArray.length(); i++) {
                    JSONObject cookJsonObject = listJsonArray.getJSONObject(i);
                    String id_cook = cookJsonObject.getString("id");
                    String classid_cook = cookJsonObject.getString("classid");
                    String name_cook = cookJsonObject.getString("name");
                    String peoplenum = cookJsonObject.getString("peoplenum");
                    String preparetime = cookJsonObject.getString("preparetime");
                    String cookingtime = cookJsonObject.getString("cookingtime");
                    String content = cookJsonObject.getString("content");
                    String pic = cookJsonObject.getString("pic");
                    String tag = cookJsonObject.getString("tag");
                    JSONArray materialArray = cookJsonObject.getJSONArray("material");
                    JSONArray processJsonArray = cookJsonObject.getJSONArray("process");
                    materialBeanList = new ArrayList<>();
                    for (int j = 0; j < materialArray.length(); j++) {
                        JSONObject materialJsonObject = materialArray.getJSONObject(j);
                        materialBeanList.add(new MaterialBean(//String amount, String mname, String type
                                materialJsonObject.getString("amount"),
                                materialJsonObject.getString("mname"),
                                materialJsonObject.getString("type")));
                    }
                    processBeanList = new ArrayList<>();
                    for (int k = 0; k < processJsonArray.length(); k++) {
                        JSONObject processJsonObject = processJsonArray.getJSONObject(k);
                        processBeanList.add(new ProcessBean(
                                processJsonObject.getString("pcontent"),
                                processJsonObject.getString("pic")));
                    }
                    cookBeanList.add(new CookBean(id_cook, classid_cook, name_cook,
                            peoplenum, preparetime,
                            cookingtime, content, pic,
                            tag, materialBeanList, processBeanList));
                }
                mLoadingLinearLayout.setVisibility(View.GONE);
                mLisview.setVisibility(View.VISIBLE);
                mLisview.setAdapter(mCookListAdapter);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
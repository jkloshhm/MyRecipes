package com.example.guojian.weekcook.activity;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.TextView;

import com.example.guojian.weekcook.R;
import com.example.guojian.weekcook.utils.GetJsonUtils;

import org.json.JSONArray;
import org.json.JSONObject;

public class ClassListActivity extends Activity {
    private String TAG = "jkloshhm_CookDemo";
    private TextView textView;
    final Handler handlerClassActivity = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            Bundle jsonBundle = msg.getData();
            String classType = jsonBundle.getString("classType");
            String jsonErrorMessage = jsonBundle.getString("errorMessage");
            String jsonData = jsonBundle.getString("stringBody");
            Log.i(TAG, "--------->>jsonData====" + jsonData);
            Log.i(TAG, "--------->>jsonErrorMessage====" + jsonErrorMessage);
            if (jsonData != null && jsonErrorMessage == null) {
                if (classType != null && classType.equals("GetDataBySearchName")) {//按名称搜索菜谱
                    try {
                        JSONObject dataJsonObject = new JSONObject(jsonData);
                        String result = dataJsonObject.getString("result");
                        String list = new JSONObject(result).getString("list");
                        JSONArray listJsonArray = new JSONArray(list);
                        //for (int i = 0; i < listJsonArray.length();i++);
                        JSONObject lisJsonObject = listJsonArray.getJSONObject(0);
                        String name = lisJsonObject.getString("name");
                        //textView.setText(jsonData);
                        //textView.setText(name);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else if (classType != null && classType.equals("GetDataClass")) {//分类名称
                    try {
                        JSONObject dataJsonObject = new JSONObject(jsonData);
                        String result = dataJsonObject.getString("result");
                        JSONArray resultJsonArray = new JSONArray(result);
                        StringBuffer s = new StringBuffer();
                        for (int i = 0; i < resultJsonArray.length(); i++) {
                            JSONObject resultJsonObject = resultJsonArray.getJSONObject(i);
                            String classId_parent = resultJsonObject.getString("classid");
                            String className_parent = resultJsonObject.getString("name");
                            String parentId_parent = resultJsonObject.getString("parentid");
                            JSONArray list_parent = resultJsonObject.getJSONArray("list");

                        }
                        Log.i(TAG, "S=" + s);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else if (classType != null && classType.equals("GetDataByClassId")) {//分类名称ID
                    try {

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
            textView.setText(jsonData);
            /*mListViewParent.setAdapter(parentClassAdapter);*/
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_class);
        textView = (TextView) findViewById(R.id.tv_class_content);
        final String classId = getIntent().getStringExtra("classId");
        new Thread(new Runnable() {
            @Override
            public void run() {
                GetJsonUtils.GetDataByClassId(handlerClassActivity, classId);
                //GetJsonUtils.GetDataBySearchNameId(handlerClassActivity,"49580");
                //GetJsonUtils.GetDataBySearchNameId(handlerClassActivity,"49580");
                //GetJsonUtils.GetDataBySearchName(handlerClassActivity,"红烧肉");
            }
        }).start();
    }
}

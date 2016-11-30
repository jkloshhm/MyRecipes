package com.example.guojian.weekcook.fragment;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.guojian.weekcook.R;
import com.example.guojian.weekcook.activity.CookListActivity;
import com.example.guojian.weekcook.utils.GetJsonUtils;
import com.example.guojian.weekcook.utils.ImageLoaderUtil;
import com.example.guojian.weekcook.utils.RadomNum;

import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class SearchFragment extends Fragment {
    private static String TAG = "jkloshhm___SearchFragment";
    private static TextView textView001, textView002, textView003;
    private static ImageView img001, img002, img003;
    final static Handler handlerSearch = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            Bundle jsonBundle = msg.getData();
            String classType = jsonBundle.getString("classType");
            String jsonErrorMessage = jsonBundle.getString("errorMessage");
            String jsonData = jsonBundle.getString("stringBody");
            String tag = jsonBundle.getString("tag");
            Log.i(TAG, "--------->>jsonData====" + jsonData);
            //Log.i(TAG, "--------->>jsonErrorMessage====" + jsonErrorMessage);
            if (jsonData != null) {
                if (classType != null && classType.equals("GetDataBySearchNameId")) {//搜索名称ID
                    getDataAndUpdateUI(jsonData,tag);
                }
            }
        }
    };
    final int num[] = RadomNum.makeCount();
    private SharedPreferences preferences;
    private SharedPreferences.Editor editor;
    private Context mContext;

    public SearchFragment() {
        // Required empty public constructor
    }

    private static void getDataAndUpdateUI(String data, String tag) {
        StringBuilder s = new StringBuilder("");
        if (tag != null) {
            String name = null;
            String pic = null;
            try {
                JSONObject dataJsonObject = new JSONObject(data);
                String result = dataJsonObject.getString("result");
                JSONObject resultJsonObject = new JSONObject(result);
                String id = resultJsonObject.getString("id");
                String classId = resultJsonObject.getString("classid");
                name = resultJsonObject.getString("name");
                pic = resultJsonObject.getString("pic");
                //JSONArray list_parent = resultJsonObject.getJSONArray("list");
                //s.append(name);
            } catch (Exception e) {
                e.printStackTrace();
            }

            if (tag.equals("1")) {
                textView001.setText(name);
                ImageLoaderUtil.setPicBitmap1(img001,pic);
            } else if (tag.equals("2")) {
                textView002.setText(name);
                ImageLoaderUtil.setPicBitmap1(img002,pic);
            } else if (tag.equals("3")) {
                textView003.setText(name);
                ImageLoaderUtil.setPicBitmap1(img003,pic);
            }
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mContext = getActivity();
        View view = inflater.inflate(R.layout.fragment_search, container, false);
        textView001 = (TextView) view.findViewById(R.id.text001);
        textView002 = (TextView) view.findViewById(R.id.text002);
        textView003 = (TextView) view.findViewById(R.id.text003);
        img001 = (ImageView) view.findViewById(R.id.img001);
        img002 = (ImageView) view.findViewById(R.id.img002);
        img003 = (ImageView) view.findViewById(R.id.img003);

        final EditText mSearchName = (EditText) view.findViewById(R.id.et_search_name);
        LinearLayout mSearchLinearLayout = (LinearLayout) view.findViewById(R.id.ll_search);
        mSearchLinearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (TextUtils.isEmpty(mSearchName.getText().toString().trim())) {
                    Toast.makeText(mContext, "请输入正确的菜名", Toast.LENGTH_SHORT).show();
                } else {
                    //setEditTextInhibitInputSpeChat(mSearchName);
                    Intent mIntent = new Intent(mContext, CookListActivity.class);
                    mIntent.putExtra("CookType", "GetDataBySearchName");
                    mIntent.putExtra("name", mSearchName.getText().toString().replace(" ", ""));
                    mContext.startActivity(mIntent);
                }
            }
        });

        preferences = mContext.getSharedPreferences("cooking", Context.MODE_PRIVATE);
        editor = preferences.edit();
        //String dateFormat = "yyyyMMdd";
        SimpleDateFormat df = new SimpleDateFormat("yyyyMMddHH");//设置日期格式
        String date = df.format(new Date());
        Log.i("jkloshhm", date);
        if (preferences.getString("date", null) == null) {
            editor.putString("date", df.format(new Date()));
        }
        editor.putString("num01", String.valueOf(num[0]));
        editor.putString("num02", String.valueOf(num[1]));
        editor.putString("num03", String.valueOf(num[2]));
        //Log.i("jkloshhm_String(date)", preferences.getString("date", null));
        if (date.equals(preferences.getString("date", null))) {
            Log.i("jkloshhm______if", date);
            editor.putString("num01", preferences.getString("num01", null));
            editor.putString("num02", preferences.getString("num02", null));
            editor.putString("num03", preferences.getString("num03", null));
        } else {
            Log.i("jkloshhm______else", date);
            int num[] = RadomNum.makeCount();
            editor.putString("date", date);
            editor.putString("num01", String.valueOf(num[0]));
            editor.putString("num02", String.valueOf(num[1]));
            editor.putString("num03", String.valueOf(num[2]));
        }
        editor.apply();

        final List<Integer> numList = new ArrayList<>();
        numList.add(0, Integer.parseInt(preferences.getString("num01", null)));
        numList.add(1, Integer.parseInt(preferences.getString("num02", null)));
        numList.add(2, Integer.parseInt(preferences.getString("num03", null)));
        Log.i("jkloshhm", numList.toString());
        try {
            Thread thread = new Thread(new Runnable() {
                @Override
                public void run() {
                    for (int i = 0; i < 3; i++) {
                        GetJsonUtils.GetDataBySearchNameId(handlerSearch, "" + numList.get(i), String.valueOf(i + 1));
                    }
                }
            });
            thread.start();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return view;
    }


}

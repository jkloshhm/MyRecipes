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
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.guojian.weekcook.R;
import com.example.guojian.weekcook.activity.CookListActivity;
import com.example.guojian.weekcook.utils.GetJsonUtils;
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
    /**
     * 禁止EditText输入特殊字符
     */
/*    public static void setEditTextInhibitInputSpeChat(EditText editText) {
        InputFilter filter = new InputFilter() {
            @Override
            public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
                String speChat = "[`~!@#$%^&*()+=|{}':;',\\[\\].<>/?~！@#￥%……&*（）——+|{}【】‘；：”“’。，、？]";
                Pattern pattern = Pattern.compile(speChat);
                Matcher matcher = pattern.matcher(source.toString());
                if (matcher.find()) return "";
                else return null;
            }
        };
        editText.setFilters(new InputFilter[]{filter});
    }*/
    private static TextView textView001, textView002, textView003;
    final static Handler handlerSearch = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            Bundle jsonBundle = msg.getData();
            String classType = jsonBundle.getString("classType");
            String jsonErrorMessage = jsonBundle.getString("errorMessage");
            String jsonData = jsonBundle.getString("stringBody");
            String tag = jsonBundle.getString("tag");
            //Log.i(TAG, "--------->>jsonData====" + jsonData);
            //Log.i(TAG, "--------->>jsonErrorMessage====" + jsonErrorMessage);
            if (jsonData != null && jsonErrorMessage == null) {
                /*if (classType != null && classType.equals("GetDataClass")) {//分类名称
                    getDataAndUpdateUI(jsonData);//GetDataBySearchNameId
                }else if (classType != null && classType.equals("GetDataBySearchName")) {//按名称搜索菜谱
                } else if (classType != null && classType.equals("GetDataClass")) {//分类名称
                   getDataAndUpdateUI(jsonData);
                } else if (classType != null && classType.equals("GetDataByClassId")) {//分类名称ID
                }*/
                if (classType != null && classType.equals("GetDataBySearchNameId")) {//搜索名称ID
                    StringBuilder s = new StringBuilder("");
                    if (tag != null) {
                        try {
                            JSONObject dataJsonObject = new JSONObject(jsonData);
                            String result = dataJsonObject.getString("result");
                            JSONObject resultJsonObject = new JSONObject(result);
                            String id = resultJsonObject.getString("id");
                            String classId = resultJsonObject.getString("classid");
                            String name = resultJsonObject.getString("name");
                            //JSONArray list_parent = resultJsonObject.getJSONArray("list");
                            s.append(id + "---" + name);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                        if (tag.equals("1")) {
                            textView001.setText(s);
                        } else if (tag.equals("2")) {
                            textView002.setText(s);
                        } else if (tag.equals("3")) {
                            textView003.setText(s);
                        }
                    }
                }
            }
        }
    };
    int num[] = RadomNum.makeCount();
    private SharedPreferences preferences;
    private SharedPreferences.Editor editor;
    private Context mContext;

    public SearchFragment() {
        // Required empty public constructor
    }

    private static void getDataAndUpdateUI(String data) {
        try {
            JSONObject dataJsonObject = new JSONObject(data);
            String result = dataJsonObject.getString("result");
            JSONObject resultJsonObject = new JSONObject(result);
            String id = resultJsonObject.getString("id");
            String classId = resultJsonObject.getString("classid");
            String name = resultJsonObject.getString("name");
            //JSONArray list_parent = resultJsonObject.getJSONArray("list");
            StringBuilder s = new StringBuilder("");
            s.append(id + "---" + name);
            textView001.setText(s);
        } catch (Exception e) {
            e.printStackTrace();
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
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");//设置日期格式
        String date = df.format(new Date());
        Log.i("jkloshhm", date);
        if (preferences.getString("date", null) == null) {
            editor.putString("date", df.format(new Date()));
        }
        editor.putString("num01", String.valueOf(num[0]));
        editor.putString("num02", String.valueOf(num[1]));
        editor.putString("num03", String.valueOf(num[2]));
        if (date.equals(preferences.getString("date", null))) {
            editor.putString("num01", preferences.getString("num01", null));
            editor.putString("num02", preferences.getString("num02", null));
            editor.putString("num03", preferences.getString("num03", null));
        } else {
            int num[] = RadomNum.makeCount();
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

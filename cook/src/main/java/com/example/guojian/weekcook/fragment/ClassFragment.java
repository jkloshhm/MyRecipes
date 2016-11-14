package com.example.guojian.weekcook.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;

import com.example.guojian.weekcook.R;
import com.example.guojian.weekcook.activity.ClassListActivity;
import com.example.guojian.weekcook.adapter.ChildrenClassAdapter;
import com.example.guojian.weekcook.adapter.ParentClassAdapter;
import com.example.guojian.weekcook.bean.ChildrenClassBean;
import com.example.guojian.weekcook.bean.ParentClassBean;
import com.example.guojian.weekcook.utils.GetJsonUtils;
import com.example.guojian.weekcook.utils.HeaderGridView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class ClassFragment extends Fragment {
    //private static TextView textView;
    private static ParentClassBean parentClassBean;
    private static ChildrenClassBean childrenClassBean;
    private static List<ParentClassBean> parentClassBeenList;
    private static List<ChildrenClassBean> childrenClassBeenList;
    private static Context context;
    private String TAG = "guojian_CookDemo";
    private Button mButtonSearch, mButtonClass;
    private ListView mListViewParent, mListViewChildren;
    private HeaderGridView mGridViewChildren;
    private ParentClassAdapter parentClassAdapter;
    private ChildrenClassAdapter childrenClassAdapter;
    final Handler handler = new Handler() {
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
                            childrenClassBeenList = new ArrayList<>();
                            for (int j = 0; j < list_parent.length(); j++) {
                                JSONObject list_children = list_parent.getJSONObject(j);
                                childrenClassBean = new ChildrenClassBean(
                                        list_children.getString("classid"),
                                        list_children.getString("name"),
                                        list_children.getString("parentid"));
                                childrenClassBeenList.add(childrenClassBean);
                                Log.i(TAG, "name=========" + list_children.getString("name"));
                            }
                            parentClassBean = new ParentClassBean(
                                    childrenClassBeenList,
                                    classId_parent,
                                    className_parent,
                                    parentId_parent);
                            parentClassBeenList.add(parentClassBean);
                            s.append(classId_parent + "-" +
                                    className_parent + "-" +
                                    parentId_parent + "\n");
                        }
                        Log.i(TAG, "S=" + s);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else if (classType != null && classType.equals("GetDataByClassId")) {//分类名称ID
                    try {
                        /*JSONObject dataJsonObject = new JSONObject(jsonData);
                        String result = dataJsonObject.getString("result");
                        String list = new JSONObject(result).getString("list");
                        JSONArray listJsonArray = new JSONArray(list);
                        //for (int i = 0; i < listJsonArray.length();i++);
                        JSONObject lisJsonObject = listJsonArray.getJSONObject(0);
                        String name = lisJsonObject.getString("name");*/
                        //textView.setText(jsonData);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
            mListViewParent.setAdapter(parentClassAdapter);
            initChildrenView(0);
        }
    };

    public ClassFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container, Bundle savedInstanceState) {
        context = getActivity();
        // Inflate the layout for this fragment
        View mClassView = inflater.inflate(R.layout.fragment_class, container, false);
        //LayoutInflater mLayoutInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View headerView = inflater.inflate(R.layout.layout_class_children_header, container, false);
        mListViewParent = (ListView) mClassView.findViewById(R.id.lv_parent_class);
        //mListViewChildren = (ListView) mClassView.findViewById(R.id.lv_children_class);
        mGridViewChildren = (HeaderGridView) mClassView.findViewById(R.id.lv_children_class);
        parentClassBeenList = new ArrayList<>();
        new Thread(new Runnable() {
            @Override
            public void run() {
                GetJsonUtils.GetDataClass(handler);
            }
        }).start();
        parentClassAdapter = new ParentClassAdapter(context, parentClassBeenList);
        mListViewParent.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                initChildrenView(position);
            }
        });
        mGridViewChildren.addHeaderView(headerView);
        mGridViewChildren.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ChildrenClassBean childrenClassBean1 = childrenClassBeenList.get(position);
                String classId = childrenClassBean1.getChildrenClassId();
                Intent intent = new Intent(context, ClassListActivity.class);
                intent.putExtra("classId", classId);
                startActivity(intent);
            }
        });
        return mClassView;
    }

    public void initChildrenView(int position) {
        ParentClassBean parentClassBean1 = parentClassBeenList.get(position);
        childrenClassBeenList = parentClassBean1.getChildrenClassBeen();
        childrenClassAdapter = new ChildrenClassAdapter(childrenClassBeenList, context);
        mGridViewChildren.setAdapter(childrenClassAdapter);
        parentClassAdapter.setSelectItem(position);
        parentClassAdapter.notifyDataSetInvalidated();
    }


}

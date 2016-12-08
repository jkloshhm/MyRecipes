package com.example.guojian.weekcook.fragment;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.SimpleAdapter;

import com.example.guojian.weekcook.R;
import com.example.guojian.weekcook.activity.CookListActivity;
import com.example.guojian.weekcook.activity.DetailsActivity;
import com.example.guojian.weekcook.activity.SearchActivity;
import com.example.guojian.weekcook.adapter.AutoPlayingViewPager;
import com.example.guojian.weekcook.bean.CookBean;
import com.example.guojian.weekcook.bean.MaterialBean;
import com.example.guojian.weekcook.bean.ProcessBean;
import com.example.guojian.weekcook.utils.GetJsonUtils;
import com.example.guojian.weekcook.utils.RadomNum;

import org.json.JSONArray;
import org.json.JSONObject;

import java.lang.ref.WeakReference;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 */
public class HomeFragment extends Fragment {

    private static String TAG = "jkloshhm___SearchFragment";
    private static List<CookBean> cookBeanList = null;
    private static AutoPlayingViewPager mAutoPlayingViewPager;
    private static Context mContext;
    private static AutoPlayingViewPager.OnPageItemClickListener onPageItemClickListener = new AutoPlayingViewPager.OnPageItemClickListener() {

        @Override
        public void onPageItemClick(int position, CookBean cookBean) {
            // 直接返回链接,使用WebView加载
            if (cookBean != null) {
                //链接存在时才进行下一步操作,当然，这只是简单判断,这个字符串不是正确链接,则需要加上正则表达式判断。
                Intent intent = new Intent(mContext, DetailsActivity.class);
                intent.putExtra("cookBean01", cookBean);
                mContext.startActivity(intent);
            }
        }

    };
    private final int num[] = RadomNum.makeCount();
    private List<Integer> numList = null;
    private MyHandler handlerSearch = new MyHandler(this);
    private SharedPreferences preferences;
    private SharedPreferences.Editor editor;
    private int imgId[] = {R.mipmap.nangua, R.mipmap.muer, R.mipmap.ou, R.mipmap.shanyao,
            R.mipmap.baicai, R.mipmap.hongshu, R.mipmap.yangrou, R.mipmap.niurou};
    private String imgName[] = {"南瓜", "木耳", "藕", "山药", "白菜", "红薯", "羊肉", "牛肉",};
    private GridView mGridViewHotMaterial;
    private List<Map<String, Object>> mHotMaterialList;
    private SimpleAdapter mHotMaterialSimpleAdapter;

    public HomeFragment() {
        // Required empty public constructor
    }

    private static void getDataAndUpdateUI(String data, String tag) {
        if (tag != null) {
            try {
                JSONObject dataJsonObject = new JSONObject(data);
                String result = dataJsonObject.getString("result");
                JSONObject resultJsonObject = new JSONObject(result);
                String id_cook = resultJsonObject.getString("id");
                String classid_cook = resultJsonObject.getString("classid");
                String name_cook = resultJsonObject.getString("name");
                String peoplenum = resultJsonObject.getString("peoplenum");
                String preparetime = resultJsonObject.getString("preparetime");
                String cookingtime = resultJsonObject.getString("cookingtime");
                String content = resultJsonObject.getString("content");
                String picUrl = resultJsonObject.getString("pic");
                String tagCook = resultJsonObject.getString("tag");
                String maryString = "mary";
                JSONArray materialArray = resultJsonObject.getJSONArray("material");
                JSONArray processJsonArray = resultJsonObject.getJSONArray("process");
                List<MaterialBean> materialBeanList = new ArrayList<>();
                for (int j = 0; j < materialArray.length(); j++) {
                    JSONObject materialJsonObject = materialArray.getJSONObject(j);
                    materialBeanList.add(new MaterialBean(
                            materialJsonObject.getString("amount"),
                            materialJsonObject.getString("mname"),
                            materialJsonObject.getString("type")));
                }
                List<ProcessBean> processBeanList = new ArrayList<>();
                for (int k = 0; k < processJsonArray.length(); k++) {
                    JSONObject processJsonObject = processJsonArray.getJSONObject(k);
                    processBeanList.add(new ProcessBean(
                            processJsonObject.getString("pcontent"),
                            processJsonObject.getString("pic")));
                }
                cookBeanList.add(new CookBean(id_cook, classid_cook, name_cook,
                        peoplenum, preparetime,
                        cookingtime, content, picUrl,
                        tagCook, materialBeanList, processBeanList, maryString));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mContext = getActivity();
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        LinearLayout mSearchLinearLayout = (LinearLayout) view.findViewById(R.id.ll_search);
        mSearchLinearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent mIntent = new Intent(mContext, SearchActivity.class);
                mContext.startActivity(mIntent);
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

        numList = new ArrayList<>();
        numList.add(0, Integer.parseInt(preferences.getString("num01", null)));
        numList.add(1, Integer.parseInt(preferences.getString("num02", null)));
        numList.add(2, Integer.parseInt(preferences.getString("num03", null)));
        Log.i("jkloshhm", numList.toString());

        mAutoPlayingViewPager = (AutoPlayingViewPager) view.findViewById(R.id.auto_play_viewpager);
        setViewPager();

        mGridViewHotMaterial = (GridView) view.findViewById(R.id.gv_hot_material);
        //新建List
        mHotMaterialList = new ArrayList<Map<String, Object>>();
        //获取数据
        getGridViewHotMaterialData();
        //新建适配器
        String[] from = {"image","name"};
        int[] to = {R.id.iv_hot_material_item_img, R.id.tv_hot_material_item_name};
        mHotMaterialSimpleAdapter = new SimpleAdapter(mContext, mHotMaterialList, R.layout.hot_material_gv_adapter_item, from, to);
        //配置适配器
        mGridViewHotMaterial.setAdapter(mHotMaterialSimpleAdapter);
        mGridViewHotMaterial.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Map<String, Object> map = new HashMap<String, Object>();
                map = mHotMaterialList.get(position);
                String name = (String) map.get("name");
                Intent intent = new Intent(mContext,CookListActivity.class);
                intent.putExtra("name",name);
                intent.putExtra("CookType","GetDataBySearchName");
                startActivity(intent);
            }
        });
        return view;
    }

    public List<Map<String, Object>> getGridViewHotMaterialData() {
        //cion和iconName的长度是相同的，这里任选其一都可以
        for (int i = 0; i < imgId.length; i++) {
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("name", imgName[i]);
            map.put("image", imgId[i]);
            mHotMaterialList.add(map);
        }
        return mHotMaterialList;
    }

    private void setViewPager() {
        //使用异步加载模拟网络请求
        MyAsyncTask myAsyncTask = new MyAsyncTask();
        myAsyncTask.execute();
    }

    @Override
    public void onResume() {
        //没有数据时不执行startPlaying,避免执行几次导致轮播混乱
        if (cookBeanList != null && !cookBeanList.isEmpty()) {
            mAutoPlayingViewPager.startPlaying();
        }
        super.onResume();
    }

    @Override
    public void onPause() {
        mAutoPlayingViewPager.stopPlaying();
        super.onPause();
    }

    private class MyHandler extends Handler {
        WeakReference<HomeFragment> homeFragmentWeakReference;

        MyHandler(HomeFragment homeFragment) {
            homeFragmentWeakReference = new WeakReference<HomeFragment>(homeFragment);
        }

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
                    getDataAndUpdateUI(jsonData, tag);
                    if (cookBeanList.size() == 3) {
                        //mAutoPlayingViewPager.notifyAll();
                        mAutoPlayingViewPager.initialize(cookBeanList).build();
                        mAutoPlayingViewPager.setOnPageItemClickListener(onPageItemClickListener);
                        mAutoPlayingViewPager.startPlaying();
                    }
                }
            }
        }
    }

    private class MyAsyncTask extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... params) {
            //模拟网络请求获取数据
            cookBeanList = new ArrayList<>();
            try {
                for (int i = 0; i < 3; i++) {
                    GetJsonUtils.GetDataBySearchNameId(handlerSearch, "" + numList.get(i), String.valueOf(i + 1));
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            //数据加载后更新UI
            /*if (cookBeanList != null && cookBeanList.size() ==3) {
                mAutoPlayingViewPager.initialize(cookBeanList).build();
                mAutoPlayingViewPager.setOnPageItemClickListener(onPageItemClickListener);
                mAutoPlayingViewPager.startPlaying();
            }*/
        }
    }

}


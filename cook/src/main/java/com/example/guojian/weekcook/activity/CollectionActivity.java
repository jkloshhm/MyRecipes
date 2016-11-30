package com.example.guojian.weekcook.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import com.example.guojian.weekcook.R;
import com.example.guojian.weekcook.adapter.CookListAdapter;
import com.example.guojian.weekcook.bean.CookBean;
import com.example.guojian.weekcook.dao.DBServices;
import com.example.guojian.weekcook.dao.MyDBServiceUtils;

import java.util.ArrayList;

public class CollectionActivity extends AppCompatActivity {

    private static DBServices db;
    private static ArrayList<CookBean> cookBeanlist;
    private static CookBean cookBean;
    //private ArrayList<String> array = new ArrayList<String>();
    private CookListAdapter adapter;
    private ListView lv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.i("jkloshhm", "CollectionActivity____________onCreate()");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_collection);
        lv = (ListView) findViewById(R.id.lv_my_collection_list);
        final LinearLayout mBackLinearLayout = (LinearLayout) findViewById(R.id.ll_back_to_my_home);
        if (mBackLinearLayout != null) {
            mBackLinearLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    finish();
                }
            });
        }
        initDB();

        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                cookBean = cookBeanlist.get(position);
                try {
                    Intent intent = new Intent(CollectionActivity.this, DetailsActivity.class);
                    Bundle b = new Bundle();
                    b.putSerializable("cookBean01", cookBean);
                    intent.putExtras(b);
                    startActivity(intent);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        lv.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {
                cookBean = cookBeanlist.get(position);
                MyDBServiceUtils.delectData(cookBean, db);
                Toast.makeText(CollectionActivity.this, "取消收藏", Toast.LENGTH_SHORT).show();
                return true;
            }
        });
    }

    private void initDB() {
        db = MyDBServiceUtils.getInstance(this);
        cookBeanlist = MyDBServiceUtils.getAllObject(db);
        Log.i("jkloshhm", "CollectionActivity____________cookBeanlist.size()"+cookBeanlist.size());
        /*for (int i = 0; i < cookBeanlist.size(); i++) {
            String object = cookBeanlist.get(i).getName_cook();
            this.array.add(object);
        }*/
        adapter = new CookListAdapter(this,cookBeanlist);
        lv.setAdapter(adapter);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        db.close();
    }

    @Override
    protected void onPause() {
        Log.i("jkloshhm", "CollectionActivity____________onPause()");
        super.onPause();
    }

    @Override
    protected void onResume() {
        //initDB();
        Log.i("jkloshhm", "CollectionActivity____________onResume()");
        super.onResume();
        //initDB();
        adapter.notifyDataSetChanged();
    }

    @Override
    protected void onRestart() {
        //initDB();
        //adapter.notifyDataSetChanged();
        Log.i("jkloshhm", "CollectionActivity____________onRestart()");
        super.onRestart();
    }

    @Override
    protected void onStart() {
        Log.i("jkloshhm", "CollectionActivity____________onStart()");
        super.onStart();
    }

    @Override
    protected void onStop() {
        Log.i("jkloshhm", "CollectionActivity____________onStop()");
        super.onStop();
    }
}

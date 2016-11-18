package com.example.guojian.weekcook.activity;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import com.example.guojian.weekcook.R;
import com.example.guojian.weekcook.bean.CookBean;
import com.example.guojian.weekcook.dao.CookBeanService;
import com.example.guojian.weekcook.dao.DBServices;

import java.io.ByteArrayInputStream;
import java.io.ObjectInputStream;
import java.util.ArrayList;

public class CollectionActivity extends AppCompatActivity {

    private static Context context;
    private static DBServices db;
    private static ArrayList<CookBean> cookBeanlist;
    ArrayList<String> array = new ArrayList<String>();
    private CookBeanService mService;
    private ArrayAdapter<String> adapter;
    /*    final Handler handler_collection = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                if (msg.what == 0x123) {
                    adapter.notifyDataSetChanged();
                }
            }
        };*/
    private ListView lv;

    private static void delectData(int position) {

        SQLiteDatabase database = db.getReadableDatabase();
        String cursor_id = cookBeanlist.get(position).getTag();
        db.delete("Test", "_id like ?", new String[]{cursor_id});
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_collection);
        context = this.getApplicationContext();
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

        db = new DBServices(this);
        initDB();
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                CookBean cookBean = cookBeanlist.get(position);
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

                delectData(position);
                Toast.makeText(CollectionActivity.this, "取消收藏", Toast.LENGTH_SHORT).show();
                //Message message = new Message();
                //handler_collection.sendEmptyMessage(0x123);
                //refresh();
                //lv.deferNotifyDataSetChanged();
                //adapter.notifyDataSetChanged();
                return true;
            }
        });
    }

    public void refresh() {
        onCreate(null);
    }

    private void initDB() {
        cookBeanlist = this.getAllObject();
        for (int i = 0; i < cookBeanlist.size(); i++) {
            String object = cookBeanlist.get(i).getName_cook() + " - ";
            this.array.add(object);
        }
        adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_expandable_list_item_1, array);
        lv.setAdapter(adapter);
    }

    public ArrayList<CookBean> getAllObject() {
        ArrayList<CookBean> cookBeanList = new ArrayList<CookBean>();
        SQLiteDatabase database = db.getReadableDatabase();
        Cursor cursor = database.rawQuery("select * from Test", null);
        if (cursor != null) {
            while (cursor.moveToNext()) {
                Log.i("data-id", "data-id=====" + cursor.getString(0));
                String cursorString = cursor.getString(0);
                byte data[] = cursor.getBlob(cursor.getColumnIndex("person"));
                ByteArrayInputStream arrayInputStream = new ByteArrayInputStream(data);
                try {
                    ObjectInputStream inputStream = new ObjectInputStream(arrayInputStream);
                    CookBean cookBean = (CookBean) inputStream.readObject();
                    cookBean.setTag(cursorString);
                    cookBeanList.add(cookBean);
                    inputStream.close();
                    arrayInputStream.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        Log.d("guojian_Persons-Count", Integer.toString(cookBeanList.size()));
        return cookBeanList;
    }

}

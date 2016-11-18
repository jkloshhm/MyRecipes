package com.example.guojian.weekcook.dao;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.guojian.weekcook.bean.CookBean;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

/**
 * Created by guojian on 11/17/16.
 */
public class CookBeanService {

    Context context;

    public CookBeanService(Context context) {
        // TODO Auto-generated constructor stub
        this.context = context;
    }

    /**
     * 保存
     * @param cookBean
     */
    public  void saveObject(CookBean cookBean) {
        ByteArrayOutputStream arrayOutputStream = new ByteArrayOutputStream();
        try {
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(arrayOutputStream);
            objectOutputStream.writeObject(cookBean);
            objectOutputStream.flush();
            byte data[] = arrayOutputStream.toByteArray();
            objectOutputStream.close();
            arrayOutputStream.close();
            MyDbHelper dbhelper = MyDbHelper.getInstens(context);
            SQLiteDatabase database = dbhelper.getWritableDatabase();
            database.execSQL("insert into classtable (classtabledata) values(?)", new Object[] { data });
            database.close();
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    /**
     * 读取
     * @return
     */
    public CookBean getObject() {
        CookBean cookBean = null;
        MyDbHelper dbhelper = MyDbHelper.getInstens(context);
        SQLiteDatabase database = dbhelper.getReadableDatabase();
        Cursor cursor = database.rawQuery("select * from classtable", null);
        if (cursor != null) {
            while (cursor.moveToNext()) {
                byte data[] = cursor.getBlob(cursor.getColumnIndex("classtabledata"));
                ByteArrayInputStream arrayInputStream = new ByteArrayInputStream(data);
                try {
                    ObjectInputStream inputStream = new ObjectInputStream(arrayInputStream);
                    cookBean = (CookBean) inputStream.readObject();
                    inputStream.close();
                    arrayInputStream.close();
                    break;//这里为了测试就取一个数据
                } catch (Exception e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        }
        return cookBean;

    }

}

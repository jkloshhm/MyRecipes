package com.example.guojian.weekcook.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.widget.LinearLayout;
import android.widget.SearchView;
import android.widget.Toast;

import com.example.guojian.weekcook.R;

public class SearchActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        LinearLayout mBack = (LinearLayout) findViewById(R.id.ll_search_back);
        LinearLayout mSearch = (LinearLayout) findViewById(R.id.ll_search_content);
        SearchView mSearchView = (SearchView) findViewById(R.id.search_view_main);
        if (null != mSearchView) {
            mSearchView.setIconifiedByDefault(false);
            mSearchView.setSubmitButtonEnabled(true);
            mSearchView.setQueryHint("请输入正确的菜名");
            mSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                @Override
                public boolean onQueryTextSubmit(String query) {
                    if (TextUtils.isEmpty(query.trim())) {
                        Toast.makeText(SearchActivity.this, "请输入正确的菜名", Toast.LENGTH_SHORT).show();
                    } else {
                        //setEditTextInhibitInputSpeChat(mSearchName);
                        Intent mIntent = new Intent(SearchActivity.this, CookListActivity.class);
                        mIntent.putExtra("CookType", "GetDataBySearchName");
                        mIntent.putExtra("name", query.replace(" ", ""));
                        startActivity(mIntent);
                    }
                    return true;
                }

                @Override
                public boolean onQueryTextChange(String newText) {
                    return false;
                }
            });
        }
        /*if (mSearch != null) {
            mSearch.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (null != searchView) {
                        if (TextUtils.isEmpty(searchView.getContext().toString().trim())) {
                            Toast.makeText(SearchActivity.this, "请输入正确的菜名", Toast.LENGTH_SHORT).show();
                        } else {
                            //setEditTextInhibitInputSpeChat(mSearchName);
                            Intent mIntent = new Intent(SearchActivity.this, CookListActivity.class);
                            mIntent.putExtra("CookType", "GetDataBySearchName");
                            mIntent.putExtra("name", searchView.getContext().toString().replace(" ", ""));
                            startActivity(mIntent);
                        }
                    }

                }
            });
        }*/
    }
}

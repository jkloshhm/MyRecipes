package com.example.guojian.weekcook.fragment;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.InputFilter;
import android.text.Spanned;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.guojian.weekcook.R;
import com.example.guojian.weekcook.activity.CookListActivity;

import java.util.regex.Matcher;
import java.util.regex.Pattern;


/**
 * A simple {@link Fragment} subclass.
 */
public class SearchFragment extends Fragment {

    private Context mContext;

    public SearchFragment() {
        // Required empty public constructor
    }


    /**
     * 禁止EditText输入特殊字符
     *
     * @param editText
     */
    public static void setEditTextInhibitInputSpeChat(EditText editText) {

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
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mContext = getActivity();
        View view = inflater.inflate(R.layout.fragment_search, container, false);
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
        return view;
    }


}

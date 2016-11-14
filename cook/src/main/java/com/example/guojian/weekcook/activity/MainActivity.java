package com.example.guojian.weekcook.activity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.alibaba.apigateway.client.ApiGatewayClient;
import com.example.guojian.weekcook.R;
import com.example.guojian.weekcook.adapter.MyFragmentPagerAdapter;
import com.example.guojian.weekcook.fragment.SearchFragment;
import com.example.guojian.weekcook.fragment.ClassFragment;
import com.example.guojian.weekcook.fragment.MeFragment;

import java.util.ArrayList;
import java.util.List;


public class MainActivity extends FragmentActivity {
    private ViewPager viewPager;
    private RadioGroup radioGroup;
    private RadioButton rbSearch, rbClass, rbDiscovery, rbMe;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initGatewaySdk();
        initView();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    private void initView() {
        /**
         * RadioGroup部分
         */
        radioGroup = (RadioGroup) findViewById(R.id.radioGroup);
        rbSearch = (RadioButton) findViewById(R.id.rb_search);
        rbClass = (RadioButton) findViewById(R.id.rb_class);
        //rbDiscovery = (RadioButton) findViewById(R.id.rb_discovery);
        rbMe = (RadioButton) findViewById(R.id.rb_me);
        //RadioGroup选中状态改变监听
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.rb_search:
                        /**
                         * setCurrentItem第二个参数控制页面切换动画
                         * true:打开/false:关闭
                         */
                        viewPager.setCurrentItem(0, true);
                        break;
                    case R.id.rb_class:
                        viewPager.setCurrentItem(1, true);
                        break;
                    /*case R.id.rb_discovery:
                        viewPager.setCurrentItem(2, true);
                        break;*/
                    case R.id.rb_me:
                        viewPager.setCurrentItem(2, true);
                        break;
                }
            }
        });

        /**
         * ViewPager部分
         */
        viewPager = (ViewPager) findViewById(R.id.viewPager);
        SearchFragment weSearchFragment = new SearchFragment();
        ClassFragment classFragment = new ClassFragment();
        //DiscoveryFragment discoveryFragment = new DiscoveryFragment();
        MeFragment meFragment = new MeFragment();
        List<Fragment> alFragment = new ArrayList<Fragment>();
        alFragment.add(weSearchFragment);
        alFragment.add(classFragment);
        //alFragment.add(discoveryFragment);
        alFragment.add(meFragment);
        //ViewPager设置适配器
        viewPager.setAdapter(new MyFragmentPagerAdapter(getSupportFragmentManager(), alFragment));
        //ViewPager显示第一个Fragment
        viewPager.setCurrentItem(0);
        //ViewPager页面切换监听
        viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                switch (position) {
                    case 0:
                        //radioGroup.check(R.id.rb_chat);
                        rbSearch.setChecked(true);
                        break;
                    case 1:
                        //radioGroup.check(R.id.rb_contacts);
                        rbClass.setChecked(true);
                        break;
                    /*case 2:
                        //radioGroup.check(R.id.rb_discovery);
                        rbDiscovery.setChecked(true);
                        break;*/
                    case 2:
                        //radioGroup.check(R.id.rb_me);
                        rbMe.setChecked(true);
                        break;
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    private void initGatewaySdk() {
        // 初始化API网关
        ApiGatewayClient.init(getApplicationContext(), false);
    }
}

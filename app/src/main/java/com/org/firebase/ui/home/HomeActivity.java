package com.org.firebase.ui.home;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;
import com.org.firebase.R;
import com.org.firebase.base.BaseActivity;

import butterknife.BindView;
import butterknife.ButterKnife;

public class HomeActivity extends BaseActivity implements TabLayout.BaseOnTabSelectedListener {

    @BindView(R.id.tabLayout)
    TabLayout tabLayout;
    @BindView(R.id.vphome)
    ViewPager vphome;
    private HomeAdapter homeAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        ButterKnife.bind(this);


        tabLayout.addTab(tabLayout.newTab().setText("Top"));
        tabLayout.addTab(tabLayout.newTab().setText("Post"));
        tabLayout.addTab(tabLayout.newTab().setText("Setting"));
        homeAdapter = new HomeAdapter(this.getSupportFragmentManager());
        vphome.setAdapter(homeAdapter);
        vphome.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.addOnTabSelectedListener(this);

    }


    @Override
    public void onTabSelected(TabLayout.Tab tab) {
        homeAdapter = new HomeAdapter(this.getSupportFragmentManager());
        vphome.setAdapter(homeAdapter);
        vphome.setCurrentItem(tab.getPosition());
    }

    @Override
    public void onTabUnselected(TabLayout.Tab tab) {

    }

    @Override
    public void onTabReselected(TabLayout.Tab tab) {

    }
}

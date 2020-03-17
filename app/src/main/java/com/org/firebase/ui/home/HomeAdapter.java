package com.org.firebase.ui.home;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;
import com.org.firebase.ui.post.PostFragment;
import com.org.firebase.ui.setting.SettingFragment;
import com.org.firebase.ui.top.TopFragment;

public class HomeAdapter extends FragmentStatePagerAdapter {

    public HomeAdapter(@NonNull FragmentManager fm) {
        super(fm);
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {

        if (position == 0) {
            TopFragment topFragment = new TopFragment();
            return topFragment;
        } else if (position == 1) {
            PostFragment postFragment = new PostFragment();
            return postFragment;
        } else {
            SettingFragment settingFragment = new SettingFragment();
            return settingFragment;
        }
    }

    @Override
    public int getCount() {
        return 3;
    }

}

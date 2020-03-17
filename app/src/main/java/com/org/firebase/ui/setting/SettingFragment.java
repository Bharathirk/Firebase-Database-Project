package com.org.firebase.ui.setting;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.firebase.auth.FirebaseAuth;
import com.org.firebase.R;
import com.org.firebase.app.AppController;
import com.org.firebase.app.AppPreference;
import com.org.firebase.base.BaseFragment;
import com.org.firebase.data.navigator.Navigator;
import com.org.firebase.ui.login.LoginActivity;

import butterknife.ButterKnife;
import butterknife.OnClick;

public class SettingFragment extends BaseFragment {

    FirebaseAuth auth;
    private AppPreference preference;
    private Navigator navigator;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_setting, container, false);
        ButterKnife.bind(this, view);
        auth = FirebaseAuth.getInstance();
        navigator=AppController.getInstance().getNavigator();
        preference = AppController.getInstance().getAppPreference();


        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);
    }

    @OnClick({R.id.bt_logout})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bt_logout:

                auth.signOut();
                preference.clearPreferences();
                navigator.navigateToSignActivity(getContext());
                getActivity().finish();
                break;

        }
    }
}

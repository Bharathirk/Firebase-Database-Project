package com.org.firebase.data.navigator;


import android.content.Context;
import android.content.Intent;

import com.org.firebase.ui.home.HomeActivity;
import com.org.firebase.ui.login.LoginActivity;
import com.org.firebase.ui.signup.SignUpActivity;

public class Navigator {
    public void navigateToHomeActivity(Context context) {
        context.startActivity(new Intent(context, HomeActivity.class));
    }

    public void navigateToSignUpActivity(Context context) {
        context.startActivity(new Intent(context, SignUpActivity.class));
    }

    public void navigateToSignActivity(Context context) {
        context.startActivity(new Intent(context, LoginActivity.class));
    }


}

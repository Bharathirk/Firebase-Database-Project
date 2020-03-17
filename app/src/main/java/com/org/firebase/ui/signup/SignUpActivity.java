package com.org.firebase.ui.signup;

import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.org.firebase.R;
import com.org.firebase.app.AppController;
import com.org.firebase.app.AppPreference;
import com.org.firebase.base.BaseActivity;
import com.org.firebase.data.navigator.Navigator;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class SignUpActivity extends BaseActivity {
    FirebaseAuth auth;
    @BindView(R.id.edt_username)
    TextInputEditText edtUsername;
    @BindView(R.id.edt_password)
    TextInputEditText edtPassword;
    @BindView(R.id.edt_sign_mobile_number)
    TextInputEditText edtSignMobileNumber;
    @BindView(R.id.bt_signin)
    Button btSignin;
    private Navigator navigator;
    private AppPreference preference;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        ButterKnife.bind(this);
        navigator= AppController.getInstance().getNavigator();
        preference= AppController.getInstance().getAppPreference();
        auth = FirebaseAuth.getInstance();

    }

    @OnClick(R.id.bt_signin)
    public void onViewClickedd() {
        navigator.navigateToSignActivity(SignUpActivity.this);
        finish();
    }

    @OnClick(R.id.floatingActionButton)
    public void onViewClicked() {

        String email = edtUsername.getText().toString().trim();
        String password = edtPassword.getText().toString().trim();

        if (TextUtils.isEmpty(email)) {
            Toast.makeText(getApplicationContext(), "Enter email address!", Toast.LENGTH_SHORT).show();
            return;
        }

        if (TextUtils.isEmpty(password)) {
            Toast.makeText(getApplicationContext(), "Enter password!", Toast.LENGTH_SHORT).show();
            return;
        }

        if (password.length() < 6) {
            Toast.makeText(getApplicationContext(), "Password too short, enter minimum 6 characters!", Toast.LENGTH_SHORT).show();
            return;
        }


        auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(SignUpActivity.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        Toast.makeText(SignUpActivity.this, "createUserWithEmail:onComplete:" + task.isSuccessful(), Toast.LENGTH_SHORT).show();

                        if (!task.isSuccessful()) {
                            Toast.makeText(SignUpActivity.this, "Authentication failed." + task.getException(),
                                    Toast.LENGTH_SHORT).show();
                        } else {

                            preference.putUserId(task.getResult().getUser().getUid());
                            preference.putUserName(task.getResult().getUser().getDisplayName());

                            navigator.navigateToHomeActivity(SignUpActivity.this);
                            finish();
                        }
                    }
                });
    }
}

package com.org.firebase.ui.login;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
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
import com.org.firebase.ui.signup.SignUpActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class LoginActivity extends BaseActivity {
    @BindView(R.id.et_name)
    TextInputEditText etName;
    @BindView(R.id.et_pass)
    TextInputEditText etPass;
    private Navigator navigator;
    private FirebaseAuth auth;
    private AppPreference preference;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
        navigator = AppController.getInstance().getNavigator();
        auth = FirebaseAuth.getInstance();
        preference= AppController.getInstance().getAppPreference();

    }
    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = auth.getCurrentUser();
        if (currentUser!=null){
            Toast.makeText(this, "welcome :"+currentUser.getEmail().toString(), Toast.LENGTH_SHORT).show();
            navigator.navigateToHomeActivity(LoginActivity.this);
            finish();
        }
    }


    @OnClick({R.id.tv_signup, R.id.floatingActionButton})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_signup:
                navigator.navigateToSignUpActivity(this);
                finish();
                break;
            case R.id.floatingActionButton:
                String email = etName.getText().toString();
                final String password = etPass.getText().toString();

                if (TextUtils.isEmpty(email)) {
                    Toast.makeText(getApplicationContext(), "Enter email address!", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (TextUtils.isEmpty(password)) {
                    Toast.makeText(getApplicationContext(), "Enter password!", Toast.LENGTH_SHORT).show();
                    return;
                }
                auth.signInWithEmailAndPassword(email, password)
                        .addOnCompleteListener(LoginActivity.this, new OnCompleteListener<AuthResult>() {
                                    @Override
                                    public void onComplete(@NonNull Task<AuthResult> task) {

                                        if (!task.isSuccessful()) {

                                            if (password.length() < 6) {
                                                etPass.setError(getString(R.string.minimum_password));
                                            } else {
                                                Toast.makeText(LoginActivity.this, getString(R.string.auth_failed), Toast.LENGTH_LONG).show();
                                            }
                                        } else {
                                            FirebaseUser firebaseUser=task.getResult().getUser();
                                            String userName=firebaseUser.getDisplayName()!=null?firebaseUser.getDisplayName():
                                                    firebaseUser.getEmail()!=null?firebaseUser.getEmail().split("@")[0]:"";
                                            preference.putUserId(firebaseUser.getUid());
                                            preference.putUserName(userName);
                                            navigator.navigateToHomeActivity(LoginActivity.this);
                                            finish();
                                        }
                                    }
                                });
                break;
        }
    }
}

package com.ba.styleme.ui.activities.user_registration.signin;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;

import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;

import com.ba.styleme.R;
import com.ba.styleme.data.network.ApiConstant;
import com.ba.styleme.ui.activities.base.BaseActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class SigninActivity extends BaseActivity implements View.OnClickListener {
    EditText username, password;
    TextInputLayout til_username, til_password;

    ProgressBar loadingProgressBar;

    Button login;

    public void findViews() {
        username = findViewById(R.id.username);
        password = findViewById(R.id.password);

        til_username = findViewById(R.id.til_username);
        til_password = findViewById(R.id.til_password);

        loadingProgressBar = findViewById(R.id.loading);

        login = findViewById(R.id.login);
        login.setOnClickListener(this);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signin);

        findViews();
        loadingProgressBar.setVisibility(View.GONE);

    }

    public boolean validateData() {

        if (username.getText().toString().trim().isEmpty()) {
            til_username.setError(getResourceString(R.string.this_field_should_not_empty));
            return false;
        } else {
            til_username.setErrorEnabled(false);
        }

        if (password.getText().toString().trim().isEmpty()) {
            til_password.setError(getResourceString(R.string.this_field_should_not_empty));
            return false;
        } else {
            til_password.setErrorEnabled(false);
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(username.getText().toString().trim()).matches()) {
            til_username.setError(getResourceString(R.string.invalid));
            return false;
        } else {
            til_username.setErrorEnabled(false);
        }

        if (!(isValidPassword(password.getText().toString().trim()))) {
            til_password.setError(getResourceString(R.string.password_regex_message));
            return false;
        } else {
            til_password.setErrorEnabled(false);
        }


        return true;
    }


    @Override
    public void onClick(final View view) {
        if (validateData()) {

            showLoading();

            mAuth.signInWithEmailAndPassword(username.getText().toString().trim(), password.getText().toString().trim())
                    .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {

                            if (task.isSuccessful()) {
                                if(username.getText().toString().trim().equals(ApiConstant.adminEmail)){

                                    hideLoading();
                                    appPreference.setEmailAndPassword(username.getText().toString().trim(),password.getText().toString().trim());
                                    appPreference.setUserId(mAuth.getCurrentUser().getUid() + "");
                                    appPreference.setIsLogin(true);
                                    openAdminRecomendationActivity(null);
                                }else {
                                    checkIfEmailVerified(view);
                                }
                            } else {
                                hideLoading();
                                try {
                                    throw task.getException();
                                } catch (Exception ex) {
                                   showInfoToast(ex.getMessage());
                                    // neksuApp.showDialog(context, ex.getMessage());
                                }
                            }

                        }
                    });
        }

    }

    private void checkIfEmailVerified(View view) {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        if (user.isEmailVerified()) {

            hideLoading();
            appPreference.setEmailAndPassword(username.getText().toString().trim(),password.getText().toString().trim());
            appPreference.setUserId(mAuth.getCurrentUser().getUid() + "");
            navigateToNextScreen(view);

        } else {
            hideLoading();
            showInfoToast("Email not confirmed. Please check your email address to continue");
            // email is not verified, so just prompt the message to the user and restart this activity.
            // NOTE: don't forget to log out the user.
            FirebaseAuth.getInstance().signOut();
            //restart this activity
        }
    }

    public void navigateToNextScreen(View view) {
        appPreference.setIsLogin(true);
        openHomeActivity(view);
    }
}

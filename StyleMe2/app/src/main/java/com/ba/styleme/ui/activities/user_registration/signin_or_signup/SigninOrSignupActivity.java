package com.ba.styleme.ui.activities.user_registration.signin_or_signup;

import android.os.Bundle;
import android.view.View;

import com.ba.styleme.R;
import com.ba.styleme.ui.activities.base.BaseActivity;

public class SigninOrSignupActivity extends BaseActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_singin_or_signup);

    }

    public void openSigninActivity(View view){
        super.openSigninActivity(view);
        this.finish();
    }

}

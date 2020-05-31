package com.ba.styleme.ui.activities.splash;

import android.os.Bundle;

import com.ba.styleme.data.network.ApiConstant;
import com.ba.styleme.ui.activities.base.BaseActivity;

public class SplashActivity extends BaseActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        new java.util.Timer().schedule(
                new java.util.TimerTask() {
                    @Override
                    public void run() {
                        if(appPreference.getIsLogin()){
                            if(appPreference.getEmail().equals(ApiConstant.adminEmail)){
                                openAdminRecomendationActivity(null);
                            }else{
                                openHomeActivity(null);
                            }
                        }else {
                            openSigninOrSignupActivity(null);
                        }
                    }
                },
                500
        );

    }


}

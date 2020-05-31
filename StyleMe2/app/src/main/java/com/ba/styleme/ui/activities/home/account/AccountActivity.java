package com.ba.styleme.ui.activities.home.account;

import android.os.Bundle;
import android.view.View;

import com.ba.styleme.R;
import com.ba.styleme.ui.activities.base.BaseActivity;

public class AccountActivity extends BaseActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account);
        setTitle(getResourceString(R.string.account));
        setSlidingPane();

    }




}

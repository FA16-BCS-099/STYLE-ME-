package com.ba.styleme.ui.activities.home.account.update_password;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;

import com.ba.styleme.R;
import com.ba.styleme.ui.activities.base.BaseActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;

public class UpdatePasswordActivity extends BaseActivity implements View.OnClickListener {

    EditText old_password, password, retype_password;
    TextInputLayout til_old_password, til_password, til_retype_password;

    Button update;

    public void findViews() {
        old_password = findViewById(R.id.old_password);
        password = findViewById(R.id.password);
        retype_password = findViewById(R.id.retype_password);
        til_old_password = findViewById(R.id.til_old_password);
        til_password = findViewById(R.id.til_password);
        til_retype_password = findViewById(R.id.til_retype_password);

        update = findViewById(R.id.update);
        update.setOnClickListener(this);
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_password);
        setTitle(getResourceString(R.string.update_password));

        findViews();

    }


    @Override
    public void onClick(View view) {

        if(validateData()){
            showLoading();
            mAuth.getCurrentUser().updatePassword(password.getText().toString().trim()).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    hideLoading();
                    if(task.isSuccessful()){
                        showInfoToast("Updated successfully.");

                        appPreference.setEmailAndPassword(appPreference.getEmail(),password.getText().toString().trim());
                        appPreference.setUserId(mAuth.getUid() + "");
                        finish();
                    }else{
                        showInfoToast(task.getException().getMessage());
                        Log.e("exception",task.getException().getMessage());
                    }
                }
            });
        }
    }

    public boolean validateData() {
        if (old_password.getText().toString().trim().isEmpty()) {
            til_old_password.setError(getResourceString(R.string.this_field_should_not_empty));
            return false;
        }else{
            til_old_password.setErrorEnabled(false);
        }
        if (password.getText().toString().trim().isEmpty()) {
            til_password.setError(getResourceString(R.string.this_field_should_not_empty));
            return false;
        }else{
            til_password.setErrorEnabled(false);
        }
        if (retype_password.getText().toString().trim().isEmpty()) {
            til_retype_password.setError(getResourceString(R.string.this_field_should_not_empty));
            return false;
        }else{
            til_retype_password.setErrorEnabled(false);
        }


        if (!(isValidPassword(password.getText().toString().trim()))) {
            til_password.setError(getResourceString(R.string.password_regex_message));
            return false;
        }else{
            til_password.setErrorEnabled(false);
        }

        if (!(isValidPassword(retype_password.getText().toString().trim()))) {
            til_retype_password.setError(getResourceString(R.string.password_regex_message));
            return false;
        }else{
            til_retype_password.setErrorEnabled(false);
        }


        if (!(password.getText().toString().trim().equals(retype_password.getText().toString().trim()))) {
            til_password.setError(getResourceString(R.string.password_mismatch_message));
            til_retype_password.setError(getResourceString(R.string.password_mismatch_message));
            return false;
        }else{
            til_password.setErrorEnabled(false);
            til_retype_password.setErrorEnabled(false);
        }

        if (appPreference.getPassword().equals(old_password.getText().toString().trim())) {
            til_old_password.setError("Old password is incorrect!");
            return false;
        } else {
            til_old_password.setErrorEnabled(false);
        }
        return true;
    }
}

package com.ba.styleme.ui.activities.user_registration.signin.forgot_password;

import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;

import com.ba.styleme.R;
import com.ba.styleme.ui.activities.base.BaseActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;

public class ForgotPasswordActivity extends BaseActivity implements View.OnClickListener {
    EditText username;
    TextInputLayout til_username;

    Button submit;

    public void findViews(){
        username = findViewById(R.id.username);

        til_username = findViewById(R.id.til_username);

        submit = findViewById(R.id.submit);
        submit.setOnClickListener(this);

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);

        setTitle(getResourceString(R.string.forgot_password));
        findViews();
    }


    @Override
    public void onClick(View view) {

        if(validateData()){
            showLoading();
            mAuth.sendPasswordResetEmail(username.getText().toString())
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            hideLoading();
                            if (task.isSuccessful()) {
                                Log.d("fcm", "Email sent.");
                                showInfoToast("Please check your emails.");
                                finish();
                            } else {
                                // If sign in fails, display a message to the user.
                                try {
                                    throw task.getException();
                                } catch (FirebaseAuthInvalidCredentialsException | FirebaseAuthUserCollisionException ex) {
                                    username.requestFocus();
                                    showInfoToast(ex.getMessage());
                                } catch (Exception ex) {
                                    showInfoToast(ex.getMessage());
                                }

                            }

                        }
                    });

        }
    }

    public boolean validateData() {

        if (username.getText().toString().trim().isEmpty()) {
            til_username.setError(getResourceString(R.string.this_field_should_not_empty));
            return false;
        }else{
            til_username.setErrorEnabled(false);
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(username.getText().toString().trim()).matches()) {
            til_username.setError(getResourceString(R.string.invalid));
            return false;
        } else {
            til_username.setErrorEnabled(false);
        }

        return true;
    }

}

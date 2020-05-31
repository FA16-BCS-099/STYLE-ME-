package com.ba.styleme.ui.activities.user_registration.signup;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.ba.styleme.R;
import com.ba.styleme.ui.activities.base.BaseActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class SignupActivity extends BaseActivity implements View.OnClickListener {
    EditText user_name, user_email, user_dob, user_contactno, password, retype_password;
    TextInputLayout til_user_name, til_user_email, til_user_dob, til_user_contactno, til_password, til_retype_password;
    RadioGroup rg_gender;
    RadioButton radio_male, radio_female;
    Button signup;

    Calendar calendar = Calendar.getInstance();

    public void findViews() {

        user_name = findViewById(R.id.user_name);
        user_email = findViewById(R.id.user_email);
        user_dob = findViewById(R.id.user_dob);
        user_contactno = findViewById(R.id.user_contactno);
        password = findViewById(R.id.password);
        retype_password = findViewById(R.id.retype_password);

        til_user_name = findViewById(R.id.til_user_name);
        til_user_email = findViewById(R.id.til_user_email);
        til_user_dob = findViewById(R.id.til_user_dob);
        til_user_contactno = findViewById(R.id.til_user_contactno);
        til_password = findViewById(R.id.til_password);
        til_retype_password = findViewById(R.id.til_retype_password);

        rg_gender = findViewById(R.id.rg_gender);
        radio_male = findViewById(R.id.radio_male);
        radio_female = findViewById(R.id.radio_female);

        signup = findViewById(R.id.signup);
        signup.setOnClickListener(this);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        setTitle(getResourceString(R.string.action_sign_up));

        findViews();
    }


    @Override
    public void onClick(final View view) {
        if (validateData()) {
            showLoading();

            mAuth.createUserWithEmailAndPassword(user_email.getText().toString(), password.getText().toString())
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {

                            if (task.isSuccessful()) {

                                // Create a new user with a first and last name
                                Map<String, Object> user = new HashMap<>();
                                // Access a Cloud Firestore instance from your Activity
                                
                                user.put("name", user_name.getText().toString().trim());
                                user.put("email", user_email.getText().toString().trim());
                                user.put("dob", user_dob.getText().toString().trim());
                                user.put("contact_no", user_contactno.getText().toString().trim());
                                user.put("gender_is_male", radio_male.isChecked());
                                user.put("is_old_user",false);
                                appPreference.setUserId(mAuth.getUid() + "");

                                CollectionReference users = db.collection("users");
                                users.document(appPreference.getUserId()).set(user)
                                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                   @Override
                                                                   public void onComplete(@NonNull Task<Void> task) {
                                                                       if (task.isSuccessful()) {
                                                                           sendVerificationEmail(view);

                                                                       } else {
                                                                           //      neksuApp.showDialog(context, task.getException().toString());
                                                                           Log.w("fcm", "Error adding document", task.getException());
                                                                           showInfoToast( "Cannot connect to the server. Please try again later.");
                                                                           hideLoading();
                                                                       }

                                                                   }
                                                               }
                                        );


                            } else {
                                hideLoading();
                                try {
                                    throw task.getException();
                                } catch (FirebaseAuthWeakPasswordException ex) {
                                    password.requestFocus();
                                    showInfoToast(ex.getMessage());
                                } catch (FirebaseAuthInvalidCredentialsException | FirebaseAuthUserCollisionException ex) {
                                    user_email.requestFocus();
                                    showInfoToast(ex.getMessage());
                                } catch (Exception ex) {
                                    showInfoToast(ex.getMessage());
                                }

                            }
                        }
                    });
        }
    }

    private void sendVerificationEmail(final View view) {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        user.sendEmailVerification()
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        hideLoading();
                        if (task.isSuccessful()) {
                            showInfoToast("Please check your email inbox and confirm the registration.");
                            FirebaseAuth.getInstance().signOut();
                            navigateToNextScreen(view);

                        } else {
                            overridePendingTransition(0, 0);
                            finish();
                            overridePendingTransition(0, 0);
                            startActivity(getIntent());
                        }
                    }
                });
    }


    public void navigateToNextScreen(View view) {
        this.finish();
    }

    public boolean validateData() {
        if (user_name.getText().toString().trim().isEmpty()) {
            til_user_name.setError(getResourceString(R.string.this_field_should_not_empty));
            return false;
        } else {
            til_user_name.setErrorEnabled(false);
        }
        if (user_email.getText().toString().trim().isEmpty()) {
            til_user_email.setError(getResourceString(R.string.this_field_should_not_empty));
            return false;
        } else {
            til_user_email.setErrorEnabled(false);
        }

        if (user_dob.getText().toString().trim().isEmpty()) {
            til_user_dob.setError(getResourceString(R.string.this_field_should_not_empty));
            return false;
        } else {
            til_user_dob.setErrorEnabled(false);
        }
        if (user_contactno.getText().toString().trim().isEmpty()) {
            til_user_contactno.setError(getResourceString(R.string.this_field_should_not_empty));
            return false;
        } else {
            til_user_contactno.setErrorEnabled(false);
        }

        if (user_contactno.getText().toString().length()!=11) {
            til_user_contactno.setError("Should be 11 digits only.");
            return false;
        } else {
            til_user_contactno.setErrorEnabled(false);
        }

        if (password.getText().toString().isEmpty()) {
            til_password.setError(getResourceString(R.string.this_field_should_not_empty));
            return false;
        } else {
            til_password.setErrorEnabled(false);
        }
        if (retype_password.getText().toString().isEmpty()) {
            til_retype_password.setError(getResourceString(R.string.this_field_should_not_empty));
            return false;
        } else {
            til_retype_password.setErrorEnabled(false);
        }

        if (!(isValidPassword(password.getText().toString().trim()))) {
            til_password.setError(getResourceString(R.string.password_regex_message));
            return false;
        } else {
            til_password.setErrorEnabled(false);
        }

        if (!(isValidPassword(retype_password.getText().toString().trim()))) {
            til_retype_password.setError(getResourceString(R.string.password_regex_message));
            return false;
        } else {
            til_retype_password.setErrorEnabled(false);
        }

        if (!(password.getText().toString().trim().equals(retype_password.getText().toString().trim()))) {
            til_password.setError(getResourceString(R.string.password_mismatch_message));
            til_retype_password.setError(getResourceString(R.string.password_mismatch_message));
            return false;
        } else {
            til_password.setErrorEnabled(false);
            til_retype_password.setErrorEnabled(false);
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(user_email.getText().toString().trim()).matches()) {
            til_user_email.setError(getResourceString(R.string.invalid));
            return false;
        } else {
            til_user_email.setErrorEnabled(false);
        }

        if (rg_gender.getCheckedRadioButtonId() == -1) {
            Toast.makeText(this, "Please select gender.", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    public void openDatePickerDialog(View view) {
        final int mYear = calendar.get(Calendar.YEAR);
        final int mMonth = calendar.get(Calendar.MONTH);
        int mDay = calendar.get(Calendar.DAY_OF_MONTH);
        calendar.add(Calendar.YEAR, -18);
        // date picker dialog
        DatePickerDialog picker = new DatePickerDialog(this, AlertDialog.THEME_HOLO_DARK,
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int finalyear, int finalmonthOfYear, int finaldayOfMonth) {
                        calendar.set(finalyear, (finalmonthOfYear), finaldayOfMonth);
                        user_dob.setText(finaldayOfMonth + "/" + (finalmonthOfYear + 1) + "/" + finalyear);

                    }
                }, mYear, mMonth, mDay);

        picker.getDatePicker().setMaxDate(calendar.getTimeInMillis());
        picker.show();

    }

}

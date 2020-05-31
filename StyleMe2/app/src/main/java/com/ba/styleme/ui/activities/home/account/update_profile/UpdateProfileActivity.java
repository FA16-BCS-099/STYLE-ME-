package com.ba.styleme.ui.activities.home.account.update_profile;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.ba.styleme.R;
import com.ba.styleme.ui.activities.base.BaseActivity;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class UpdateProfileActivity extends BaseActivity implements View.OnClickListener {

    EditText user_name, user_email, user_dob, user_contactno;
    TextInputLayout til_user_name, til_user_email, til_user_dob, til_user_contactno;
    RadioGroup rg_gender;
    RadioButton radio_male, radio_female;
    ImageView img_right;
    Button update;
    boolean isEditable = false;

    Calendar calendar = Calendar.getInstance();


    public void findViews() {
        img_right = findViewById(R.id.img_right);
        user_name = findViewById(R.id.user_name);
        user_email = findViewById(R.id.user_email);
        user_dob = findViewById(R.id.user_dob);
        user_contactno = findViewById(R.id.user_contactno);
        til_user_name = findViewById(R.id.til_user_name);
        til_user_email = findViewById(R.id.til_user_email);
        til_user_dob = findViewById(R.id.til_user_dob);
        til_user_contactno = findViewById(R.id.til_user_contactno);

        rg_gender = findViewById(R.id.rg_gender);
        radio_male = findViewById(R.id.radio_male);
        radio_female = findViewById(R.id.radio_female);

        update = findViewById(R.id.update);
        update.setOnClickListener(this);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_profile);
        setTitle(getResourceString(R.string.update_profile));

        findViews();

        getUserData();

        img_right.setImageDrawable(getDrawable(R.drawable.ic_edit));
        disbaleFields();
        img_right.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!isEditable) {
                    isEditable = true;
                    img_right.setImageDrawable(getDrawable(R.drawable.ic_cancel));
                    enableFields();
                } else {
                    isEditable = false;
                    img_right.setImageDrawable(getDrawable(R.drawable.ic_edit));
                    disbaleFields();
                }
            }
        });

    }


    public void enableFields() {
        user_name.setEnabled(true);
        //  user_email.setEnabled(true);
        user_dob.setEnabled(true);
        user_contactno.setEnabled(true);
        radio_male.setEnabled(true);
        radio_female.setEnabled(true);
        update.setVisibility(View.VISIBLE);
    }

    public void disbaleFields() {
        user_name.setEnabled(false);
        user_email.setEnabled(false);
        user_dob.setEnabled(false);
        user_contactno.setEnabled(false);
        radio_male.setEnabled(false);
        radio_female.setEnabled(false);
        update.setVisibility(View.GONE);
    }


    @Override
    public void onClick(View view) {
        if (validateData()) {
            showLoading();
            // Create a new user with a first and last name
            Map<String, Object> user = new HashMap<>();
            // Access a Cloud Firestore instance from your Activity
            user.put("name", user_name.getText().toString().trim());
            user.put("email", user_email.getText().toString().trim());
            user.put("dob", user_dob.getText().toString().trim());
            user.put("contact_no", user_contactno.getText().toString().trim());
            user.put("gender_is_male", radio_male.isChecked());
            final CollectionReference users = db.collection("users");

            users.document(appPreference.getUserId()).set(user)
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                               @Override
                                               public void onComplete(@NonNull final Task<Void> task) {
                                                   hideLoading();
                                                   if (task.isSuccessful()) {
                                                       showInfoToast("Updated successfully.");
                                                       disbaleFields();
                                                   } else {
                                                       //      neksuApp.showDialog(context, task.getException().toString());
                                                       Log.w("fcm", "Error adding document", task.getException());
                                                       showInfoToast("Cannot connect to the server. Please try again later.");
                                                   }
                                               }
                                           }
                    );
        }

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


    public void getUserData() {
        showLoading();
        DocumentReference docRef = db.collection("users").document(appPreference.getUserId());
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                hideLoading();
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        Log.d("fcm", "DocumentSnapshot data: " + document.getData());

                        user_name.setText(document.getData().get("name").toString());
                        user_email.setText(document.getData().get("email").toString());
                        user_dob.setText(document.getData().get("dob").toString());
                        user_contactno.setText(document.getData().get("contact_no").toString());
                        if (document.getData().get("gender_is_male").toString().equals("true")) {
                            radio_male.setChecked(true);
                        } else {
                            radio_female.setChecked(true);
                        }

                    } else {
                        Log.d("fcm", "No such document");
                    }
                } else {
                    Log.d("fcm", "get failed with ", task.getException());
                }
            }

        });
    }

}

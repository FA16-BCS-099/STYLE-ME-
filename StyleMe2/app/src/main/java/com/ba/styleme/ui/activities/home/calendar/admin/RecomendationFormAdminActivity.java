package com.ba.styleme.ui.activities.home.calendar.admin;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.annotation.NonNull;

import com.ba.styleme.R;
import com.ba.styleme.data.network.model.EventDetailModel;
import com.ba.styleme.data.network.model.RecomendationModel;
import com.ba.styleme.services.broadcast_recevier.AlarmReceiver;
import com.ba.styleme.ui.activities.base.BaseActivity;
import com.ba.styleme.ui.activities.base.ImageUploadBase;
import com.ba.styleme.utils.Utils;
import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class RecomendationFormAdminActivity extends ImageUploadBase implements View.OnClickListener {
    EditText event_type, out_fit_type,weather;
    TextInputLayout til_event_date, til_event_name, til_event_description, til_event_type, til_out_fit_type,til_weather;

    Button submit;
    ImageView img_right;

    RecomendationModel recomendationModel=null;

    public void findViews() {

        event_type = findViewById(R.id.event_type);
        out_fit_type = findViewById(R.id.out_fit_type);
        weather = findViewById(R.id.weather);

        til_event_date = findViewById(R.id.til_event_date);
        til_event_date.setVisibility(View.GONE);
        til_event_name = findViewById(R.id.til_event_name);
        til_event_name.setVisibility(View.GONE);
        til_event_description = findViewById(R.id.til_event_description);
        til_event_description.setVisibility(View.GONE);
        til_event_type = findViewById(R.id.til_event_type);
        til_out_fit_type = findViewById(R.id.til_out_fit_type);
        til_weather = findViewById(R.id.til_weather);
        til_weather.setVisibility(View.VISIBLE);

        submit = findViewById(R.id.submit);
        submit.setOnClickListener(this);

        img_right = findViewById(R.id.img_right);
        img_right.setImageDrawable(getResources().getDrawable(R.drawable.ic_attachment));
        img_right.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showPictureDialog();
            }
        });
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_submission);
        setTitle(getResourceString(R.string.recomendatations));
        findViews();
        if (getIntent() != null) {
            recomendationModel = (RecomendationModel) getIntent().getSerializableExtra("recomendationModel");
            if(recomendationModel!=null) {
                event_type.setText(recomendationModel.getEvent_type());
                out_fit_type.setText(recomendationModel.getEvent_out_fit_type());
                weather.setText(recomendationModel.getWeather());
                findViewById(R.id.img_product).setVisibility(View.VISIBLE);

                Glide.with(context)
                        .load(recomendationModel.getImageUrl())
                        .placeholder(R.drawable.ic_logo)
                        .error(R.drawable.ic_logo)
                        .into((ImageView) findViewById(R.id.img_product));
            }
        }

    }

    @Override
    public void performImageAction(Bitmap bitmap, String base64String, File file) {
        this.file = file;

        findViewById(R.id.img_product).setVisibility(View.VISIBLE);
        ((ImageView) findViewById(R.id.img_product)).setImageBitmap(bitmap);
        if(recomendationModel!=null){
            recomendationModel.setImageUrl("");
        }
    }

    @Override
    public void onClick(View view) {
        if (validateData()) {
            submitData();
        }
    }

    public void navigateToNextScreen() {
        finish();
    }

    public boolean validateData() {

        if (event_type.getText().toString().isEmpty()) {
            til_event_type.setError(getResourceString(R.string.please_select) + " " + til_event_type.getHint());
            return false;
        } else {
            til_event_type.setErrorEnabled(false);
        }
        if (out_fit_type.getText().toString().isEmpty()) {
            til_out_fit_type.setError(getResourceString(R.string.please_select) + " " + til_out_fit_type.getHint());
            return false;
        } else {
            til_out_fit_type.setErrorEnabled(false);
        }

        if (weather.getText().toString().isEmpty()) {
            til_weather.setError(getResourceString(R.string.please_select) + " " + weather.getHint());
            return false;
        } else {
            til_weather.setErrorEnabled(false);
        }

        if (recomendationModel==null&&file == null) {
            showInfoToast("Please attached the image.");
            return false;
        }
        return true;
    }

    public void submitData() {
        showLoading();

        final CollectionReference events = db.collection("admin_event_detail");


        if(recomendationModel==null) {

            recomendationModel = new RecomendationModel(events.document().getId(), appPreference.getUserId(), "", event_type.getText().toString().trim(), out_fit_type.getText().toString().trim(),weather.getText().toString().trim());

        }

        recomendationModel.setEvent_out_fit_type(out_fit_type.getText().toString().trim());
        recomendationModel.setEvent_type( event_type.getText().toString().trim());
        recomendationModel.setWeather( weather.getText().toString().trim());

        final String id = recomendationModel.getId();
        Log.e("id", recomendationModel.getId());
        events.document(id).set(recomendationModel)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                           @Override
                                           public void onComplete(@NonNull final Task<Void> task) {
                                               if (task.isSuccessful()) {
                                                   if(recomendationModel.getImageUrl().trim().isEmpty()&&file!=null) {
                                                       uploadImage(id, events, recomendationModel);
                                                   }else{
                                                       hideLoading();
                                                   }
                                               } else {
                                                   hideLoading();
                                                   Log.w("fcm", "Error adding document", task.getException());
                                                   showInfoToast("Cannot connect to the server. Please try again later.");
                                               }
                                           }
                                       }
                );


    }

    public void uploadImage(final String id, final CollectionReference events, final RecomendationModel recomendationModel) {

        final StorageReference childRef = storageRef.child("AdminRecomendations/" + id);

        //uploading the image
        final UploadTask uploadTask = childRef.putFile(Uri.fromFile(file));

        uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
            @Override
            public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                if (!task.isSuccessful()) {
                    throw task.getException();
                }
                //  progressDialog.dismiss();
                //  neksuApp.showDialog(context, "Try again.");
                return childRef.getDownloadUrl();
            }
        }).addOnCompleteListener(new OnCompleteListener<Uri>() {
            @Override
            public void onComplete(@NonNull Task<Uri> task) {
                if (task.isSuccessful()) {
                    Uri downUri = task.getResult();
                    showInfoToast("Successfully Submitted.");
                    recomendationModel.setImageUrl(downUri.toString());
                    events.document(id).set(recomendationModel);
                    navigateToNextScreen();
                } else {
                    showInfoToast(task.getException().getMessage().toString());
                }
                hideLoading();

            }
        });

    }

}

package com.ba.styleme.ui.activities.home.calendar.user.add_look;

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
import com.ba.styleme.services.broadcast_recevier.AlarmReceiver;
import com.ba.styleme.ui.activities.base.ImageUploadBase;
import com.ba.styleme.utils.CommonUtils;
import com.ba.styleme.utils.Utils;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.kwabenaberko.openweathermaplib.constants.Lang;
import com.kwabenaberko.openweathermaplib.constants.Units;
import com.kwabenaberko.openweathermaplib.implementation.OpenWeatherMapHelper;
import com.kwabenaberko.openweathermaplib.implementation.callbacks.CurrentWeatherCallback;
import com.kwabenaberko.openweathermaplib.models.currentweather.CurrentWeather;

import java.io.File;

public class AddLookActivity extends ImageUploadBase implements View.OnClickListener {
    EditText event_date, event_name, event_description, event_type, out_fit_type;
    TextInputLayout til_event_date, til_event_name, til_event_description, til_event_type, til_out_fit_type;

    Button submit;

    ImageView img_right;

    EventDetailModel eventDetailModel;

    public void findViews() {
        event_date = findViewById(R.id.event_date);
        event_name = findViewById(R.id.event_name);
        event_description = findViewById(R.id.event_description);
        event_type = findViewById(R.id.event_type);
        out_fit_type = findViewById(R.id.out_fit_type);

        til_event_date = findViewById(R.id.til_event_date);
        til_event_name = findViewById(R.id.til_event_name);
        til_event_name.setVisibility(View.GONE);
        til_event_description = findViewById(R.id.til_event_description);
        til_event_description.setVisibility(View.GONE);
        til_event_type = findViewById(R.id.til_event_type);
        til_event_type.setVisibility(View.GONE);
        til_out_fit_type = findViewById(R.id.til_out_fit_type);

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
        setTitle(getResourceString(R.string.add_look));

        findViews();

        if (getIntent() != null) {
//            Log.v("unique",getIntent().getStringExtra("getWeather"));
            eventDetailModel = (EventDetailModel) getIntent().getParcelableExtra("calendarListModel");
            event_date.setText(Utils.getDate(eventDetailModel.getEvent_date()));
            Log.v("nabegha",eventDetailModel.getWeather());
        }


    }

    @Override
    public void performImageAction(Bitmap bitmap, String base64String, File file) {
        this.file=file;
        ((ImageView)findViewById(R.id.img_product)).setVisibility(View.VISIBLE);
        ((ImageView)findViewById(R.id.img_product)).setImageBitmap(bitmap);
    }

    @Override
    public void onClick(View view) {
        if (validateData()) {
            showLoading();
            final CollectionReference events = db.collection("evenet_detail");
            final CollectionReference looks = db.collection("look");
             DocumentReference user = db.collection("users").document(appPreference.getUserId());
            user.update("is_old_user",true).addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void aVoid) {

                                                                          }
                                                                      }
            );

            String id=eventDetailModel.getUser_id();

            eventDetailModel.setUser_id(appPreference.getUserId());
            eventDetailModel.setEvent_out_fit_type(out_fit_type.getText().toString().trim());


            events.document(id).set(eventDetailModel);
            looks.document(id).set(eventDetailModel);

            uploadImage(id,events);
            uploadImage(id,looks);

            Log.e("id",id);
            Log.e("id",eventDetailModel.getUser_id());


        }

    }

    public void uploadImage(final String id, final CollectionReference events) {

        final StorageReference childRef = storageRef.child("AddLook/" + id);

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
                    eventDetailModel.setImageUrl(downUri.toString());
                    events.document(id).set(eventDetailModel);


                    Utils.setReminder(context, AlarmReceiver.class, eventDetailModel);
                }else{
                    showInfoToast(task.getException().getMessage().toString());
                }
                hideLoading();
                finish();
            }
        });

    }




    public boolean validateData() {
        if (out_fit_type.getText().toString().isEmpty()) {
            til_out_fit_type.setError(getResourceString(R.string.please_select)+" "+til_out_fit_type.getHint());
            return false;
        }else{
            til_out_fit_type.setErrorEnabled(false);
        }

        if(file==null){
            showInfoToast("Please attached the image.");
            return false;
        }
        return true;
    }
}

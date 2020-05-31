package com.ba.styleme.ui.activities.home.calendar.user;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;

import com.ba.styleme.R;
import com.ba.styleme.data.network.model.EventDetailModel;
import com.ba.styleme.ui.activities.base.BaseActivity;
import com.ba.styleme.utils.CommonUtils;
import com.ba.styleme.utils.Utils;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.kwabenaberko.openweathermaplib.constants.Lang;
import com.kwabenaberko.openweathermaplib.constants.Units;
import com.kwabenaberko.openweathermaplib.implementation.OpenWeatherMapHelper;
import com.kwabenaberko.openweathermaplib.implementation.callbacks.CurrentWeatherCallback;
import com.kwabenaberko.openweathermaplib.models.currentweather.CurrentWeather;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class EventSubmissionActivity extends BaseActivity implements View.OnClickListener {
    EditText event_date, event_name, event_description, event_type, out_fit_type;
    TextInputLayout til_event_date, til_event_name, til_event_description, til_event_type, til_out_fit_type;

    Button submit;

    EventDetailModel eventDetailModel;

    public void findViews() {
        event_date = findViewById(R.id.event_date);
        event_name = findViewById(R.id.event_name);
        event_description = findViewById(R.id.event_description);
        event_type = findViewById(R.id.event_type);
        out_fit_type = findViewById(R.id.out_fit_type);

        til_event_date = findViewById(R.id.til_event_date);
        til_event_name = findViewById(R.id.til_event_name);
        til_event_description = findViewById(R.id.til_event_description);
        til_event_type = findViewById(R.id.til_event_type);
        til_out_fit_type = findViewById(R.id.til_out_fit_type);

        submit = findViewById(R.id.submit);


        submit.setOnClickListener(this);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_submission);
        setTitle(getResourceString(R.string.event_detail));

        findViews();

        if (getIntent() != null) {
            if (getIntent().getStringExtra("action").equals("update")) {
                eventDetailModel = (EventDetailModel) getIntent().getParcelableExtra("eventDetailModel");
                event_name.setText(eventDetailModel.getEvent_name());
                event_type.setText(eventDetailModel.getEvent_type());
                event_description.setText(eventDetailModel.getEvent_description());
                out_fit_type.setText(eventDetailModel.getEvent_out_fit_type());
                event_date.setText(Utils.getDate(eventDetailModel.getEvent_date()));

            }
            if (getIntent().getStringExtra("action").equals("add")) {
                eventDetailModel = (EventDetailModel) getIntent().getParcelableExtra("calendarListModel");
                event_date.setText(Utils.getDate(eventDetailModel.getEvent_date()));


            }
        }
    }

    @Override
    public void onClick(View view) {
        if (!validateData()) {

        }else if(currentWeather==null){
            fetchLocation();
        }
    }

    public void navigateToNextScreen() {
        showAlertDialog("Today weather temperature is "+currentWeather.getMain().getTempMax()+"℃. So, Recommendation will be accordingly.");
    }

    public void showAlertDialog(String title) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setMessage(title);
        builder.setNegativeButton(context.getString(android.R.string.ok),
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        if(dialog!=null ){
                            dialog.dismiss();
                            eventDetailModel.setWeather(getWeather(currentWeather.getMain().getTempMax()));
                            Log.v("nabegha",eventDetailModel.getWeather());
                            Intent intent = new Intent(context, RecomendationActivity.class);
                            intent.putExtra("calendarListModel", eventDetailModel);

                            intent.putExtra("getWeather",getWeather(currentWeather.getMain().getTempMax()));
                            finish();
                            Log.v("unique",getWeather(currentWeather.getMain().getTempMax()));
                            startActivity(intent);
                        }
                    }
                });

        builder.setCancelable(false);
        builder.show();
    }


    public boolean validateData() {
        if (event_name.getText().toString().isEmpty()) {
            til_event_name.setError(getResourceString(R.string.this_field_should_not_empty));
            return false;
        } else {
            til_event_name.setErrorEnabled(false);
        }
        if (event_description.getText().toString().isEmpty()) {
            til_event_description.setError(getResourceString(R.string.this_field_should_not_empty));
            return false;
        } else {
            til_event_description.setErrorEnabled(false);
        }
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
        return true;
    }

    public void submitData() {

        // Create a new user with a first and last name
        final Map<String, Object> event = new HashMap<>();
        // Access a Cloud Firestore instance from your Activity

        event.put("user_id", appPreference.getUserId());
        event.put("event_date", new Timestamp(new Date(Utils.getTimeInMillisecond(event_date.getText().toString().trim()))));
        event.put("event_name", event_name.getText().toString().trim());
        event.put("event_description", event_description.getText().toString().trim());
        event.put("event_type", event_type.getText().toString().trim());
        event.put("event_out_fit_type", out_fit_type.getText().toString().trim());
        event.put("date", eventDetailModel.getDate());
        event.put("imageUrl", "");
        event.put("weather" ,getWeather(currentWeather.getMain().getTempMax()));
        Log.v("unique",getWeather(currentWeather.getMain().getTempMax()));
        final CollectionReference events = db.collection("evenet_detail");


        eventDetailModel.setEvent_name(event_date.getText().toString().trim());
        eventDetailModel.setEvent_name(event_name.getText().toString().trim());
        eventDetailModel.setEvent_description(event_description.getText().toString().trim());
        eventDetailModel.setEvent_type(event_type.getText().toString().trim());
        eventDetailModel.setEvent_out_fit_type(out_fit_type.getText().toString().trim());

        final String id = events.document().getId();
        eventDetailModel.setUser_id(id);
        Log.e("id", eventDetailModel.getUser_id());
        events.document(id).set(event)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                           @Override
                                           public void onComplete(@NonNull final Task<Void> task) {
                                               hideLoading();
                                               if (task.isSuccessful()) {
                                                   showInfoToast("Updated successfully.");
                                                   navigateToNextScreen();
                                               } else {
                                                   Log.w("fcm", "Error adding document", task.getException());
                                                   showInfoToast("Cannot connect to the server. Please try again later.");
                                               }
                                           }
                                       }
                );

    }

    private void fetchLocation() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 105);
            return;
        }
        if(isGPSEnableCheck()) {
            showLoading();
            FusedLocationProviderClient fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);;
            Task<Location> task = fusedLocationProviderClient.getLastLocation();
            task.addOnSuccessListener(new OnSuccessListener<Location>() {
                @Override
                public void onSuccess(Location location) {

                    if(location!=null) {
                        getWeahterData(location.getLatitude(), location.getLongitude());
                    }else{
                        fetchLocation();
                    }

                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    hideLoading();
                    showInfoToast(e.toString());
                }
            });
        }else{
            showInfoToast("Kindly on your gps and try again!");
        }
    }

    public Boolean isGPSEnableCheck() {
        final LocationManager manager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        return manager.isProviderEnabled(LocationManager.GPS_PROVIDER);
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case 105:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    fetchLocation();
                }
                break;
        }
    }

    CurrentWeather currentWeather;

    public void getWeahterData(double latitude,double longitude) {
        OpenWeatherMapHelper helper = new OpenWeatherMapHelper(getString(R.string.OPEN_WEATHER_MAP_API_KEY));
        helper.setUnits(Units.METRIC);
        helper.setLang(Lang.ENGLISH);
        final String TAG = "Weather_Data";
        if(CommonUtils.isOnline(context)) {
            helper.getCurrentWeatherByGeoCoordinates(latitude, longitude, new CurrentWeatherCallback() {
                @Override
                public void onSuccess(CurrentWeather cw) {
                    currentWeather=cw;
                    Log.v(TAG, "Coordinates: " + currentWeather.getCoord().getLat() + ", " + currentWeather.getCoord().getLon() + "\n"
                            + "Weather Description: " + currentWeather.getWeather().get(0).getDescription() + "\n"
                            + "Temperature: " + currentWeather.getMain().getTempMax() + "\n"
                            + "Wind Speed: " + currentWeather.getWind().getSpeed() + "\n"
                            + "City, Country: " + currentWeather.getName() + ", " + currentWeather.getSys().getCountry()
                    );
                    Log.v("new_magic","aimen");
                    Log.v("new_magic", Objects.requireNonNull(getIntent().getStringExtra("action")));
                    String action =getIntent().getStringExtra("action");
                    Log.v("nabegha",eventDetailModel.getDate());

                    if (action.equals("update")){
                        Log.v("nabegha",eventDetailModel.getDate());
                                final CollectionReference events = db.collection("evenet_detail");
                                Query query= events.whereEqualTo("date",eventDetailModel.getDate());
                                Log.v("nabegha_kashmiri",eventDetailModel.getDate());
                                Log.v("magic","tum farigh nai ho");
                                Log.v("nabegha",eventDetailModel.getDate());


                                final Map<String, Object> event = new HashMap<>();
                                // Access a Cloud Firestore instance from your Activity

                                event.put("user_id", appPreference.getUserId());
                                event.put("event_date", new Timestamp(new Date(Utils.getTimeInMillisecond(event_date.getText().toString().trim()))));
                                event.put("event_name", event_name.getText().toString().trim());
                                event.put("event_description", event_description.getText().toString().trim());
                                event.put("event_type", event_type.getText().toString().trim());
                                event.put("event_out_fit_type", out_fit_type.getText().toString().trim());
                                event.put("date", eventDetailModel.getDate());
                                event.put("imageUrl",eventDetailModel.getImageUrl());
                                event.put("weather" ,getWeather(currentWeather.getMain().getTempMax()));
                                eventDetailModel.setEvent_name(event_date.getText().toString().trim());
                                eventDetailModel.setEvent_name(event_name.getText().toString().trim());
                                eventDetailModel.setEvent_description(event_description.getText().toString().trim());
                                eventDetailModel.setEvent_type(event_type.getText().toString().trim());
                                eventDetailModel.setEvent_out_fit_type(out_fit_type.getText().toString().trim());

                                final String id = events.document().getId();
                                eventDetailModel.setUser_id(id);

                                query.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                                    @Override
                                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                                        for (DocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                                            final String id = documentSnapshot.getId();
                                            events.document(id).set(event)
                                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                               @Override
                                                                               public void onComplete(@NonNull final Task<Void> task) {
                                                                                   hideLoading();
                                                                                   if (task.isSuccessful()) {
                                                                                       showInfoToast("Updated successfully.");
                                                                                       navigateToNextScreen();
                                                                                   } else {
                                                                                       Log.w("fcm", "Error adding document", task.getException());
                                                                                       showInfoToast("Cannot connect to the server. Please try again later.");
                                                                                   }
                                                                               }
                                                                           }
                                                    );

                                        }
                                    }
                                });



                            }




                    if(getIntent().getStringExtra("action").equals("add")){
                    submitData();}
                }

                @Override
                public void onFailure(Throwable throwable) {
                    hideLoading();
                    showInfoToast(throwable.getMessage());
                    Log.v(TAG, throwable.getMessage());
                }
            });
        }else{
            hideLoading();
            showInfoToast("Kindly on your internet and try again!");
        }
    }

    public String getWeather(double temp){

        if(temp<20){
            return getResourceString(R.string.cold);
        }else if(temp>=20&&temp<=30){
            return getResourceString(R.string.moderate);
        }else{
            return getResourceString(R.string.warm);
        }
    }
}

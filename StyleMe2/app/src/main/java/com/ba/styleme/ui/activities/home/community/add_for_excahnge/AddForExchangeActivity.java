package com.ba.styleme.ui.activities.home.community.add_for_excahnge;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import androidx.annotation.NonNull;

import com.ba.styleme.R;
import com.ba.styleme.ui.activities.base.BaseActivity;
import com.ba.styleme.ui.activities.base.ImageUploadBase;
import com.ba.styleme.utils.Utils;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class AddForExchangeActivity extends ImageUploadBase implements View.OnClickListener , LocationListener {
    private LocationManager locationManager;
    private LocationListener locationListener;

    Geocoder geocoder;
    List<Address> addresses;



    ImageView img_right, img_product;
    EditText pro_name, pro_texture, pro_worth, pro_exchange_for, contact_no,location;
    TextInputLayout til_pro_name, til_pro_texture, til_pro_worth, til_pro_exchange_for, til_contact_no,til_location;
    RadioGroup rg_status;
    RadioButton rb_available, rb_not_available;

    Button submit;

    File file;

    public void findViews() {
        img_right = findViewById(R.id.img_right);
        img_product = findViewById(R.id.img_product);

        pro_name = findViewById(R.id.pro_name);
        pro_texture = findViewById(R.id.pro_texture);
        pro_worth = findViewById(R.id.pro_worth);
        pro_exchange_for = findViewById(R.id.pro_exchange_for);
        contact_no = findViewById(R.id.contact_no);
        location = findViewById(R.id.location);

        til_pro_name = findViewById(R.id.til_pro_name);
        til_pro_texture = findViewById(R.id.til_pro_texture);
        til_pro_worth = findViewById(R.id.til_pro_worth);
        til_pro_exchange_for = findViewById(R.id.til_pro_exchange_for);
        til_contact_no = findViewById(R.id.til_contact_no);
        til_location = findViewById(R.id.til_location);

        rg_status = findViewById(R.id.rg_status);
        rb_available = findViewById(R.id.rb_available);
        rb_not_available = findViewById(R.id.rb_not_available);

        submit = findViewById(R.id.submit);
        submit.setOnClickListener(this);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_for_exchange);
        setTitle(getResourceString(R.string.add_for_exchange));

        findViews();

        img_right.setImageDrawable(getDrawable(R.drawable.ic_attachment));
        img_right.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showPictureDialog();
            }
        });

        geocoder = new Geocoder(this, Locale.getDefault());

        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location loc) {
                Log.d("location", Double.toString(loc.getLongitude()));
                String  longitude= Double.toString(loc.getLongitude());
                String latitude= Double.toString(loc.getLatitude());
                try {
                    addresses  = geocoder.getFromLocation(loc.getLatitude(),loc.getLongitude(), 1);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                String address = addresses.get(0).getAddressLine(0);
                String city = addresses.get(0).getLocality();
                location.setText(address + city);
                location.setFocusable(false);


//
//

            }

            @Override
            public void onStatusChanged(String s, int i, Bundle bundle) {

            }

            @Override
            public void onProviderEnabled(String s) {

            }

            @Override
            public void onProviderDisabled(String s) {
                Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                startActivity(intent);
            }
        };
        if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{
                    Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.INTERNET}, 5);

            return;
        } else {
            locationManager.requestLocationUpdates("gps", 5000, 0, locationListener);

        }


    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case 5:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    locationManager.requestLocationUpdates("gps", 5000, 0, locationListener);

                }
        }

    }


    @Override
    public void onClick(View view) {
        if (validateData()) {
            submitData();
        }

    }


    public boolean validateData() {
        if (pro_name.getText().toString().isEmpty()) {
            til_pro_name.setError(getResourceString(R.string.this_field_should_not_empty));
            return false;
        } else {
            til_pro_name.setErrorEnabled(false);
        }
        if (pro_texture.getText().toString().isEmpty()) {
            til_pro_texture.setError(getResourceString(R.string.this_field_should_not_empty));
            return false;
        } else {
            til_pro_texture.setErrorEnabled(false);
        }
        if (pro_worth.getText().toString().isEmpty()) {
            til_pro_worth.setError(getResourceString(R.string.this_field_should_not_empty));
            return false;
        } else {
            til_pro_worth.setErrorEnabled(false);
        }

        if (pro_exchange_for.getText().toString().isEmpty()) {
            til_pro_exchange_for.setError(getResourceString(R.string.this_field_should_not_empty));
            return false;
        } else {
            til_pro_exchange_for.setErrorEnabled(false);
        }

        if (contact_no.getText().toString().isEmpty()) {
            til_contact_no.setError(getResourceString(R.string.this_field_should_not_empty));
            return false;
        } else {
            til_contact_no.setErrorEnabled(false);
        }

        if (contact_no.getText().toString().length()!=11) {
            til_contact_no.setError("Should be 11 digits only.");
            return false;
        } else {
            til_contact_no.setErrorEnabled(false);
        }
        if (location.getText().toString().isEmpty()) {
            til_location.setError(getResourceString(R.string.this_field_should_not_empty));
            return false;
        } else {
            til_location.setErrorEnabled(false);
        }


        if(file==null){
            showInfoToast("Please attached the image.");
            return false;
        }
        return true;
    }

    @Override
    public void performImageAction(Bitmap bitmap, String base64String, File file) {
        this.file = file;
        img_product.setVisibility(View.VISIBLE);
        img_product.setImageBitmap(bitmap);
    }


    public void submitData() {
        showLoading();

        // Create a new user with a first and last name
        final Map<String, Object> event = new HashMap<>();
        // Access a Cloud Firestore instance from your Activity
        pro_name = findViewById(R.id.pro_name);
        pro_texture = findViewById(R.id.pro_texture);
        pro_worth = findViewById(R.id.pro_worth);
        pro_exchange_for = findViewById(R.id.pro_exchange_for);
        contact_no = findViewById(R.id.contact_no);
        event.put("user_id", appPreference.getUserId());
        event.put("pro_name", pro_name.getText().toString().trim());
        event.put("pro_texture", pro_texture.getText().toString().trim());
        event.put("pro_worth", pro_worth.getText().toString().trim());
        event.put("pro_exchange_for", pro_exchange_for.getText().toString().trim());
        event.put("contact_no", contact_no.getText().toString().trim());
        event.put("is_available", rb_available.isChecked());
        event.put("imageUrl", "");
        event.put("location", location.getText().toString().trim());

        final CollectionReference events = db.collection("add_for_exchange");
        final String id = events.document().getId();
        events.document(id).set(event)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                           @Override
                                           public void onComplete(@NonNull final Task<Void> task) {


                                               if (task.isSuccessful()) {
                                                   if (file != null) {
                                                       uploadImage(id, events, event);
                                                   } else {
                                                       Log.e("nullfile", "nullfile");
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

    public void uploadImage(final String id, final CollectionReference events, final Map<String, Object> event) {

        final StorageReference childRef = storageRef.child("AddForExchangeImages/" + id);
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

                    showInfoToast("Successfully Updated.");
                    event.put("imageUrl", downUri.toString());
                    events.document(id).set(event);
                }
                hideLoading();
            }
        });

    }

    @Override
    public void onLocationChanged(Location location) {

    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }
}

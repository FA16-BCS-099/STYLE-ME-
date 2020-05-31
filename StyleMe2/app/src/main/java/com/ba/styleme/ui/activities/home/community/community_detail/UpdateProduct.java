package com.ba.styleme.ui.activities.home.community.community_detail;


import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import androidx.annotation.NonNull;

import com.ba.styleme.R;
import com.ba.styleme.data.network.model.CommunityListModel;
import com.ba.styleme.data.network.model.RecomendationModel;
import com.ba.styleme.ui.activities.base.BaseActivity;
import com.ba.styleme.ui.activities.base.ImageUploadBase;
import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

public class UpdateProduct extends ImageUploadBase implements View.OnClickListener  {

    ImageView img_right, img_product;
    EditText pro_name, pro_texture, pro_worth, pro_exchange_for, contact_no;
    TextInputLayout til_pro_name, til_pro_texture, til_pro_worth, til_pro_exchange_for, til_contact_no;
    RadioGroup rg_status;
    RadioButton rb_available, rb_not_available;

    Button submit;

    File file;
    CommunityListModel communityListModel=null;
    public void findViews() {
        img_right = findViewById(R.id.img_right);
        img_product = findViewById(R.id.img_product_update);

        pro_name = findViewById(R.id.pro_name_update);
        pro_texture = findViewById(R.id.pro_texture_update);
        pro_worth = findViewById(R.id.pro_worth_update);
        pro_exchange_for = findViewById(R.id.pro_exchange_for_update);
        contact_no = findViewById(R.id.contact_no_update);


        til_pro_name = findViewById(R.id.til_pro_name_update);
        til_pro_texture = findViewById(R.id.til_pro_texture_update);
        til_pro_worth = findViewById(R.id.til_pro_worth_update);
        til_pro_exchange_for = findViewById(R.id.til_pro_exchange_for_update);
        til_contact_no = findViewById(R.id.till_contact_no_update);

        rg_status = findViewById(R.id.rg_status);
        rb_available = findViewById(R.id.rb_available);
        rb_not_available = findViewById(R.id.rb_not_available);

        submit = findViewById(R.id.submit_update);
        submit.setOnClickListener(this);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_product);
        setTitle("Edit Product");
        findViews();
        if (getIntent() != null) {
            communityListModel = (CommunityListModel) getIntent().getSerializableExtra("communityListModel");
            if(communityListModel!=null) {
                pro_name.setText(communityListModel.getPro_name());
                pro_exchange_for.setText(communityListModel.getPro_exchange_for());
                pro_texture.setText(communityListModel.getPro_texture());
                pro_worth.setText(communityListModel.getPro_worth());
                contact_no.setText(communityListModel.getContact_no());


                img_right.setImageDrawable(getDrawable(R.drawable.ic_attachment));
                img_right.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        showPictureDialog();
                    }
                });


                findViewById(R.id.img_product_update).setVisibility(View.VISIBLE);

                Glide.with(context)
                        .load(communityListModel.getImageUrl())
                        .placeholder(R.drawable.ic_logo)
                        .error(R.drawable.ic_logo)
                        .into((ImageView) findViewById(R.id.img_product_update));

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
        pro_name = findViewById(R.id.pro_name_update);
        pro_texture = findViewById(R.id.pro_texture_update);
        pro_worth = findViewById(R.id.pro_worth_update);
        pro_exchange_for = findViewById(R.id.pro_exchange_for_update);
        contact_no = findViewById(R.id.contact_no_update);
        event.put("user_id", appPreference.getUserId());
        event.put("pro_name", pro_name.getText().toString().trim());
        event.put("pro_texture", pro_texture.getText().toString().trim());
        event.put("pro_worth", pro_worth.getText().toString().trim());
        event.put("pro_exchange_for", pro_exchange_for.getText().toString().trim());
        event.put("contact_no", contact_no.getText().toString().trim());
        event.put("is_available", rb_available.isChecked());
        event.put("imageUrl", "");
        final CollectionReference events = db.collection("add_for_exchange");
        Query query= events.whereEqualTo("imageUrl",communityListModel.getImageUrl());

        query.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                for (DocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                    final String id = documentSnapshot.getId();
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
            }
        });



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

}



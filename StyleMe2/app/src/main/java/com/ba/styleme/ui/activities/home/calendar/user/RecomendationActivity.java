package com.ba.styleme.ui.activities.home.calendar.user;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.ba.styleme.R;
import com.ba.styleme.data.network.ApiConstant;
import com.ba.styleme.data.network.model.EventDetailModel;
import com.ba.styleme.data.network.model.RecomendationModel;
import com.ba.styleme.ui.activities.home.calendar.user.add_look.AddLookActivity;
import com.ba.styleme.ui.adapters.RecomendationListAdapter;
import com.ba.styleme.ui.activities.base.BaseActivity;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

import static com.ba.styleme.data.network.ApiConstant.adminUserId;

public class RecomendationActivity extends BaseActivity {
    RecyclerView rv_list,look_list;
    ArrayList<RecomendationModel> recomendationModels = new ArrayList<>();
    ArrayList<RecomendationModel> lookModels = new ArrayList<>();


    RecomendationListAdapter recomendationListAdapter;


    EventDetailModel eventDetailModel;
    String weatherName="";
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_with_back_button);
        setTitle(getResourceString(R.string.recomendatations));
        if (getIntent() != null) {
            eventDetailModel = (EventDetailModel) getIntent().getParcelableExtra("calendarListModel");
            weatherName=getIntent().getStringExtra("getWeather");

        }
        rv_list = findViewById(R.id.rv_list);
        look_list=findViewById(R.id.look_list);
        findViewById(R.id.ll_community_items).setVisibility(View.GONE);
        findViewById(R.id.add_for_exchange).setVisibility(View.GONE);

        GridLayoutManager gridLayoutManager=new GridLayoutManager(this,2);
        rv_list.setLayoutManager(gridLayoutManager);
        GridLayoutManager gridLayoutManagerlook=new GridLayoutManager(this,2);
        look_list.setLayoutManager(gridLayoutManagerlook);
       /* dressSuggestionListModels.add(new DressSuggestionListModel("http://pngimg.com/uploads/dress/dress_PNG180.png", "test"));
        dressSuggestionListModels.add(new DressSuggestionListModel("http://pngimg.com/uploads/dress/dress_PNG180.png", "test"));
        dressSuggestionListModels.add(new DressSuggestionListModel("http://pngimg.com/uploads/dress/dress_PNG180.png", "test"));
        dressSuggestionListModels.add(new DressSuggestionListModel("http://pngimg.com/uploads/dress/dress_PNG180.png", "test"));
        dressSuggestionListModels.add(new DressSuggestionListModel("http://pngimg.com/uploads/dress/dress_PNG180.png", "test"));
*/
        recomendationListAdapter = new RecomendationListAdapter(this,appPreference,recomendationModels, eventDetailModel);
        rv_list.setAdapter(recomendationListAdapter);
        recomendationListAdapter = new RecomendationListAdapter(this,appPreference,lookModels, eventDetailModel);
        look_list.setAdapter(recomendationListAdapter);


    }

    @Override
    public void onResume(){
        super.onResume();
        getData();
        getlookData();
    }


    public void getData(){
        showLoading();
        final CollectionReference events = db.collection("admin_event_detail");
        Log.e("user_id", adminUserId);
        events.whereEqualTo("user_id", adminUserId).whereEqualTo("weather",weatherName).whereEqualTo("event_type",eventDetailModel.getEvent_type()).whereEqualTo("event_out_fit_type",eventDetailModel.getEvent_out_fit_type()).get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot documentSnapshots) {
                if (documentSnapshots.isEmpty()) {
                    hideLoading();
                    showHideNoRecordFound(recomendationModels.size());
                    Log.e("message", "onSuccess: LIST EMPTY"+documentSnapshots.toString());
                    return;
                } else {
                    for (DocumentSnapshot documentSnapshot : documentSnapshots) {
                        if (documentSnapshot.exists()) {
                            // Convert the whole Query Snapshot to a list
                            // of objects directly! No need to fetch each
                            // document.
                            recomendationModels.clear();
                            recomendationModels.addAll(documentSnapshots.toObjects(RecomendationModel.class));

                            recomendationListAdapter.notifyDataSetChanged();
                            hideLoading();
                            showHideNoRecordFound(recomendationModels.size());

                        }
                        break;
                    }
                }
            }
        });
    }
    public void getlookData(){
        showLoading();
        final CollectionReference events = db.collection("look");
        Log.e("user_id", adminUserId);
        events.whereEqualTo("user_id", appPreference.getUserId()).whereEqualTo("weather",weatherName).whereEqualTo("event_type",eventDetailModel.getEvent_type()).whereEqualTo("event_out_fit_type",eventDetailModel.getEvent_out_fit_type()).get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot documentSnapshots) {
                if (documentSnapshots.isEmpty()) {
                    hideLoading();
                    showHideNoRecordFound(lookModels.size());
                    Log.e("message", "onSuccess: LIST EMPTY"+documentSnapshots.toString());
                    return;
                } else {
                    for (DocumentSnapshot documentSnapshot : documentSnapshots) {
                        if (documentSnapshot.exists()) {
                            // Convert the whole Query Snapshot to a list
                            // of objects directly! No need to fetch each
                            // document.
                            lookModels.clear();
                            lookModels.addAll(documentSnapshots.toObjects(RecomendationModel.class));

                            recomendationListAdapter.notifyDataSetChanged();
                            hideLoading();
                            showHideNoRecordFound(lookModels.size());

                        }
                        break;
                    }
                }
            }
        });
    }

}

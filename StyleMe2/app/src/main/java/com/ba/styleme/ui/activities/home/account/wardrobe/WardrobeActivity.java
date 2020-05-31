package com.ba.styleme.ui.activities.home.account.wardrobe;

import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.ba.styleme.R;
import com.ba.styleme.data.network.model.EventDetailModel;
import com.ba.styleme.ui.activities.base.BaseActivity;
import com.ba.styleme.ui.adapters.WardrobeListAdapter;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class WardrobeActivity extends BaseActivity {
    RecyclerView rv_list;
    List<EventDetailModel> eventDetailModels = new ArrayList<>();
    WardrobeListAdapter wardrobeListAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_with_back_button);
        setTitle(getResourceString(R.string.wardrobe));

        rv_list = findViewById(R.id.rv_list);
        findViewById(R.id.ll_community_items).setVisibility(View.GONE);
        findViewById(R.id.add_for_exchange).setVisibility(View.GONE);

        GridLayoutManager gridLayoutManager=new GridLayoutManager(this,2);
        rv_list.setLayoutManager(gridLayoutManager);

        wardrobeListAdapter = new WardrobeListAdapter(this,eventDetailModels);
        rv_list.setAdapter(wardrobeListAdapter);
        getData();

    }
    public void getData(){
        showLoading();
        final CollectionReference events = db.collection("evenet_detail");

        events.whereEqualTo("user_id", appPreference.getUserId()).get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot documentSnapshots) {
                if (documentSnapshots.isEmpty()) {
                    hideLoading();
                    showHideNoRecordFound(eventDetailModels.size());
                    Log.e("message", "onSuccess: LIST EMPTY"+documentSnapshots.toString());
                    return;
                } else {
                    for (DocumentSnapshot documentSnapshot : documentSnapshots) {
                        if (documentSnapshot.exists()) {
                            // Convert the whole Query Snapshot to a list
                            // of objects directly! No need to fetch each
                            // document.
                            eventDetailModels.clear();
                            eventDetailModels.addAll( documentSnapshots.toObjects(EventDetailModel.class));

                            Log.e("Size",eventDetailModels.size()+"");
                            wardrobeListAdapter.notifyDataSetChanged();
                            hideLoading();
                            showHideNoRecordFound(eventDetailModels.size());
                            break;

                        }
                    }
                }
            }
        });
    }

}

package com.ba.styleme.ui.activities.home.calendar.admin;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.ba.styleme.R;
import com.ba.styleme.data.network.model.EventDetailModel;
import com.ba.styleme.data.network.model.RecomendationModel;
import com.ba.styleme.ui.activities.base.BaseActivity;
import com.ba.styleme.ui.adapters.RecomendationListAdapter;
import com.ba.styleme.utils.Utils;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class RecomendationAdminActivity extends BaseActivity {
    RecyclerView rv_list;
    ArrayList<RecomendationModel> recomendationModels = new ArrayList<>();
    RecomendationListAdapter recomendationListAdapter;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_with_back_button);
        setTitle(getResourceString(R.string.recomendatations));

        findViewById(R.id.img_back_button).setEnabled(false);
        findViewById(R.id.img_back_button).setVisibility(View.INVISIBLE);
        ((ImageView) findViewById(R.id.img_right)).setImageDrawable(context.getDrawable(R.drawable.ic_add));
        findViewById(R.id.img_right).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(context, RecomendationFormAdminActivity.class));
            }
        });

        rv_list = findViewById(R.id.rv_list);
        findViewById(R.id.ll_community_items).setVisibility(View.GONE);
        findViewById(R.id.add_for_exchange).setVisibility(View.GONE);
        findViewById(R.id.img_logout).setVisibility(View.VISIBLE);

        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 2);
        rv_list.setLayoutManager(gridLayoutManager);
        recomendationListAdapter = new RecomendationListAdapter(this, appPreference, recomendationModels, null);
        rv_list.setAdapter(recomendationListAdapter);


    }

    @Override
    public void onResume(){
        super.onResume();
        getData();
    }

    public void getData() {
        showLoading();
        final CollectionReference events = db.collection("admin_event_detail");
        Log.e("user_id", appPreference.getUserId());
        events.whereEqualTo("user_id", appPreference.getUserId()).get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot documentSnapshots) {
                if (documentSnapshots.isEmpty()) {
                    hideLoading();
                    showHideNoRecordFound(recomendationModels.size());
                    Log.e("message", "onSuccess: LIST EMPTY" + documentSnapshots.toString());
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

}

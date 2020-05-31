package com.ba.styleme.ui.activities.home.community.community_detail;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.ba.styleme.data.network.model.CommunityListModel;
import com.ba.styleme.ui.activities.base.BaseActivity;

import android.os.Bundle;
import android.util.Log;

import com.ba.styleme.R;
import com.ba.styleme.ui.adapters.CommunityListAdapter;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class ShowProduct extends BaseActivity{
    RecyclerView rv_list;
    List<CommunityListModel> communityListModels = new ArrayList<>();
    CommunityListAdapter communityListAdapter;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_product);
      setTitle("My Product");
      setSlidingPane();
        rv_list = findViewById(R.id.productlist);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 2);
        rv_list.setLayoutManager(gridLayoutManager);

        communityListAdapter = new  CommunityListAdapter(this, communityListModels,true);
        rv_list.setAdapter(communityListAdapter);

        getCommunityList();

    }
    @Override
    public void onResume(){
        super.onResume();
        getCommunityList();
    }

    public void getCommunityList() {
        showLoading();
        final CollectionReference events = db.collection("add_for_exchange");
        Query query= events.whereEqualTo("user_id",appPreference.getUserId());

        query.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot documentSnapshots) {
                if (documentSnapshots.isEmpty()) {
                    hideLoading();
                    showHideNoRecordFound(communityListModels.size());
                    Log.e("message", "onSuccess: LIST EMPTY");
                    return;
                } else {
                    for (DocumentSnapshot documentSnapshot : documentSnapshots) {
                        if (documentSnapshot.exists()) {
                            // Convert the whole Query Snapshot to a list
                            // of objects directly! No need to fetch each
                            // document.
                            communityListModels.clear();
                            List<CommunityListModel> communityListModel = documentSnapshots.toObjects(CommunityListModel.class);
                            communityListModels.addAll(communityListModel);
                            hideLoading();
                            communityListAdapter.notifyDataSetChanged();
                            showHideNoRecordFound(communityListModels.size());
                            Log.e("snapshot", communityListModels.size() + "");
                        }
                        Log.e("snapshot", documentSnapshot.toString());
                    }
                }
            }
        });

    }
}
package com.ba.styleme.ui.activities.home.community;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.ba.styleme.R;
import com.ba.styleme.ui.activities.home.community.community_detail.ShowProduct;
import com.ba.styleme.ui.adapters.CommunityListAdapter;
import com.ba.styleme.data.network.model.CommunityListModel;
import com.ba.styleme.ui.activities.base.BaseActivity;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class CommunityActivity extends BaseActivity {
    RecyclerView rv_list;
    List<CommunityListModel> communityListModels = new ArrayList<>();
    CommunityListAdapter communityListAdapter;
    ImageView articles,shopping_site,show_product;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);
        setTitle(getResourceString(R.string.community));
        setSlidingPane();
        articles=findViewById(R.id.ic_articles);
        shopping_site=findViewById(R.id.ic_shopping_sites);
        show_product=findViewById(R.id.show_product);

        final String[] shopping_links={"https://www.khaadi.com/","https://www.louisvuitton.com/","https://www.gulahmedshop.com/","https://www.sanasafinaz.com/"};
        final String[] article_links={"https://www.fibre2fashion.com/industry-article/free-fashion-industry-article/7","https://www.vogue.com/fashion","https://fashionmagazine.com/"};
        articles.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Random generator = new Random();
                int randomIndex = generator.nextInt(article_links.length);
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(article_links[randomIndex]));
                startActivity(browserIntent);


            }
        });
        shopping_site.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Random generator = new Random();
                int randomIndex = generator.nextInt(shopping_links.length);
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(shopping_links[randomIndex]));
                startActivity(browserIntent);
            }
        });
        show_product.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(v.getContext(), ShowProduct.class);
                startActivity(intent);
            }
        });

        rv_list = findViewById(R.id.rv_list);
        findViewById(R.id.ll_community_items).setVisibility(View.VISIBLE);
        findViewById(R.id.add_for_exchange).setVisibility(View.VISIBLE);

        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 2);
        rv_list.setLayoutManager(gridLayoutManager);

        communityListAdapter = new CommunityListAdapter(this, communityListModels);
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
        events.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
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

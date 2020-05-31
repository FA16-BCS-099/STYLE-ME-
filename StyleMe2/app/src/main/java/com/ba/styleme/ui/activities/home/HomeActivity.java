package com.ba.styleme.ui.activities.home;

import android.os.Bundle;
import android.util.Log;

import com.ba.styleme.R;
import com.ba.styleme.data.network.model.RecomendationModel;
import com.ba.styleme.ui.adapters.SliderAdapterExample;
import com.ba.styleme.ui.activities.base.BaseActivity;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.smarteist.autoimageslider.IndicatorAnimations;
import com.smarteist.autoimageslider.SliderView;

import java.util.ArrayList;

import static com.ba.styleme.data.network.ApiConstant.adminUserId;

public class HomeActivity extends BaseActivity {
    ArrayList<RecomendationModel> recomendationModels = new ArrayList<>();
    SliderAdapterExample adapter=null;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        setTitle(getResourceString(R.string.home));
        setSlidingPane();

        SliderView sliderView = findViewById(R.id.imageSlider);

        adapter = new SliderAdapterExample(context,recomendationModels);

        sliderView.setSliderAdapter(adapter);

        sliderView.setIndicatorAnimation(IndicatorAnimations.SWAP); //set indicator animation by using SliderLayout.IndicatorAnimations. :WORM or THIN_WORM or COLOR or DROP or FILL or NONE or SCALE or SCALE_DOWN or SLIDE and SWAP!!
        /*sliderView.setSliderTransformAnimation(SliderAnimations.SIMPLETRANSFORMATION);
        sliderView.setAutoCycleDirection(SliderView.AUTO_CYCLE_DIRECTION_BACK_AND_FORTH);*/
       /* sliderView.setIndicatorSelectedColor(Color.WHITE);
        sliderView.setIndicatorUnselectedColor(Color.GRAY);*/
       /* sliderView.setScrollTimeInSec(4); //set scroll delay in seconds :*/

    }

    @Override
    public void onResume(){
        super.onResume();
        getData();
    }


    public void getData(){
        showLoading();
        final CollectionReference events = db.collection("admin_event_detail");
        Log.e("user_id", adminUserId);
        events.whereEqualTo("user_id", adminUserId).limit(5).get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
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

                            adapter.notifyDataSetChanged();
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

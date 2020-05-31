package com.ba.styleme.ui.activities.home.notification;

import android.os.Bundle;
import android.view.View;

import androidx.recyclerview.widget.RecyclerView;
import com.ba.styleme.R;
import com.ba.styleme.ui.adapters.NotificationListAdapter;
import com.ba.styleme.data.network.model.NotificationListModel;
import com.ba.styleme.ui.activities.base.BaseActivity;

import java.util.ArrayList;

public class NotificationActivity extends BaseActivity {
    RecyclerView rv_list;
    ArrayList<NotificationListModel> notificationListModels = new ArrayList<>();
    NotificationListAdapter notificationListAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);
        setTitle(getResourceString(R.string.notification));
        setSlidingPane();

        rv_list = findViewById(R.id.rv_list);
        notificationListModels=appPreference.getNotificationList();
        if(notificationListModels==null){
            notificationListModels=new ArrayList<>();
        }
        notificationListAdapter = new NotificationListAdapter(notificationListModels);
        rv_list.setAdapter(notificationListAdapter);

        showHideNoRecordFound(notificationListModels.size());

    }




}

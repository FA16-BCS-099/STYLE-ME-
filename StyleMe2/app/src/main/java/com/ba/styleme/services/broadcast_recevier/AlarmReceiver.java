package com.ba.styleme.services.broadcast_recevier;

import android.app.Notification;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.ba.styleme.data.network.model.NotificationListModel;
import com.ba.styleme.data.prefs.AppPreference;
import com.ba.styleme.ui.activities.home.HomeActivity;
import com.ba.styleme.ui.activities.home.notification.NotificationActivity;
import com.ba.styleme.utils.ParcelableUtil;
import com.ba.styleme.utils.Utils;

import java.util.ArrayList;

public class AlarmReceiver extends BroadcastReceiver {

    String TAG = "AlarmReceiver";

    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction() != null && context != null) {

            if (intent.getAction().equalsIgnoreCase(Intent.ACTION_BOOT_COMPLETED)) {
                // Set the alarm here.
                Log.d(TAG, "onReceive: BOOT_COMPLETED");
                /*LocalData localData = new LocalData(context);
                Utils.setReminder(context, AlarmReceiver.class,
                        localData.get_hour(), localData.get_min());*/
                return;
            }
        }

        Log.e(TAG, "onReceive: NotificationRecived");
        String action = intent.getAction();


        Log.e("notificationAction", action);
        NotificationListModel notificationListModel = new NotificationListModel(intent.getStringExtra("event_name"),
                intent.getStringExtra("event_date"),
                intent.getStringExtra("event_desc")
        );//Trigger the notification
        AppPreference appPreference = new AppPreference(context);
        ArrayList<NotificationListModel> notificationListModels = appPreference.getNotificationList();
        if (notificationListModels == null) {
            notificationListModels = new ArrayList<>();
        }
        notificationListModels.add(notificationListModel);
        appPreference.setNotificationList(notificationListModels);
        //if (notificationListModel != null)
        Utils.showNotification(context, NotificationActivity.class,
                notificationListModel.getEvent() + "", notificationListModel.getDress() + "");

    }
}
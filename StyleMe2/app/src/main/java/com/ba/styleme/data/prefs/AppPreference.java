package com.ba.styleme.data.prefs;


import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.ba.styleme.R;
import com.ba.styleme.data.network.model.NotificationListModel;
import com.ba.styleme.data.network.model.UserDetailModel;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.preference.PowerPreference;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collections;


/**
 * Created by ahtisham.hassan
 */

public class AppPreference {

    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    Context context;

    // Constructor
    public AppPreference(Context context) {
        this.context = context;
        sharedPreferences = context.getSharedPreferences(context.getString(R.string.app_name), Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
    }


    // Setting app language
    public void setIsLogin(Boolean isLogin) {
        editor.putBoolean("isLogin", isLogin);
        editor.apply();
        editor.commit();
    }

    // If there will null language store in shared prefrences then english will be return
    public Boolean getIsLogin() {
        return sharedPreferences.getBoolean("isLogin", false);
    }

    public void logoutUser() {

        setIsLogin(false);
        clearUserObject();

    }


    // UserObject
    public void setUserObject(UserDetailModel userObject) {

        PowerPreference.getDefaultFile().putObject("userObject", userObject);
    }

    public UserDetailModel getUserObject() {
        UserDetailModel userObject = PowerPreference.getDefaultFile().getObject("userObject", UserDetailModel.class);
        return userObject;
    }

    // NotificationObject
    public void setNotificationList(ArrayList<NotificationListModel> notificationList) {
        //  PowerPreference.getDefaultFile().putObject("notificationList", notificationList);
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = prefs.edit();
        Gson gson = new Gson();
        String json = gson.toJson(notificationList);
        editor.putString("notificationList", json);
        editor.apply();     // This line is IMPORTANT !!!
    }

    public ArrayList<NotificationListModel> getNotificationList() {
        //  ArrayList<NotificationListModel> notificationList =  PowerPreference.getDefaultFile().getObject("notificationList", NotificationListModel.class,new ArrayList<NotificationListModel>());
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        Gson gson = new Gson();
        String json = prefs.getString("notificationList", null);
        Type type = new TypeToken<ArrayList<NotificationListModel>>() {
        }.getType();
        if (json == null) {
            return null;
        }
        ArrayList<NotificationListModel> notificationListModels = gson.fromJson(json, type);
        Collections.reverse(notificationListModels);
        return notificationListModels;
    }


    public void clearUserObject() {
        PowerPreference.getDefaultFile().remove("userObject");
    }


    // Setting Biometric enable language
    public void setEmailAndPassword(String email, String password) {
        editor.putString("email", email);
        editor.putString("password", password);
        editor.apply();
        editor.commit();
    }

    // If there will null Biometric enable in shared prefrences then english will be return
    public String getEmail() {
        return sharedPreferences.getString("email", "");
    }

    // If there will null Biometric enable in shared prefrences then english will be return
    public String getPassword() {
        return sharedPreferences.getString("password", "");
    }

    // Setting user id
    public void setUserId(String userId) {
        editor.putString("userId", userId);
        editor.apply();
        editor.commit();
    }

    public String getUserId() {
        return sharedPreferences.getString("userId", "");
    }
}
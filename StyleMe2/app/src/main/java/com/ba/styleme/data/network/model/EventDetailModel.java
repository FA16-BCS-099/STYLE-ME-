package com.ba.styleme.data.network.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;
import com.google.firebase.Timestamp;

public class EventDetailModel implements Parcelable {

    String user_id,imageUrl,event_name,event_description,date,event_type,event_out_fit_type,weather;
    Timestamp event_date;

    private EventDetailModel(Parcel in) {
        user_id = in.readString();
        imageUrl = in.readString();
        event_name = in.readString();
        event_description = in.readString();
        date = in.readString();
        event_type = in.readString();
        event_out_fit_type = in.readString();
        event_date= (Timestamp) in.readValue(Timestamp.class.getClassLoader());
        weather=in.readString();
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {

        parcel.writeString(user_id);
        parcel.writeString(imageUrl);
        parcel.writeString(event_name);
        parcel.writeString(event_description);
        parcel.writeString(date);
        parcel.writeString(event_type);
        parcel.writeString(event_out_fit_type);
        parcel.writeValue(event_date);
        parcel.writeString(weather);
    }


    public EventDetailModel() {
    }

    public EventDetailModel(String user_id, String imageUrl, String event_name, String event_description, String date, Timestamp event_date, String event_type, String event_out_fit_type, String weather) {
        this.user_id = user_id;
        this.imageUrl = imageUrl;
        this.event_name = event_name;
        this.event_description = event_description;
        this.date = date;
        this.event_date = event_date;
        this.event_type = event_type;
        this.event_out_fit_type = event_out_fit_type;
        this.weather=weather;
    }

    public EventDetailModel(String user_id, String imageUrl, String event_name, String event_description, String date, Timestamp event_date, String event_type, String event_out_fit_type) {
        this.user_id = user_id;
        this.imageUrl = imageUrl;
        this.event_name = event_name;
        this.event_description = event_description;
        this.date = date;
        this.event_date = event_date;
        this.event_type = event_type;
        this.event_out_fit_type = event_out_fit_type;

    }
    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getEvent_type() {
        return event_type;
    }

    public void setEvent_type(String event_type) {
        this.event_type = event_type;
    }

    public String getEvent_out_fit_type() {
        return event_out_fit_type;
    }

    public void setEvent_out_fit_type(String event_out_fit_type) {
        this.event_out_fit_type = event_out_fit_type;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public Timestamp getEvent_date() {
        return event_date;
    }

    public void setEvent_date(Timestamp event_date) {
        this.event_date = event_date;
    }

    public String getEvent_name() {
        return event_name;
    }

    public void setEvent_name(String event_name) {
        this.event_name = event_name;
    }

    public String getEvent_description() {
        return event_description;
    }

    public void setEvent_description(String event_description) {
        this.event_description = event_description;
    }

    public String getWeather() {
        return weather;
    }

    public void setWeather(String weather) {
        this.weather = weather;
    }

    @Override
    public int describeContents() {
        return 0;
    }



    public static final Creator<EventDetailModel> CREATOR = new Creator<EventDetailModel>() {
        @Override
        public EventDetailModel createFromParcel(Parcel source) {
            return new EventDetailModel(source);
        }

        @Override
        public EventDetailModel[] newArray(int size) {
            return new EventDetailModel[size];
        }
    };

}

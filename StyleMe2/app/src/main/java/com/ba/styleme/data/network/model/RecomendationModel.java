package com.ba.styleme.data.network.model;

import java.io.Serializable;

public class RecomendationModel implements Serializable {

    String id,user_id,imageUrl,event_type,event_out_fit_type,weather;

    public RecomendationModel() {
    }

    public RecomendationModel(String id, String user_id, String imageUrl, String event_type, String event_out_fit_type,String weather) {
        this.id = id;
        this.user_id = user_id;
        this.imageUrl = imageUrl;
        this.event_type = event_type;
        this.event_out_fit_type = event_out_fit_type;
        this.weather = weather;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
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

    public String getWeather() {
        return weather;
    }

    public void setWeather(String weather) {
        this.weather = weather;
    }
}

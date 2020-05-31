package com.ba.styleme.data.network.model;

import java.io.Serializable;

public class CommunityListModel implements Serializable {

    String user_id,imageUrl,pro_name,pro_texture,pro_worth,pro_exchange_for,contact_no,location;

    boolean is_available=false;

    public CommunityListModel() {
    }

    public CommunityListModel(String imageUrl, String pro_name, String pro_texture, String pro_worth, String pro_exchange_for, String contact_no, boolean is_available ,String location) {
        this.imageUrl = imageUrl;
        this.pro_name = pro_name;
        this.pro_texture = pro_texture;
        this.pro_worth = pro_worth;
        this.pro_exchange_for = pro_exchange_for;
        this.contact_no = contact_no;
        this.is_available = is_available;
        this.location=location;


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

    public String getPro_name() {
        return pro_name;
    }

    public void setPro_name(String pro_name) {
        this.pro_name = pro_name;
    }

    public String getPro_texture() {
        return pro_texture;
    }

    public void setPro_texture(String pro_texture) {
        this.pro_texture = pro_texture;
    }

    public String getPro_worth() {
        return pro_worth;
    }

    public void setPro_worth(String pro_worth) {
        this.pro_worth = pro_worth;
    }

    public String getPro_exchange_for() {
        return pro_exchange_for;
    }

    public void setPro_exchange_for(String pro_exchange_for) {
        this.pro_exchange_for = pro_exchange_for;
    }

    public String getContact_no() {
        return contact_no;
    }

    public void setContact_no(String contact_no) {
        this.contact_no = contact_no;
    }

    public boolean isIs_available() {
        return is_available;
    }

    public void setIs_available(boolean is_available) {
        this.is_available = is_available;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }
}

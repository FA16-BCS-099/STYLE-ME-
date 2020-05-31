package com.ba.styleme.data.network.model;

import java.io.Serializable;

public class WardrobeListModel implements Serializable {

    String imageUrl,pro_name;

    public WardrobeListModel(String imageUrl, String pro_name) {
        this.imageUrl = imageUrl;
        this.pro_name = pro_name;
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

}

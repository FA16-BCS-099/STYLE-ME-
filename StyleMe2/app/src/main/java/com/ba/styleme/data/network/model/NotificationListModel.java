package com.ba.styleme.data.network.model;

public class NotificationListModel  {

    String event, date, dress;



    public NotificationListModel(String event, String date, String dress) {
        this.event = event;
        this.date = date;
        this.dress = dress;
    }

    public String getEvent() {
        return event;
    }

    public void setEvent(String event) {
        this.event = event;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getDress() {
        return dress;
    }

    public void setDress(String dress) {
        this.dress = dress;
    }


}

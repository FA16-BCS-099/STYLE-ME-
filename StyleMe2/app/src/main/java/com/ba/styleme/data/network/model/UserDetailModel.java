package com.ba.styleme.data.network.model;


public class UserDetailModel {
    String user_id,user_name,user_email,user_dob,user_contactno, is_old_user;

    public UserDetailModel(String user_id, String user_name, String user_email, String user_dob, String user_contactno,String is_old_user) {
        this.user_id = user_id;
        this.user_name = user_name;
        this.user_email = user_email;
        this.user_dob = user_dob;
        this.user_contactno = user_contactno;
        this.is_old_user=is_old_user;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getUser_name() {
        return user_name;
    }

    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }

    public String getUser_email() {
        return user_email;
    }

    public void setUser_email(String user_email) {
        this.user_email = user_email;
    }

    public String getUser_dob() {
        return user_dob;
    }

    public void setUser_dob(String user_dob) {
        this.user_dob = user_dob;
    }

    public String getUser_contactno() {
        return user_contactno;
    }

    public void setUser_contactno(String user_contactno) {
        this.user_contactno = user_contactno;
    }

    public String getIs_old_user() {
        return is_old_user;
    }

    public void setIs_old_user(String is_old_user) {
        this.is_old_user = is_old_user;
    }
}

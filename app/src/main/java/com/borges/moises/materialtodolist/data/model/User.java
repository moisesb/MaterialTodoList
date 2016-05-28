package com.borges.moises.materialtodolist.data.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * Created by moises.anjos on 11/05/2016.
 */
public class User {

    private String uid;
    private String userName;
    private String email;
    private String imageUrl;

    @JsonIgnore
    public String getUid() {
        return uid;
    }

    @JsonIgnore
    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
}

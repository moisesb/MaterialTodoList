package com.borges.moises.materialtodolist.data.model;

/**
 * Created by moises.anjos on 11/05/2016.
 */
public class User {

    private String userName;
    private String email;
    private String imageUrl;

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

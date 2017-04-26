package com.knowme.knowmeapp.models;

import com.google.firebase.database.IgnoreExtraProperties;

/**
 * Created by blessochampion on 4/25/17.
 */

@IgnoreExtraProperties
public class UserProfile
{
    private String userId;
    private String fullName;
    private String username;
    private String email;

    public  UserProfile(){

    }

    public UserProfile(String userId, String fullName, String username, String email) {
        this.fullName = fullName;
        this.username = username;
        this.userId = userId;
        this.email = email;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}

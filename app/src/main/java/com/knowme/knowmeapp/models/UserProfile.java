package com.knowme.knowmeapp.models;

import com.google.firebase.database.IgnoreExtraProperties;

/**
 * Created by blessochampion on 4/25/17.
 */

@IgnoreExtraProperties
public class UserProfile
{
    private String fullName;
    private String username;

    public  UserProfile(){

    }

    public UserProfile(String fullName, String username) {
        this.fullName = fullName;
        this.username = username;
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

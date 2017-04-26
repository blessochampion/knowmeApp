package com.knowme.knowmeapp.preferences;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by blessochampion on 11/14/16.
 */
public class ProfilePrefs {

    private static final String PREFS = "settings";
    private static final String KEY_USERNAME = "username";

    private static ProfilePrefs sInstance;
    private Context context;
    private SharedPreferences prefs;


    private ProfilePrefs(Context c){
        sInstance = this;
        context = c;
        prefs = context.getSharedPreferences(PREFS, Context.MODE_PRIVATE);

    }

    public static  ProfilePrefs getInstance(Context AppC){

        return sInstance == null? new ProfilePrefs(AppC): sInstance;
    }


    public String getUsername(){
        return prefs.getString(KEY_USERNAME, "");
    }


    public void setUsername(String  username){
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(KEY_USERNAME, username);
        editor.commit();
    }
}

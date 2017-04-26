package com.knowme.knowmeapp.interfaces;

import com.google.firebase.database.DatabaseReference;

/**
 * Created by blessochampion on 4/25/17.
 */

public interface SettingsItemClickedListener {
    void logOutButtonClicked();
    DatabaseReference getDBRef();
    void setNewFolderListener(NewCreationListener listener);
}

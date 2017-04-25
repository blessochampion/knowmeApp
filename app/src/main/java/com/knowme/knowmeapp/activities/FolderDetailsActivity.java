package com.knowme.knowmeapp.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.knowme.knowmeapp.R;
import com.knowme.knowmeapp.adapters.ProfilesAdapter;
import com.knowme.knowmeapp.models.Folder;
import com.knowme.knowmeapp.models.Profile;

import java.util.ArrayList;
import java.util.List;

public class FolderDetailsActivity extends AppCompatActivity implements AdapterView.OnItemClickListener {
    GridView mProfilesGridView;
    TextView mErrorTextView;
    ProgressBar mLoadingIndicator;
    public static final String KEY_FOLDER = FolderDetailsActivity.class.getName()
            + ":folder";
    Folder folder;

    ProfilesAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_folder_details);

        String folderName = "";
        int noOfProfiles = 0;
        Intent intentThatStartedThisActivity = getIntent();
        if (intentThatStartedThisActivity != null) {
            if (intentThatStartedThisActivity.hasExtra(KEY_FOLDER)){
                folder =  intentThatStartedThisActivity.getParcelableExtra(KEY_FOLDER);
                folderName = folder.getName();
                noOfProfiles = folder.getNoOfProfilesInside();

            }
        }

        mProfilesGridView = (GridView) findViewById(R.id.gv_profiles);
        mErrorTextView = (TextView) findViewById(R.id.tv_error);
        mLoadingIndicator = (ProgressBar) findViewById(R.id.pb_loading_indicator);
        Toolbar toolbar = (Toolbar) findViewById(R.id.tb_folder_details);

        setSupportActionBar(toolbar);
        String actionBarTitle = folderName;
        getSupportActionBar().setTitle(actionBarTitle);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);



        List<Profile> profiles = getProfiles(folderName, noOfProfiles);
        Context context = this;
        adapter = new ProfilesAdapter(context, profiles);
        mProfilesGridView.setAdapter(adapter);
        mProfilesGridView.setOnItemClickListener(this);
        mLoadingIndicator.setVisibility(View.INVISIBLE);
        showData();

    }

    private void showData() {
        mProfilesGridView.setVisibility(View.VISIBLE);
        mErrorTextView.setVisibility(View.INVISIBLE);

    }

    private void showErrorMessage(String message) {
        mProfilesGridView.setVisibility(View.INVISIBLE);
        mErrorTextView.setVisibility(View.VISIBLE);

        mErrorTextView.setText(message);
    }

    private List<Profile> getProfiles(String name, int noOfProfiles) {

        ArrayList<Profile> profiles = new ArrayList<>();
        Profile profile;
        for (int i = 0; i < noOfProfiles; i++) {
            profile = new Profile(name, R.drawable.request);
            profiles.add(profile);
        }

        return profiles;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int idOfItemClicked = item.getItemId();
        if(idOfItemClicked ==  android.R.id.home){
            onBackPressed();
            return  true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Profile selectedProfile = adapter.getItem(position);

        Context context = this;
        Class componentToStart = ProfileDetailsActivity.class;
        Intent profileDetailsIntent = new Intent(context, componentToStart);
        profileDetailsIntent.putExtra(ProfileDetailsActivity.KEY_PROFIILE, selectedProfile);
        startActivity(profileDetailsIntent);
    }
}

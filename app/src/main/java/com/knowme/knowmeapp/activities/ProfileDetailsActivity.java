package com.knowme.knowmeapp.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.knowme.knowmeapp.R;
import com.knowme.knowmeapp.models.Profile;

public class ProfileDetailsActivity extends AppCompatActivity {
    private ImageView mBusinessCardImageView;
    private TextView mFullNameTextView;
    private Profile mProfile;

    public static final String KEY_PROFIILE = "Profile";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_details);

        Intent intentThatStartedThisActivity = getIntent();
        if(intentThatStartedThisActivity != null && intentThatStartedThisActivity.hasExtra(KEY_PROFIILE)){
            mProfile = intentThatStartedThisActivity.getParcelableExtra(KEY_PROFIILE);
        }else {
            return;
        }

        mBusinessCardImageView = (ImageView) findViewById(R.id.iv_card);
        mFullNameTextView = (TextView) findViewById(R.id.tv_full_name);

        int url = mProfile.getImageURL();
        mBusinessCardImageView.setImageResource(url);

        String fullName = mProfile.getName();
        mFullNameTextView.setText(fullName);

        Toolbar toolbar = (Toolbar) findViewById(R.id.tb_folder_details);

        setSupportActionBar(toolbar);
        String actionBarTitle = getString(R.string.details);
        getSupportActionBar().setTitle(actionBarTitle);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);



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
}

package com.knowme.knowmeapp.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.knowme.knowmeapp.R;
import com.knowme.knowmeapp.models.Profile;

public class ProfileDetailsActivity extends AppCompatActivity {
    private ImageView mBusinessCardImageView;
    private TextView mFullNameTextView;
    private TextView mNotesContentTextView;
    private Profile mProfile;
    private EditText mNotesEditText;
    private Button mAddNotesButton;

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
        mNotesContentTextView = (TextView) findViewById(R.id.tv_notes_content);
        mNotesEditText = (EditText) findViewById(R.id.et_notes);
        mAddNotesButton = (Button) findViewById(R.id.bt_add_notes);
        mAddNotesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String userInput = mNotesEditText.getText().toString().trim();
                mNotesContentTextView.append(userInput);
            }
        });


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

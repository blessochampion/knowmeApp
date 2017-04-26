package com.knowme.knowmeapp.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
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
    List<Profile> profiles;

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


        profiles = getProfiles(folderName, noOfProfiles);
        Context context = this;
        adapter = new ProfilesAdapter(context, profiles);
        mProfilesGridView.setAdapter(adapter);
        mProfilesGridView.setOnItemClickListener(this);
        mLoadingIndicator.setVisibility(View.INVISIBLE);
        if (noOfProfiles == 0) {
            showErrorMessage("No Profiles here yet :)");
        } else {
            showData();
        }

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
        } else if (idOfItemClicked == R.id.action_add_business_card) {
            showDialog();
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.folder_details_menu, menu);

        return true;
    }

    private void showDialog() {
        LayoutInflater layoutInflater = LayoutInflater.from(FolderDetailsActivity.this);
        View newFolderView = layoutInflater.inflate(R.layout.create_folder, null);
        TextView title = (TextView) newFolderView.findViewById(R.id.title);
        final EditText gotoEdittext = (EditText) newFolderView.findViewById(R.id.page_no_edit_text);
        final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(FolderDetailsActivity.this);

        alertDialogBuilder.setView(newFolderView);
        final AlertDialog alertD = alertDialogBuilder.create();
        title.setText("Enter Username");

        final Button cancelButton = (Button) newFolderView.findViewById(R.id.dialog_cancel);
        cancelButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                alertD.dismiss();

            }

        });

        final Button okButton = (Button) newFolderView.findViewById(R.id.dialog_ok);
        okButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertD.dismiss();
                if (gotoEdittext.getText().toString().trim().isEmpty())
                    profiles.add(new Profile("empty", R.drawable.request));
                else
                    profiles.add(new Profile(gotoEdittext.getText().toString().trim(), R.drawable.request));

                adapter.notifyDataSetChanged();

            }
        });

        alertD.show();
    }

}

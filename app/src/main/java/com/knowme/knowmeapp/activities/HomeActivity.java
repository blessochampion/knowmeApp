package com.knowme.knowmeapp.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.knowme.knowmeapp.R;
import com.knowme.knowmeapp.adapters.FoldersAdapter;
import com.knowme.knowmeapp.data.UserProfileContract;
import com.knowme.knowmeapp.models.Folder;
import com.knowme.knowmeapp.models.UserProfile;

import java.util.ArrayList;
import java.util.List;

public class HomeActivity extends AppCompatActivity implements AdapterView.OnItemClickListener, NavigationView.OnNavigationItemSelectedListener {
    GridView mFoldersGridView;
    TextView mErrorTextView;
    ProgressBar mLoadingIndicator;
    FoldersAdapter adapter;

    //FireBase
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private DatabaseReference mDatabase;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //FireBase
        mDatabase = FirebaseDatabase.getInstance().getReference();

        mAuth = FirebaseAuth.getInstance();

        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                } else {
                    //user is signed out
                    logout();
                }

            }
        };

        setContentView(R.layout.activity_home);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        String actionBarTitle = getString(R.string.app_name);
        getSupportActionBar().setTitle(actionBarTitle);

        /*Activity specific*/
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();


        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });



        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        //set email and fullname
        View navigationV =  (LayoutInflater.from(this).inflate(R.layout.nav_header_drawer, navigationView));
       TextView emailTextView = (TextView) navigationV.findViewById(R.id.tv_email);

        String email = mAuth.getCurrentUser().getEmail();
        emailTextView.setText(email);

        final TextView fullName = (TextView) navigationV.findViewById(R.id.tv_full_name);

        final String userId = mAuth.getCurrentUser().getUid();
        mDatabase.child(UserProfileContract.TABLE_NAME).child(userId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                UserProfile userProfile = dataSnapshot.getValue(UserProfile.class);
                String fullname = userProfile.getFullName();
                fullName.setText(fullname);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


        mFoldersGridView = (GridView) findViewById(R.id.gv_folders);
        mErrorTextView = (TextView) findViewById(R.id.tv_error);
        mLoadingIndicator = (ProgressBar) findViewById(R.id.pb_loading_indicator);
        List<Folder> folders = getFolders();


        Context context = this;
        adapter = new FoldersAdapter(context, folders);
        mFoldersGridView.setAdapter(adapter);
        mLoadingIndicator.setVisibility(View.INVISIBLE);
        mFoldersGridView.setOnItemClickListener(this);
        showData();

    }

    private void logout() {
        Context context = this;
        Class componentToStart = LoginActivity.class;
        Intent intent = new Intent(context, componentToStart);
        startActivity(intent);

    }

    @Override
    protected void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (mAuthListener != null) {
            mAuth.removeAuthStateListener(mAuthListener);
        }
    }


    private void showData() {
        mFoldersGridView.setVisibility(View.VISIBLE);
        mErrorTextView.setVisibility(View.INVISIBLE);

    }

    private void showErrorMessage(String message) {
        mFoldersGridView.setVisibility(View.INVISIBLE);
        mErrorTextView.setVisibility(View.VISIBLE);

        mErrorTextView.setText(message);
    }

    public List<Folder> getFolders() {
        ArrayList<Folder> folders = new ArrayList<>();
        folders.add(new Folder(1, "Whatsapp", 20));
        folders.add(new Folder(2, "2go", 10));
        folders.add(new Folder(4, "Google", 4));
        folders.add(new Folder(7, "Facebook", 8));
        return folders;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Folder folder = adapter.getItem(position);
        startFolderDetailsActivity(folder);

    }

    private void startFolderDetailsActivity(Folder folder) {
        Context context = this;
        Class component = FolderDetailsActivity.class;
        Intent folderDetailsIntent = new Intent(context, component);
        folderDetailsIntent.putExtra(FolderDetailsActivity.KEY_FOLDER, folder);
        startActivity(folderDetailsIntent);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }


    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}

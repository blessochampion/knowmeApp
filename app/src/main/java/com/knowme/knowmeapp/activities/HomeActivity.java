package com.knowme.knowmeapp.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.knowme.knowmeapp.R;
import com.knowme.knowmeapp.data.UserProfileContract;
import com.knowme.knowmeapp.fragments.HomeFragment;
import com.knowme.knowmeapp.fragments.SettingsFragment;
import com.knowme.knowmeapp.interfaces.NewCreationListener;
import com.knowme.knowmeapp.interfaces.SettingsItemClickedListener;
import com.knowme.knowmeapp.models.Folder;
import com.knowme.knowmeapp.models.UserProfile;
import com.knowme.knowmeapp.preferences.ProfilePrefs;

public class HomeActivity extends AppCompatActivity implements
        NavigationView.OnNavigationItemSelectedListener, SettingsItemClickedListener {


    //FireBase
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private DatabaseReference mDatabase;
    FloatingActionButton mFAB;
    NewCreationListener listener;

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


        mFAB = (FloatingActionButton) findViewById(R.id.fab);
        mFAB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDialog();
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

        final String username = ProfilePrefs.getInstance(getApplicationContext()).getUsername();

        mDatabase.child(UserProfileContract.TABLE_NAME).child(username).addListenerForSingleValueEvent(new ValueEventListener() {

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

        startHomeFragment();

    }

    private void showDialog() {
        LayoutInflater layoutInflater = LayoutInflater.from(HomeActivity.this);
        View newFolderView = layoutInflater.inflate(R.layout.create_folder, null);
        TextView title = (TextView) newFolderView.findViewById(R.id.title);
        final EditText gotoEdittext = (EditText) newFolderView.findViewById(R.id.page_no_edit_text);
        final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(HomeActivity.this);

        alertDialogBuilder.setView(newFolderView);
        final AlertDialog alertD = alertDialogBuilder.create();
        title.setText("Enter Folder Name");

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
                    saveNewFolderToServer("empty");
                else
                    saveNewFolderToServer(gotoEdittext.getText().toString().trim());

            }
        });

        alertD.show();
    }

    private void saveNewFolderToServer(String trim) {
        final String username = ProfilePrefs.getInstance(getApplicationContext()).getUsername();
        mDatabase.child(UserProfileContract.COLUMN_FOLDER)
                .child(username).push().setValue(new Folder(trim));
        listener.deliverFolder(new Folder(trim));
    }

    private void startHomeFragment() {

        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fl_container, new HomeFragment());
        transaction.commit();
        mFAB.setVisibility(View.VISIBLE);

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

    @Override
    public void onBackPressed() {

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else if (!backStackIsEmpty()) {
            popBackStack();
            if (getSupportFragmentManager().getBackStackEntryCount() == 0) {
                mFAB.setVisibility(View.VISIBLE);
            }
            return;
        } else {
            super.onBackPressed();
        }
    }

    private void popBackStack() {
        getSupportFragmentManager().popBackStack();
    }

    private boolean backStackIsEmpty() {
        return getSupportFragmentManager().getBackStackEntryCount() > 0;
    }


    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {

        int idOfSelectedItem = item.getItemId();

        if (idOfSelectedItem == R.id.nav_home) {
            startHomeFragment();
        } else if (idOfSelectedItem == R.id.nav_settings) {
            startSettingsFragment();
        } else if (idOfSelectedItem == R.id.nav_tags) {
            startHomeFragment();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);


        return true;
    }

    private void startSettingsFragment() {
        String title = getString(R.string.settings);
        setActionBarTitle(title);
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fl_container, new SettingsFragment());
        transaction.addToBackStack(title);
        transaction.commit();
        mFAB.setVisibility(View.INVISIBLE);
    }

    @Override
    public void logOutButtonClicked() {
        mAuth.signOut();
    }

    @Override
    public DatabaseReference getDBRef() {
        return mDatabase;
    }

    @Override
    public void setNewFolderListener(NewCreationListener listener) {
        this.listener = listener;
    }


    public void setActionBarTitle(String actionBarTitle) {
        getSupportActionBar().setTitle(actionBarTitle);
        ;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.home_menu, menu);
        return  true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int idOfSelectedItem = item.getItemId();
        if (idOfSelectedItem == R.id.settings){
            startSettingsFragment();
            return  true;
        }
        return  super.onOptionsItemSelected(item);
    }
}

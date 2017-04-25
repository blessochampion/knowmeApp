package com.knowme.knowmeapp.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.knowme.knowmeapp.R;
import com.knowme.knowmeapp.adapters.FoldersAdapter;
import com.knowme.knowmeapp.models.Folder;

import java.util.ArrayList;
import java.util.List;

public class HomeActivity extends AppCompatActivity implements AdapterView.OnItemClickListener, NavigationView.OnNavigationItemSelectedListener {
    GridView mFoldersGridView;
    TextView mErrorTextView;
    ProgressBar mLoadingIndicator;
    FoldersAdapter adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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

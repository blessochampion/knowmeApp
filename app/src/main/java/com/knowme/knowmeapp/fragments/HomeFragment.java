package com.knowme.knowmeapp.fragments;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.knowme.knowmeapp.R;
import com.knowme.knowmeapp.activities.FolderDetailsActivity;
import com.knowme.knowmeapp.adapters.FoldersAdapter;
import com.knowme.knowmeapp.data.UserProfileContract;
import com.knowme.knowmeapp.interfaces.NewCreationListener;
import com.knowme.knowmeapp.interfaces.SettingsItemClickedListener;
import com.knowme.knowmeapp.models.Folder;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class HomeFragment extends Fragment implements AdapterView.OnItemClickListener,
        NewCreationListener {

    GridView mFoldersGridView;
    TextView mErrorTextView;
    ProgressBar mLoadingIndicator;
    FoldersAdapter adapter;
    SettingsItemClickedListener listener;
    List<Folder> folders;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        listener = (SettingsItemClickedListener) context;
        listener.setNewFolderListener(this);
    }

    public HomeFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_home, container, false);
        mFoldersGridView = (GridView) v.findViewById(R.id.gv_folders);
        mErrorTextView = (TextView) v.findViewById(R.id.tv_error);
        mLoadingIndicator = (ProgressBar) v.findViewById(R.id.pb_loading_indicator);
        Context context = getActivity();
        folders = getFolders();
        adapter = new FoldersAdapter(context, folders);
        mFoldersGridView.setAdapter(adapter);
        mLoadingIndicator.setVisibility(View.INVISIBLE);
        mFoldersGridView.setOnItemClickListener(this);
        showData();
       // loadDataFromServer();
        return v;

    }

    void loadDataFromServer() {
        listener.getDBRef().child(UserProfileContract.COLUMN_FOLDER).addValueEventListener
                (new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {

                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                }
        );
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
        folders.add(new Folder(1, "Work", 20));
        folders.add(new Folder(2, "Colleagues", 10));
        folders.add(new Folder(4, "Clients", 4));
        folders.add(new Folder(7, "Leads", 5));
        folders.add(new Folder(4, "Defence", 6));
        folders.add(new Folder(7, "Conference", 9));
        return folders;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Folder folder = adapter.getItem(position);
        startFolderDetailsActivity(folder);

    }

    private void startFolderDetailsActivity(Folder folder) {
        Context context = getActivity();
        Class component = FolderDetailsActivity.class;
        Intent folderDetailsIntent = new Intent(context, component);
        folderDetailsIntent.putExtra(FolderDetailsActivity.KEY_FOLDER, folder);
        startActivity(folderDetailsIntent);
    }

    @Override
    public void deliverFolder(Folder folder) {
       folders.add(folder);
        adapter.notifyDataSetChanged();
    }
}

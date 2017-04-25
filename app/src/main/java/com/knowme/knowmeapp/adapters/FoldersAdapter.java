package com.knowme.knowmeapp.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.knowme.knowmeapp.R;
import com.knowme.knowmeapp.models.Folder;

import java.util.List;

/**
 * Created by blessochampion on 4/21/17.
 */

public class FoldersAdapter extends ArrayAdapter<Folder> {
    Context context;

    public FoldersAdapter(Context context, List<Folder> objects) {
        super(context, 0, objects);
        this.context = context;

    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        Folder currentFolder = getItem(position);

        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.folders_item, parent, false);
        }

        TextView  nameTextView = (TextView) convertView.findViewById(R.id.tv_name);
        TextView noOfProfilesTextView = (TextView) convertView.findViewById(R.id.tv_no_of_profiles);

        String name = currentFolder.getName();
        nameTextView.setText(name);

        String noOfProfiles = String.valueOf(currentFolder.getNoOfProfilesInside());
        noOfProfilesTextView.setText(noOfProfiles);



        return convertView;
    }
}

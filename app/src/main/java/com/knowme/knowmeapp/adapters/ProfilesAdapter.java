package com.knowme.knowmeapp.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.knowme.knowmeapp.R;
import com.knowme.knowmeapp.models.Profile;

import java.util.List;

/**
 * Created by blessochampion on 4/21/17.
 */

public class ProfilesAdapter extends ArrayAdapter<Profile> {
    Context context;

    public ProfilesAdapter(Context context, List<Profile> objects) {
        super(context, 0, objects);
        this.context =  context;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Profile currentProfile = getItem(position);

        if(convertView == null){
            convertView = LayoutInflater.from(context).inflate(R.layout.profiles_item, parent, false);
        }

        TextView nameTextView = (TextView) convertView.findViewById(R.id.tv_name);
        ImageView  profileImage = (ImageView) convertView.findViewById(R.id.iv_profile_image);

        String name = currentProfile.getName();
        nameTextView.setText(name);

        int profileImageDrawable = currentProfile.getImageURL();
        profileImage.setImageResource(profileImageDrawable);

        return convertView;
    }
}

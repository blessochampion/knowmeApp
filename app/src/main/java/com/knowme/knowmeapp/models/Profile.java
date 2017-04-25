package com.knowme.knowmeapp.models;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by blessochampion on 4/21/17.
 */

public class Profile implements Parcelable {
    String name;
    int ImageURL;

    public Profile(String name, int imageURL) {
        this.name = name;
        ImageURL = imageURL;
    }

    protected Profile(Parcel in) {
        name = in.readString();
        ImageURL = in.readInt();
    }

    public static final Creator<Profile> CREATOR = new Creator<Profile>() {
        @Override
        public Profile createFromParcel(Parcel in) {
            return new Profile(in);
        }

        @Override
        public Profile[] newArray(int size) {
            return new Profile[size];
        }
    };

    public String getName() {
        return name;
    }

    public int getImageURL() {
        return ImageURL;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeInt(ImageURL);
    }
}

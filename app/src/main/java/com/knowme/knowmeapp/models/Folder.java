package com.knowme.knowmeapp.models;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by blessochampion on 4/21/17.
 */

public class Folder implements Parcelable
{
    private int id;
    private String name;
    private int noOfProfilesInside;

    public Folder(int id, String name, int noOfProfilesInside) {
        this.id = id;
        this.name = name;
        this.noOfProfilesInside = noOfProfilesInside;
    }

    protected Folder(Parcel in) {
        id = in.readInt();
        name = in.readString();
        noOfProfilesInside = in.readInt();
    }

    public static final Creator<Folder> CREATOR = new Creator<Folder>() {
        @Override
        public Folder createFromParcel(Parcel in) {
            return new Folder(in);
        }

        @Override
        public Folder[] newArray(int size) {
            return new Folder[size];
        }
    };

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public int getNoOfProfilesInside() {
        return noOfProfilesInside;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(name);
        dest.writeInt(noOfProfilesInside);
    }
}

package com.example.healthdouglas;

import android.os.Parcel;
import android.os.Parcelable;

public class Hospital implements Parcelable {
    private String name;
    private String vicinity;

    public Hospital(String name, String vicinity) {
        this.name = name;
        this.vicinity = vicinity;
    }

    protected Hospital(Parcel in) {
        name = in.readString();
        vicinity = in.readString();
    }

    public static final Creator<Hospital> CREATOR = new Creator<Hospital>() {
        @Override
        public Hospital createFromParcel(Parcel in) {
            return new Hospital(in);
        }

        @Override
        public Hospital[] newArray(int size) {
            return new Hospital[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeString(vicinity);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getVicinity() {
        return vicinity;
    }

    public void setVicinity(String vicinity) {
        this.vicinity = vicinity;
    }

    @Override
    public String toString() {
        return "Hospital{" +
                "name='" + name + '\'' +
                ", vicinity='" + vicinity + '\'' +
                '}';
    }
}

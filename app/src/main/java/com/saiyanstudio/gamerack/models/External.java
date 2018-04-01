package com.saiyanstudio.gamerack.models;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by deekshith on 16-11-2017.
 */

public class External implements Parcelable {

    @SerializedName("steam")
    @Expose
    private String steam;

    public External() { }

    public String getSteam() {
        return steam;
    }

    public void setSteam(String steam) {
        this.steam = steam;
    }

    protected External(Parcel in) {
        steam = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(steam);
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<External> CREATOR = new Parcelable.Creator<External>() {
        @Override
        public External createFromParcel(Parcel in) {
            return new External(in);
        }

        @Override
        public External[] newArray(int size) {
            return new External[size];
        }
    };
}

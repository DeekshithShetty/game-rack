package com.saiyanstudio.gamerack.models;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by deekshith on 16-11-2017.
 */

public class ESRB implements Parcelable {

    @SerializedName("synopsis")
    @Expose
    private String synopsis;
    @SerializedName("rating")
    @Expose
    private int rating;

    public String getSynopsis() {
        return synopsis;
    }

    public void setSynopsis(String synopsis) {
        this.synopsis = synopsis;
    }

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }

    public ESRB() { }

    protected ESRB(Parcel in) {
        synopsis = in.readString();
        rating = in.readInt();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(synopsis);
        dest.writeInt(rating);
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<ESRB> CREATOR = new Parcelable.Creator<ESRB>() {
        @Override
        public ESRB createFromParcel(Parcel in) {
            return new ESRB(in);
        }

        @Override
        public ESRB[] newArray(int size) {
            return new ESRB[size];
        }
    };
}

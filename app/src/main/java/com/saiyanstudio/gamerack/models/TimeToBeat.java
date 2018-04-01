package com.saiyanstudio.gamerack.models;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by deekshith on 16-11-2017.
 */

public class TimeToBeat implements Parcelable {

    @SerializedName("hastly")
    @Expose
    private int hastly;
    @SerializedName("normally")
    @Expose
    private int normally;
    @SerializedName("completely")
    @Expose
    private int completely;

    public TimeToBeat() { }

    public int getHastly() {
        return hastly;
    }

    public void setHastly(int hastly) {
        this.hastly = hastly;
    }

    public int getNormally() {
        return normally;
    }

    public void setNormally(int normally) {
        this.normally = normally;
    }

    public int getCompletely() {
        return completely;
    }

    public void setCompletely(int completely) {
        this.completely = completely;
    }

    protected TimeToBeat(Parcel in) {
        hastly = in.readInt();
        normally = in.readInt();
        completely = in.readInt();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(hastly);
        dest.writeInt(normally);
        dest.writeInt(completely);
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<TimeToBeat> CREATOR = new Parcelable.Creator<TimeToBeat>() {
        @Override
        public TimeToBeat createFromParcel(Parcel in) {
            return new TimeToBeat(in);
        }

        @Override
        public TimeToBeat[] newArray(int size) {
            return new TimeToBeat[size];
        }
    };
}

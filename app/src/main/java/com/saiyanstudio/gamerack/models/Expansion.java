package com.saiyanstudio.gamerack.models;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by deekshith on 11-11-2017.
 */

public class Expansion implements Parcelable {

    private int id;
    private String name;
    private int baseGameId;
    private String baseGameName;
    private boolean isCompleted;

    public Expansion() { }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getBaseGameId() {
        return baseGameId;
    }

    public void setBaseGameId(int baseGameId) {
        this.baseGameId = baseGameId;
    }

    public String getBaseGameName() {
        return baseGameName;
    }

    public void setBaseGameName(String baseGameName) {
        this.baseGameName = baseGameName;
    }

    public boolean isCompleted() {
        return isCompleted;
    }

    public void setCompleted(boolean isCompleted) {
        this.isCompleted = isCompleted;
    }

    public Expansion(Parcel in) {
        id = in.readInt();
        name = in.readString();
        baseGameId = in.readInt();
        baseGameName = in.readString();
        isCompleted = in.readByte() != 0;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(name);
        dest.writeInt(baseGameId);
        dest.writeString(baseGameName);
        dest.writeByte((byte) (isCompleted ? 1 : 0));
    }

    // This is to de-serialize the object
    public static final Parcelable.Creator<Expansion> CREATOR = new Parcelable.Creator<Expansion>(){
        public Expansion createFromParcel(Parcel in) {
            return new Expansion(in);
        }
        public Expansion[] newArray(int size) {
            return new Expansion[size];
        }
    };
}

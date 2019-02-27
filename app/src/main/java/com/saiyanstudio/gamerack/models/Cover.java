package com.saiyanstudio.gamerack.models;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by deekshith on 16-11-2017.
 */

public class Cover implements Parcelable {

    @SerializedName("url")
    @Expose
    private String url;
    @SerializedName("image_id")
    @Expose
    private String imageId;
    @SerializedName("width")
    @Expose
    private int width;
    @SerializedName("height")
    @Expose
    private int height;

    public Cover() { }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getImageId() {
        return imageId;
    }

    public void setImageId(String imageId) {
        this.imageId = imageId;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }


    protected Cover(Parcel in) {
        url = in.readString();
        imageId = in.readString();
        width = in.readInt();
        height = in.readInt();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(url);
        dest.writeString(imageId);
        dest.writeInt(width);
        dest.writeInt(height);
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<Cover> CREATOR = new Parcelable.Creator<Cover>() {
        @Override
        public Cover createFromParcel(Parcel in) {
            return new Cover(in);
        }

        @Override
        public Cover[] newArray(int size) {
            return new Cover[size];
        }
    };
}

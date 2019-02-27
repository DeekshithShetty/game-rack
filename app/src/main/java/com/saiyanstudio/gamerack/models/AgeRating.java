package com.saiyanstudio.gamerack.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.saiyanstudio.gamerack.common.Constants;

/**
 * Created by deekshith on 27-02-2019.
 */

public class AgeRating {
    @SerializedName("category")
    @Expose
    private int category;
    @SerializedName("rating")
    @Expose
    private int rating;

    private String ratingName = null;

    public int getCategory() {
        return category;
    }

    public void setCategory(int category) {
        this.category = category;
    }

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }

    public String getRatingName() {
        if(ratingName != null) return ratingName;

        // TODO: Create a mapper for rating name and value
        switch (this.rating){
            case 1:
                ratingName = Constants.AgeRatingValue.Three;
                break;
            case 2:
                ratingName = Constants.AgeRatingValue.Seven;
                break;
            case 3:
                ratingName = Constants.AgeRatingValue.Twelve;
                break;
            case 4:
                ratingName = Constants.AgeRatingValue.Sixteen;
                break;
            case 5:
                ratingName = Constants.AgeRatingValue.Eighteen;
                break;
            case 6:
                ratingName = Constants.AgeRatingValue.RP;
                break;
            case 7:
                ratingName = Constants.AgeRatingValue.EC;
                break;
            case 8:
                ratingName = Constants.AgeRatingValue.E;
                break;
            case 9:
                ratingName = Constants.AgeRatingValue.E10;
                break;
            case 10:
                ratingName = Constants.AgeRatingValue.T;
                break;
            case 11:
                ratingName = Constants.AgeRatingValue.M;
                break;
            case 12:
                ratingName = Constants.AgeRatingValue.AO;
                break;
        }
        return ratingName;
    }

    public void setRatingName(String ratingName) {
        this.ratingName = ratingName;
    }
}

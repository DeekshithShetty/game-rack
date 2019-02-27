package com.saiyanstudio.gamerack.models;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.saiyanstudio.gamerack.common.Constants;
import com.saiyanstudio.gamerack.common.Utility;

import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.List;

public class Game implements Parcelable {

    @SerializedName("id")
    @Expose
    private int id;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("summary")
    @Expose
    private String summary;
    @SerializedName("rating")
    @Expose
    private double rating;
    @SerializedName("aggregated_rating")
    @Expose
    private double aggregatedRating;
    @SerializedName("total_rating")
    @Expose
    private double totalRating;
    @SerializedName("involved_companies")
    @Expose
    private List<Integer> developers = null;
    @SerializedName("game_engines")
    @Expose
    private List<Integer> gameEngines = null;
    @SerializedName("category")
    @Expose
    private int category;
    @SerializedName("time_to_beat")
    @Expose
    private TimeToBeat timeToBeat;
    @SerializedName("game_modes")
    @Expose
    private List<Integer> gameModes = null;
    @SerializedName("themes")
    @Expose
    private List<Integer> themes = null;
    @SerializedName("genres")
    @Expose
    private List<Integer> genres = null;
    @SerializedName("expansions")
    @Expose
    private List<Integer> expansions = null;
    @SerializedName("age_ratings")
    @Expose
    private List<AgeRating> ageRatings = null;
    @SerializedName("first_release_date")
    @Expose
    private long firstReleaseDate;
    @SerializedName("cover")
    @Expose
    private Cover cover;
    @SerializedName("websites")
    @Expose
    private List<Website> websites = null;
    @SerializedName("external")
    @Expose
    private External external;

    private String platform;
    private String store;
    private String status;
    private String steamRatings;
    private String metacriticRatings;
    private String ignRatings;
    private boolean isFavorite;
    private boolean isInLibrary;

    private List<Info> developersInfo = null;
    private List<Info> gameModesInfo = null;
    private List<Info> themesInfo = null;
    private List<Info> genresInfo = null;
    private List<Info> expansionsInfo = null;

    private String developerInfoString;
    private String genresAndTagsString;
    private List<Expansion> expansionList = null;
    private String esrb;
    private String pegi;

    public Game() { }

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

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public double getRating() {
        return rating;
    }

    public void setRating(double rating) {
        this.rating = rating;
    }

    public double getAggregatedRating() {
        return aggregatedRating;
    }

    public void setAggregatedRating(double aggregatedRating) {
        this.aggregatedRating = aggregatedRating;
    }

    public double getTotalRating() {
        return totalRating;
    }

    public void setTotalRating(double totalRating) {
        this.totalRating = totalRating;
    }

    public List<Integer> getDevelopers() {
        return developers;
    }

    public void setDevelopers(List<Integer> developers) {
        this.developers = developers;
    }

    public List<Integer> getGameEngines() {
        return gameEngines;
    }

    public void setGameEngines(List<Integer> gameEngines) {
        this.gameEngines = gameEngines;
    }

    public int getCategory() {
        return category;
    }

    public void setCategory(int category) {
        this.category = category;
    }

    public TimeToBeat getTimeToBeat() {
        return timeToBeat;
    }

    public void setTimeToBeat(TimeToBeat timeToBeat) {
        this.timeToBeat = timeToBeat;
    }

    public List<Integer> getGameModes() {
        return gameModes;
    }

    public void setGameModes(List<Integer> gameModes) {
        this.gameModes = gameModes;
    }

    public List<Integer> getThemes() {
        return themes;
    }

    public void setThemes(List<Integer> themes) {
        this.themes = themes;
    }

    public List<Integer> getGenres() {
        return genres;
    }

    public void setGenres(List<Integer> genres) {
        this.genres = genres;
    }

    public List<Integer> getExpansions() {
        return expansions;
    }

    public void setExpansions(List<Integer> expansions) {
        this.expansions = expansions;
    }

    public List<AgeRating> getAgeRatings() { return ageRatings; }

    public void setAgeRatings(List<AgeRating> ageRatings) { this.ageRatings = ageRatings; }

    public long getFirstReleaseDate() {
        return firstReleaseDate;
    }

    public void setFirstReleaseDate(long firstReleaseDate) {
        this.firstReleaseDate = firstReleaseDate;
    }

    public Cover getCover() {
        return cover;
    }

    public void setCover(Cover cover) {
        this.cover = cover;
    }

    public List<Website> getWebsites() {
        return websites;
    }

    public void setWebsites(List<Website> websites) {
        this.websites = websites;
    }

    public External getExternal() {
        return external;
    }

    public void setExternal(External external) {
        this.external = external;
    }

    // -------------------------------------------------------

    public String getPlatform() { return platform; }

    public void setPlatform(String platform) { this.platform = platform; }

    public String getStore() { return store; }

    public void setStore(String store) { this.store = store; }

    public String getStatus() { return status; }

    public void setStatus(String status) { this.status = status; }

    public String getSteamRatings() { return steamRatings; }

    public void setSteamRatings(String steamRatings) { this.steamRatings = steamRatings; }

    public String getMetacriticRatings() { return metacriticRatings; }

    public void setMetacriticRatings(String metacriticRatings) { this.metacriticRatings = metacriticRatings; }

    public String getIgnRatings() { return ignRatings; }

    public void setIgnRatings(String ignRatings) { this.ignRatings = ignRatings; }

    public boolean isFavorite() { return isFavorite; }

    public void setFavorite(boolean favorite) { isFavorite = favorite; }

    public boolean isInLibrary() { return isInLibrary; }

    public void setInLibrary(boolean inLibrary) { isInLibrary = inLibrary; }

    // -------------------------------------------------------

    public List<Info> getDevelopersInfo() {
        return developersInfo;
    }

    public void setDevelopersInfo(List<Info> developersInfo) {
        this.developersInfo = developersInfo;
    }

    public List<Info> getGameModesInfo() {
        return gameModesInfo;
    }

    public void setGameModesInfo(List<Info> gameModesInfo) {
        this.gameModesInfo = gameModesInfo;
    }

    public List<Info> getThemesInfo() {
        return themesInfo;
    }

    public void setThemesInfo(List<Info> themesInfo) {
        this.themesInfo = themesInfo;
    }

    public List<Info> getGenresInfo() {
        return genresInfo;
    }

    public void setGenresInfo(List<Info> genresInfo) {
        this.genresInfo = genresInfo;
    }

    public List<Info> getExpansionsInfo() {
        return expansionsInfo;
    }

    public void setExpansionsInfo(List<Info> expansionsInfo) {
        this.expansionsInfo = expansionsInfo;
    }

    public List<Expansion> getExpansionList() {
        if(expansionList != null) return expansionList;

        expansionList = new ArrayList<Expansion>();
        if(expansionsInfo != null){
            for (Info expansion: expansionsInfo) {
                Expansion dlc = new Expansion();
                dlc.setId(expansion.getId());
                dlc.setName(expansion.getName());
                dlc.setBaseGameId(this.getId());
                dlc.setBaseGameName(this.getName());
                dlc.setCompleted(false);
                expansionList.add(dlc);
            }
        }
        return expansionList;
    }

    public void setExpansionList(List<Expansion> expansionList) {
        this.expansionList = expansionList;
    }

    // -------------------------------------------------------

    public void setDeveloperInfoString(String developerInfoString) {
        this.developerInfoString = developerInfoString;
    }

    public String getDevelopersInfoString() {

        if(developerInfoString != null) return developerInfoString;

        if(developersInfo != null)
            return StringUtils.join(Utility.getNamePropertyList(developersInfo), ", ");
        else
            return "";
    }

    public void setGenresAndTagsString(String genresAndTagsString) {
        this.genresAndTagsString = genresAndTagsString;
    }

    public String getGenresAndTagsString() {

        if(genresAndTagsString != null) return genresAndTagsString;

        String finalJoinedString = "";
        String gameModesInfoAsString = null;
        String themesInfoAsString = null;
        String genresInfoAsString = null;

        if(gameModesInfo != null)
            gameModesInfoAsString = StringUtils.join(Utility.getNamePropertyList(gameModesInfo), ", ");

        if(themesInfo != null)
            themesInfoAsString = StringUtils.join(Utility.getNamePropertyList(themesInfo), ", ");

        if(genresInfo != null)
            genresInfoAsString = StringUtils.join(Utility.getNamePropertyList(genresInfo), ", ");

        if(gameModesInfoAsString != null)
            finalJoinedString += gameModesInfoAsString;

        if(themesInfoAsString != null) {
            if(gameModesInfoAsString != null) finalJoinedString += ", ";
            finalJoinedString += themesInfoAsString;
        }

        if(genresInfoAsString != null) {
            if(gameModesInfoAsString != null || themesInfoAsString != null) finalJoinedString += ", ";
            finalJoinedString += genresInfoAsString;
        }

        return finalJoinedString;
    }

    public void setEsrb(String esrb) { this.esrb = esrb; }

    public String getEsrb() {
        if(esrb != null) return esrb;

        if(getAgeRatings() != null) {
            for(AgeRating ageRating : getAgeRatings()) {
                if(ageRating.getCategory() == Constants.AgeRatingCategory.ESRB) {
                    esrb = ageRating.getRatingName();
                    return esrb;
                }
            }
        }
        return null;
    }

    public void setPegi(String pegi) { this.pegi = pegi; }

    public String getPegi() {
        if(pegi != null) return pegi;

        if(getAgeRatings() != null) {
            for(AgeRating ageRating : getAgeRatings()) {
                if(ageRating.getCategory() == Constants.AgeRatingCategory.PEGI) {
                    pegi = ageRating.getRatingName();
                    return pegi;
                }
            }
        }
        return null;
    }

    protected Game(Parcel in) {
        id = in.readInt();
        name = in.readString();
        summary = in.readString();
        rating = in.readDouble();
        aggregatedRating = in.readDouble();
        totalRating = in.readDouble();
        if (in.readByte() == 0x01) {
            developers = new ArrayList<Integer>();
            in.readList(developers, Integer.class.getClassLoader());
        } else {
            developers = null;
        }
        if (in.readByte() == 0x01) {
            gameEngines = new ArrayList<Integer>();
            in.readList(gameEngines, Integer.class.getClassLoader());
        } else {
            gameEngines = null;
        }
        category = in.readInt();
        timeToBeat = (TimeToBeat) in.readValue(TimeToBeat.class.getClassLoader());
        if (in.readByte() == 0x01) {
            gameModes = new ArrayList<Integer>();
            in.readList(gameModes, Integer.class.getClassLoader());
        } else {
            gameModes = null;
        }
        if (in.readByte() == 0x01) {
            themes = new ArrayList<Integer>();
            in.readList(themes, Integer.class.getClassLoader());
        } else {
            themes = null;
        }
        if (in.readByte() == 0x01) {
            genres = new ArrayList<Integer>();
            in.readList(genres, Integer.class.getClassLoader());
        } else {
            genres = null;
        }
        if (in.readByte() == 0x01) {
            expansions = new ArrayList<Integer>();
            in.readList(expansions, Integer.class.getClassLoader());
        } else {
            expansions = null;
        }
        firstReleaseDate = in.readLong();
        cover = (Cover) in.readValue(Cover.class.getClassLoader());
        esrb = in.readString();
        pegi = in.readString();
        if (in.readByte() == 0x01) {
            websites = new ArrayList<Website>();
            in.readList(websites, Website.class.getClassLoader());
        } else {
            websites = null;
        }
        external = (External) in.readValue(External.class.getClassLoader());
        platform = in.readString();
        store = in.readString();
        status = in.readString();
        steamRatings = in.readString();
        metacriticRatings = in.readString();
        ignRatings = in.readString();
        isFavorite = in.readByte() != 0x00;
        isInLibrary = in.readByte() != 0x00;
        if (in.readByte() == 0x01) {
            developersInfo = new ArrayList<Info>();
            in.readList(developersInfo, Info.class.getClassLoader());
        } else {
            developersInfo = null;
        }
        if (in.readByte() == 0x01) {
            gameModesInfo = new ArrayList<Info>();
            in.readList(gameModesInfo, Info.class.getClassLoader());
        } else {
            gameModesInfo = null;
        }
        if (in.readByte() == 0x01) {
            themesInfo = new ArrayList<Info>();
            in.readList(themesInfo, Info.class.getClassLoader());
        } else {
            themesInfo = null;
        }
        if (in.readByte() == 0x01) {
            genresInfo = new ArrayList<Info>();
            in.readList(genresInfo, Info.class.getClassLoader());
        } else {
            genresInfo = null;
        }
        if (in.readByte() == 0x01) {
            expansionsInfo = new ArrayList<Info>();
            in.readList(expansionsInfo, Info.class.getClassLoader());
        } else {
            expansionsInfo = null;
        }
        developerInfoString = in.readString();
        genresAndTagsString = in.readString();
        if (in.readByte() == 0x01) {
            expansionList = new ArrayList<Expansion>();
            in.readList(expansionList, Expansion.class.getClassLoader());
        } else {
            expansionList = null;
        }
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(name);
        dest.writeString(summary);
        dest.writeDouble(rating);
        dest.writeDouble(aggregatedRating);
        dest.writeDouble(totalRating);
        if (developers == null) {
            dest.writeByte((byte) (0x00));
        } else {
            dest.writeByte((byte) (0x01));
            dest.writeList(developers);
        }
        if (gameEngines == null) {
            dest.writeByte((byte) (0x00));
        } else {
            dest.writeByte((byte) (0x01));
            dest.writeList(gameEngines);
        }
        dest.writeInt(category);
        dest.writeValue(timeToBeat);
        if (gameModes == null) {
            dest.writeByte((byte) (0x00));
        } else {
            dest.writeByte((byte) (0x01));
            dest.writeList(gameModes);
        }
        if (themes == null) {
            dest.writeByte((byte) (0x00));
        } else {
            dest.writeByte((byte) (0x01));
            dest.writeList(themes);
        }
        if (genres == null) {
            dest.writeByte((byte) (0x00));
        } else {
            dest.writeByte((byte) (0x01));
            dest.writeList(genres);
        }
        if (expansions == null) {
            dest.writeByte((byte) (0x00));
        } else {
            dest.writeByte((byte) (0x01));
            dest.writeList(expansions);
        }
        dest.writeLong(firstReleaseDate);
        dest.writeValue(cover);
        dest.writeValue(esrb);
        dest.writeValue(pegi);
        if (websites == null) {
            dest.writeByte((byte) (0x00));
        } else {
            dest.writeByte((byte) (0x01));
            dest.writeList(websites);
        }
        dest.writeValue(external);
        dest.writeString(platform);
        dest.writeString(store);
        dest.writeString(status);
        dest.writeString(steamRatings);
        dest.writeString(metacriticRatings);
        dest.writeString(ignRatings);
        dest.writeByte((byte) (isFavorite ? 0x01 : 0x00));
        dest.writeByte((byte) (isInLibrary ? 0x01 : 0x00));
        if (developersInfo == null) {
            dest.writeByte((byte) (0x00));
        } else {
            dest.writeByte((byte) (0x01));
            dest.writeList(developersInfo);
        }
        if (gameModesInfo == null) {
            dest.writeByte((byte) (0x00));
        } else {
            dest.writeByte((byte) (0x01));
            dest.writeList(gameModesInfo);
        }
        if (themesInfo == null) {
            dest.writeByte((byte) (0x00));
        } else {
            dest.writeByte((byte) (0x01));
            dest.writeList(themesInfo);
        }
        if (genresInfo == null) {
            dest.writeByte((byte) (0x00));
        } else {
            dest.writeByte((byte) (0x01));
            dest.writeList(genresInfo);
        }
        if (expansionsInfo == null) {
            dest.writeByte((byte) (0x00));
        } else {
            dest.writeByte((byte) (0x01));
            dest.writeList(expansionsInfo);
        }
        dest.writeString(developerInfoString);
        dest.writeString(genresAndTagsString);
        if (expansionList == null) {
            dest.writeByte((byte) (0x00));
        } else {
            dest.writeByte((byte) (0x01));
            dest.writeList(expansionList);
        }
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<Game> CREATOR = new Parcelable.Creator<Game>() {
        @Override
        public Game createFromParcel(Parcel in) {
            return new Game(in);
        }

        @Override
        public Game[] newArray(int size) {
            return new Game[size];
        }
    };
}
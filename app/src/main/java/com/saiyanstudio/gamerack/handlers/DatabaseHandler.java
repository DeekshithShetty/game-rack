package com.saiyanstudio.gamerack.handlers;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.saiyanstudio.gamerack.common.Constants;
import com.saiyanstudio.gamerack.models.Cover;
import com.saiyanstudio.gamerack.models.Expansion;
import com.saiyanstudio.gamerack.models.Game;
import com.saiyanstudio.gamerack.models.TimeToBeat;
import com.saiyanstudio.gamerack.models.Website;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * Created by deekshith on 25-11-2017.
 */

public class DatabaseHandler extends SQLiteOpenHelper {

    // Database Version
    private static final int DATABASE_VERSION = 4;

    // Database Name
    private static final String DATABASE_NAME = "game_db";

    // Table names
    private static final String TABLE_GAMES = "games";
    private static final String TABLE_EXPANSIONS = "expansions";

    // Game Table Columns names
    private static final String KEY_ID = "id";
    private static final String KEY_NAME = "name";
    private static final String KEY_CREATED_AT = "created_at";
    private static final String KEY_IMAGE_ID = "image_id";
    private static final String KEY_PLATFORM = "platform";
    private static final String KEY_STORE = "store";
    private static final String KEY_STATUS = "status";
    private static final String KEY_SUMMARY = "summary";
    private static final String KEY_IS_FAVORITE = "is_favorite";
    private static final String KEY_IS_IN_LIBRARY = "is_in_library";
    private static final String KEY_RATING = "rating";
    private static final String KEY_AGGREGATED_RATING = "aggregated_rating";
    private static final String KEY_DEVELOPERS_INFO = "developers_info";
    private static final String KEY_RELEASE_DATE = "release_date";
    private static final String KEY_GENRES_AND_TAGS = "genres_and_tags";
    private static final String KEY_TIME_TO_BEAT = "time_to_beat";
    private static final String KEY_ESRB = "esrb";
    private static final String KEY_PEGI = "pegi";
    private static final String KEY_OFFICIAL_SITE_LINK = "official_site_link";
    private static final String KEY_STEAM_LINK = "steam_link";
    private static final String KEY_TWITCH_LINK = "twitch_link";
    private static final String KEY_WIKIA_LINK = "wikia_link";

    // Expansion Table Columns names
    private static final String KEY_EXPANSION_ID = "id";
    private static final String KEY_EXPANSION_NAME = "name";
    private static final String KEY_EXPANSION_IS_COMPLETED = "is_completed";
    private static final String KEY_BASE_GAME_ID = "base_game_id";
    private static final String KEY_BASE_GAME_NAME = "base_game_name";

    public DatabaseHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_GAMES_TABLE = "CREATE TABLE " + TABLE_GAMES + "("
                + KEY_ID + " INTEGER PRIMARY KEY,"
                + KEY_NAME + " TEXT,"
                + KEY_CREATED_AT + " DATETIME,"
                + KEY_IMAGE_ID + " TEXT,"
                + KEY_PLATFORM + " TEXT,"
                + KEY_STORE + " TEXT,"
                + KEY_STATUS + " TEXT,"
                + KEY_SUMMARY + " TEXT,"
                + KEY_IS_FAVORITE + " INTEGER,"
                + KEY_IS_IN_LIBRARY + " INTEGER,"
                + KEY_RATING + " REAL,"
                + KEY_AGGREGATED_RATING + " REAL,"
                + KEY_DEVELOPERS_INFO + " TEXT,"
                + KEY_RELEASE_DATE + " INTEGER,"
                + KEY_GENRES_AND_TAGS + " TEXT,"
                + KEY_TIME_TO_BEAT + " INTEGER,"
                + KEY_ESRB + " TEXT,"
                + KEY_PEGI + " TEXT,"
                + KEY_OFFICIAL_SITE_LINK + " TEXT,"
                + KEY_STEAM_LINK + " TEXT,"
                + KEY_TWITCH_LINK + " TEXT,"
                + KEY_WIKIA_LINK + " TEXT"
                + ")";

        String CREATE_EXPANSIONS_TABLE = "CREATE TABLE " + TABLE_EXPANSIONS + "("
                + KEY_EXPANSION_ID + " INTEGER PRIMARY KEY,"
                + KEY_EXPANSION_NAME + " TEXT,"
                + KEY_BASE_GAME_ID + " INTEGER,"
                + KEY_BASE_GAME_NAME + " TEXT,"
                + KEY_EXPANSION_IS_COMPLETED + " INTEGER"
                + ")";

        db.execSQL(CREATE_GAMES_TABLE);
        db.execSQL(CREATE_EXPANSIONS_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_GAMES);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_EXPANSIONS);

        // Create tables again
        onCreate(db);
    }

    public void purgeAllDataFromDb() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DELETE FROM " + TABLE_GAMES);
        db.execSQL("DELETE FROM " + TABLE_EXPANSIONS);
    }

    public void closeDB() {
        SQLiteDatabase db = this.getReadableDatabase();
        if (db != null && db.isOpen())
            db.close();
    }

    //region Game CRUD Operations

    public long addGame(Game game) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues gameValues = new ContentValues();

        gameValues.put(KEY_ID, game.getId());
        gameValues.put(KEY_NAME, game.getName());
        gameValues.put(KEY_CREATED_AT, getDateTime());
        gameValues.put(KEY_IMAGE_ID, (game.getCover() != null) ? game.getCover().getImageId() : null);
        gameValues.put(KEY_PLATFORM, game.getPlatform());
        gameValues.put(KEY_STORE, game.getStore());
        gameValues.put(KEY_STATUS, game.getStatus());
        gameValues.put(KEY_SUMMARY, game.getSummary());
        gameValues.put(KEY_IS_FAVORITE, game.isFavorite() ? 1 : 0);
        gameValues.put(KEY_IS_IN_LIBRARY, game.isInLibrary() ? 1 : 0);
        gameValues.put(KEY_RATING, game.getRating());
        gameValues.put(KEY_AGGREGATED_RATING, game.getAggregatedRating());
        gameValues.put(KEY_DEVELOPERS_INFO, game.getDevelopersInfoString());
        gameValues.put(KEY_RELEASE_DATE, game.getFirstReleaseDate());
        gameValues.put(KEY_GENRES_AND_TAGS, game.getGenresAndTagsString());
        gameValues.put(KEY_TIME_TO_BEAT, (game.getTimeToBeat() != null) ? game.getTimeToBeat().getNormally() : null);
        gameValues.put(KEY_ESRB, game.getEsrb());
        gameValues.put(KEY_PEGI, game.getPegi());

        List<Website> websiteList = game.getWebsites();
        if(websiteList != null && websiteList.size() > 0){

            for (final Website website: websiteList) {
                if(website.getCategory() == Constants.WebSiteCategory.OfficialSite){ // Official
                    gameValues.put(KEY_OFFICIAL_SITE_LINK, website.getUrl());
                } else if(website.getCategory() == Constants.WebSiteCategory.Steam) { // Steam
                    gameValues.put(KEY_STEAM_LINK, website.getUrl());
                } else if(website.getCategory() == Constants.WebSiteCategory.Twitch){ // Twitch
                    gameValues.put(KEY_TWITCH_LINK, website.getUrl());
                } else if(website.getCategory() == Constants.WebSiteCategory.Wikia){ // Wikia
                    gameValues.put(KEY_WIKIA_LINK, website.getUrl());
                }
            }
        }

        long id = db.insert(TABLE_GAMES, null, gameValues);

        List<Expansion> baseGameExpansionList = game.getExpansionList();
        if(baseGameExpansionList != null && baseGameExpansionList.size() > 0){
            for (Expansion expansion: baseGameExpansionList) {
                addExpansion(expansion);
            }
        }

        db.close();

        return id;
    }

    public Game getGame(int id) {
        SQLiteDatabase db = this.getReadableDatabase();

        String selectGameQuery = "SELECT  * FROM " + TABLE_GAMES
                + " WHERE "
                + KEY_ID + " = " + id;

        Cursor gameCursor = db.rawQuery(selectGameQuery, null);

        if (gameCursor != null && gameCursor.moveToFirst()){
            Game game = getGameFromCursor(gameCursor);
            game.setExpansionList(getExpansionsForBaseGame(game));

            return game;
        }
        else
            return null;


    }

    public List<Game> getAllGames() {
        List<Game> gameList = new ArrayList<Game>();
        String selectQuery = "SELECT  * FROM "
                + TABLE_GAMES
                + " ORDER BY "
                + KEY_CREATED_AT
                + " DESC";

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {

                Game game = getGameFromCursor(cursor);
                game.setExpansionList(getExpansionsForBaseGame(game));

                gameList.add(game);

            } while (cursor.moveToNext());
        }

        return gameList;
    }

    public List<Game> getAllGamesWithStatusFilter(String statusFilter) {
        List<Game> gameList = new ArrayList<Game>();
        String selectQuery = "SELECT  * FROM "
                + TABLE_GAMES
                + " WHERE "
                + KEY_STATUS + " = '" + statusFilter + "'"
                + " ORDER BY "
                + KEY_CREATED_AT
                + " DESC";

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {

                Game game = getGameFromCursor(cursor);
                game.setExpansionList(getExpansionsForBaseGame(game));

                gameList.add(game);

            } while (cursor.moveToNext());
        }

        return gameList;
    }

    public int getGamesCount() {
        String countQuery = "SELECT  * FROM " + TABLE_GAMES;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        cursor.close();

        return cursor.getCount();
    }

    public int updateGame(Game game) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_PLATFORM, game.getPlatform());
        values.put(KEY_STORE, game.getStore());
        values.put(KEY_STATUS, game.getStatus());
        values.put(KEY_IS_FAVORITE, game.isFavorite() ? 1 : 0);
        values.put(KEY_IS_IN_LIBRARY, game.isInLibrary() ? 1 : 0);

        return db.update(TABLE_GAMES, values, KEY_ID + " = ?",
                new String[] { String.valueOf(game.getId()) });
    }

    public void deleteGame(Game game) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_GAMES, KEY_ID + " = ?",
                new String[] { String.valueOf(game.getId()) });

        for (Expansion expansion: game.getExpansionList()) {
            deleteExpansion(expansion);
        }
    }

    //endregion

    //region Expansion CRUD Operations

    public long addExpansion(Expansion expansion) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues expansionValues = new ContentValues();

        expansionValues.put(KEY_EXPANSION_ID, expansion.getId());
        expansionValues.put(KEY_EXPANSION_NAME, expansion.getName());
        expansionValues.put(KEY_EXPANSION_IS_COMPLETED, expansion.isCompleted() ? 1 : 0);
        expansionValues.put(KEY_BASE_GAME_ID, expansion.getBaseGameId());
        expansionValues.put(KEY_BASE_GAME_NAME, expansion.getBaseGameName());

        long id = db.insert(TABLE_EXPANSIONS, null, expansionValues);

        db.close();

        return id;
    }

    public List<Expansion> getExpansionsForBaseGame(Game baseGame) {
        SQLiteDatabase db = this.getReadableDatabase();

        String selectExpansionQuery = "SELECT  * FROM " + TABLE_EXPANSIONS
                + " WHERE "
                + KEY_BASE_GAME_ID + " = " + baseGame.getId();

        Cursor expansionCursor = db.rawQuery(selectExpansionQuery, null);

        List<Expansion> expansionList = new ArrayList<>();

        if (expansionCursor.moveToFirst()) {
            do {
                Expansion expansion = getExpansionFromCursor(expansionCursor, baseGame.getId(), baseGame.getName());
                expansionList.add(expansion);
            } while (expansionCursor.moveToNext());
        }

        return expansionList;
    }

    public int updateExpansion(Expansion expansion) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_EXPANSION_IS_COMPLETED, expansion.isCompleted() ? 1 : 0);

        return db.update(TABLE_EXPANSIONS, values, KEY_EXPANSION_ID + " = ?",
                new String[] { String.valueOf(expansion.getId()) });
    }

    public void deleteExpansion(Expansion expansion) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_EXPANSIONS, KEY_EXPANSION_ID + " = ?",
                new String[] { String.valueOf(expansion.getId()) });
    }

    public int getExpansionsCount() {
        String countQuery = "SELECT  * FROM " + TABLE_EXPANSIONS;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        cursor.close();

        return cursor.getCount();
    }

    public int getExpansionsCountForBaseGame(Game baseGame) {
        String countQuery = "SELECT  * FROM " + TABLE_EXPANSIONS
                + " WHERE "
                + KEY_BASE_GAME_ID + " = " + baseGame.getId();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        cursor.close();

        return cursor.getCount();
    }

    //endregion

    //region Private Helpers

    private Game getGameFromCursor(Cursor c) {

        Game game = new Game();

        game.setId(c.getInt(c.getColumnIndex(KEY_ID)));
        game.setName(c.getString(c.getColumnIndex(KEY_NAME)));

        Cover cover = new Cover();
        cover.setImageId(c.getString(c.getColumnIndex(KEY_IMAGE_ID)));
        game.setCover(cover);

        game.setPlatform(c.getString(c.getColumnIndex(KEY_PLATFORM)));
        game.setStore(c.getString(c.getColumnIndex(KEY_STORE)));
        game.setStatus(c.getString(c.getColumnIndex(KEY_STATUS)));
        game.setSummary(c.getString(c.getColumnIndex(KEY_SUMMARY)));
        game.setFavorite(c.getInt(c.getColumnIndex(KEY_IS_FAVORITE)) == 1);
        game.setInLibrary(c.getInt(c.getColumnIndex(KEY_IS_IN_LIBRARY)) == 1);
        game.setRating(c.getDouble(c.getColumnIndex(KEY_RATING)));
        game.setAggregatedRating(c.getDouble(c.getColumnIndex(KEY_AGGREGATED_RATING)));
        game.setDeveloperInfoString(c.getString(c.getColumnIndex(KEY_DEVELOPERS_INFO)));
        game.setFirstReleaseDate(c.getLong(c.getColumnIndex(KEY_RELEASE_DATE)));
        game.setGenresAndTagsString(c.getString(c.getColumnIndex(KEY_GENRES_AND_TAGS)));

        TimeToBeat timeToBeat = new TimeToBeat();
        timeToBeat.setNormally(c.getInt(c.getColumnIndex(KEY_TIME_TO_BEAT)));
        game.setTimeToBeat(timeToBeat);

        game.setEsrb(c.getString(c.getColumnIndex(KEY_ESRB)));
        game.setPegi(c.getString(c.getColumnIndex(KEY_PEGI)));

        List<Website> websiteList = new ArrayList<>();

        String officialSiteLink = c.getString(c.getColumnIndex(KEY_OFFICIAL_SITE_LINK));
        if(officialSiteLink != null) {
            Website officialSiteWebsite = new Website();
            officialSiteWebsite.setCategory(Constants.WebSiteCategory.OfficialSite);
            officialSiteWebsite.setUrl(officialSiteLink);
            websiteList.add(officialSiteWebsite);
        }

        String steamLink = c.getString(c.getColumnIndex(KEY_STEAM_LINK));
        if(steamLink != null) {
            Website steamWebsite = new Website();
            steamWebsite.setCategory(Constants.WebSiteCategory.Steam);
            steamWebsite.setUrl(steamLink);
            websiteList.add(steamWebsite);
        }

        String twitchLink = c.getString(c.getColumnIndex(KEY_TWITCH_LINK));
        if(twitchLink != null) {
            Website twitchWebsite = new Website();
            twitchWebsite.setCategory(Constants.WebSiteCategory.Twitch);
            twitchWebsite.setUrl(twitchLink);
            websiteList.add(twitchWebsite);
        }

        String wikiaLink = c.getString(c.getColumnIndex(KEY_WIKIA_LINK));
        if(wikiaLink != null) {
            Website wikiaWebsite = new Website();
            wikiaWebsite.setCategory(Constants.WebSiteCategory.Wikia);
            wikiaWebsite.setUrl(wikiaLink);
            websiteList.add(wikiaWebsite);
        }

        game.setWebsites(websiteList);

        return game;
    }

    private Expansion getExpansionFromCursor(Cursor c, int baseGameId, String baseGameName) {

        Expansion expansion = new Expansion();

        expansion.setId(c.getInt(c.getColumnIndex(KEY_EXPANSION_ID)));
        expansion.setName(c.getString(c.getColumnIndex(KEY_EXPANSION_NAME)));
        expansion.setCompleted(c.getInt(c.getColumnIndex(KEY_EXPANSION_IS_COMPLETED)) == 1);
        expansion.setBaseGameId(baseGameId);
        expansion.setBaseGameName(baseGameName);

        return expansion;
    }

    private String getDateTime() {
        SimpleDateFormat dateFormat = new SimpleDateFormat(
                "yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        Date date = new Date();
        return dateFormat.format(date);
    }
    //endregion
}

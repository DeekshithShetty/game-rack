package com.saiyanstudio.gamerack.retrofit;

import com.saiyanstudio.gamerack.models.Game;
import com.saiyanstudio.gamerack.models.Info;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Created by deekshith on 16-11-2017.
 */

public interface IGDBService {

    @GET("games/?fields=name,first_release_date,cover&order=popularity:desc&filter[version_parent][not_exists]=1&filter[game][not_exists]=1")
    Call<List<Game>> searchGame(@Query("search") String searchText);

    @GET("games/{gameId}?fields=name,summary,rating,aggregated_rating,total_rating,developers,game_engines,category," +
            "time_to_beat,game_modes,themes,genres,expansions,first_release_date,cover,esrb,pegi,websites,external")
    Call<List<Game>> getGameInfoById(@Path("gameId") int gameId);

    @GET("companies/{developerIds}?fields=name")
    Call<List<Info>> getDeveloperInfoByIds(@Path("developerIds") String developerIds);

    @GET("game_engines/{gameEngineIds}?fields=name")
    Call<List<Info>> getGameEngineInfoByIds(@Path("gameEngineIds") String gameEngineIds);

    @GET("game_modes/{gameModeIds}?fields=name")
    Call<List<Info>> getGameModeInfoByIds(@Path("gameModeIds") String gameModeIds);

    @GET("themes/{themeIds}?fields=name")
    Call<List<Info>> getThemeInfoByIds(@Path("themeIds") String themeIds);

    @GET("genres/{genreIds}?fields=name")
    Call<List<Info>> getGenreInfoByIds(@Path("genreIds") String genreIds);

    @GET("games/{expansionIds}?fields=name")
    Call<List<Info>> getExpansionInfoByIds(@Path("expansionIds") String expansionIds);
}

package com.saiyanstudio.gamerack.retrofit;

import com.saiyanstudio.gamerack.models.Game;
import com.saiyanstudio.gamerack.models.Info;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

/**
 * Created by deekshith on 16-11-2017.
 */

public interface IGDBService {

    @POST("games")
    Call<List<Game>> getGameInfoByQuery(@Body String body);

    @POST("companies")
    Call<List<Info>> getDeveloperInfoByQuery(@Body String body);

    @POST("genres")
    Call<List<Info>> getGenreInfoByQuery(@Body String body);

    @POST("games")
    Call<List<Info>> getExpansionInfoByQuery(@Body String body);
}

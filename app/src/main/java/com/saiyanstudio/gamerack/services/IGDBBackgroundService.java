package com.saiyanstudio.gamerack.services;

import android.app.IntentService;
import android.content.Intent;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.content.LocalBroadcastManager;
import android.widget.Toast;

import com.saiyanstudio.gamerack.common.Constants;
import com.saiyanstudio.gamerack.handlers.DatabaseHandler;
import com.saiyanstudio.gamerack.messageevents.AddGameMessageEvent;
import com.saiyanstudio.gamerack.models.Game;
import com.saiyanstudio.gamerack.models.Info;
import com.saiyanstudio.gamerack.retrofit.RetrofitClient;

import org.apache.commons.lang3.StringUtils;
import org.greenrobot.eventbus.EventBus;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by deekshith on 19-11-2017.
 */

public class IGDBBackgroundService extends IntentService {

    Handler mHandler;
    DatabaseHandler db;

    Game game = null;

    boolean isExpansionsCallDone = false;
    boolean isGenresCallDone = false;

    public IGDBBackgroundService() {
        super("IGDBBackgroundService");
        mHandler = new Handler();
        db = new DatabaseHandler(this);
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        game = intent.getParcelableExtra(Constants.IntentKeys.game);

        callExpansionsInfoApi(game.getExpansions());
        callGenresInfoApi(game.getGenres());

    }

    private void prepareSaveToDb(){
        if(isExpansionsCallDone && isGenresCallDone) {
            //Save to DB
            long id = db.addGame(game);
            db.closeDB();

            // Send AddGameMessageEvent
            if(id > 0)
                EventBus.getDefault().post(new AddGameMessageEvent(game));
        }
    }

    private void callExpansionsInfoApi(List<Integer> expansions){

        if(expansions != null && expansions.size() > 0) {
            String expansionsIdString = StringUtils.join(expansions, ",");
            String strRequestBody = "fields name;where id = (" + expansionsIdString + ");";

            Call<List<Info>> getExpansionsInfoCall
                    = RetrofitClient.getIgdbApiService(getApplicationContext()).getExpansionInfoByQuery(strRequestBody);

            getExpansionsInfoCall.enqueue(new Callback<List<Info>>() {
                @Override
                public void onResponse(Call<List<Info>> call, Response<List<Info>> response) {
                    if(response.body().size() > 0) {
                        game.setExpansionsInfo(response.body());
                    }
                    isExpansionsCallDone = true;
                    prepareSaveToDb();
                }

                @Override
                public void onFailure(Call<List<Info>> call, Throwable t) {
                    isExpansionsCallDone = true;
                    prepareSaveToDb();
                    Toast.makeText(getApplicationContext(), "Something went wrong", Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            isExpansionsCallDone = true;
            prepareSaveToDb();
        }
    }

    private void callGenresInfoApi(List<Integer> genres){

        if(genres != null && genres.size() > 0) {

            String genresIdString = StringUtils.join(game.getGenres(), ",");
            String strRequestBody = "fields name;where id = (" + genresIdString + ");";

            Call<List<Info>> getGenresInfoCall
                    = RetrofitClient.getIgdbApiService(getApplicationContext()).getGenreInfoByQuery(strRequestBody);

            getGenresInfoCall.enqueue(new Callback<List<Info>>() {
                @Override
                public void onResponse(Call<List<Info>> call, Response<List<Info>> response) {
                    if(response.body().size() > 0) {
                        game.setGenresInfo(response.body());
                    }
                    isGenresCallDone = true;
                    prepareSaveToDb();
                }

                @Override
                public void onFailure(Call<List<Info>> call, Throwable t) {
                    isGenresCallDone = true;
                    prepareSaveToDb();
                    Toast.makeText(getApplicationContext(), "Something went wrong", Toast.LENGTH_SHORT).show();
                }
            });

        } else {
            isGenresCallDone = true;
            prepareSaveToDb();
        }

    }

}

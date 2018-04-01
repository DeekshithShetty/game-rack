package com.saiyanstudio.gamerack.services;

import android.app.IntentService;
import android.content.Intent;
import android.os.Handler;
import android.support.annotation.Nullable;

import com.saiyanstudio.gamerack.common.Constants;
import com.saiyanstudio.gamerack.handlers.DatabaseHandler;
import com.saiyanstudio.gamerack.messageevents.GetAllGameMessageEvent;
import com.saiyanstudio.gamerack.models.Game;

import org.greenrobot.eventbus.EventBus;

import java.util.List;


/**
 * Created by deekshith on 25-11-2017.
 */

public class GameListLoadingService extends IntentService {

    private DatabaseHandler db;
    private List<Game> gameList;
    private String statusFilter;

    public GameListLoadingService() {
        super("GameListLoadingService");
        db = new DatabaseHandler(this);
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {

        statusFilter = intent.getStringExtra(Constants.IntentKeys.statusFilter);

        if(statusFilter.equalsIgnoreCase(Constants.Status.all))
            gameList = db.getAllGames();
        else
            gameList = db.getAllGamesWithStatusFilter(statusFilter);

        db.closeDB();

        EventBus.getDefault().post(new GetAllGameMessageEvent(gameList, statusFilter));
    }
}

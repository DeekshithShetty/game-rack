package com.saiyanstudio.gamerack.messageevents;

import com.saiyanstudio.gamerack.models.Game;

import java.util.List;

/**
 * Created by deekshith on 25-11-2017.
 */

public class GetAllGameMessageEvent {

    private List<Game> gameList;

    private String statusFilter;

    public GetAllGameMessageEvent(List<Game> gameList, String statusFilter){
        this.gameList = gameList;
        this.statusFilter = statusFilter;
    }

    public List<Game> getGameList(){
        return gameList;
    }

    public String getStatusFilter() { return statusFilter; }
}
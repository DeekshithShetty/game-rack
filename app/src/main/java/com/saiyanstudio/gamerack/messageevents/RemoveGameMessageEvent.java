package com.saiyanstudio.gamerack.messageevents;

import com.saiyanstudio.gamerack.models.Game;

/**
 * Created by deekshith on 25-11-2017.
 */

public class RemoveGameMessageEvent {
    private Game game;

    public RemoveGameMessageEvent(Game game){
        this.game = game;
    }

    public Game getGame(){
        return game;
    }

}

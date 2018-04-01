package com.saiyanstudio.gamerack.messageevents;

import com.saiyanstudio.gamerack.models.Game;

/**
 * Created by deekshith on 25-11-2017.
 */

public class AddGameMessageEvent {

    private Game game;

    public AddGameMessageEvent(Game game){
        this.game = game;
    }

    public Game getGame(){
        return game;
    }
}

package com.saiyanstudio.gamerack.messageevents;

import com.saiyanstudio.gamerack.models.Expansion;
import com.saiyanstudio.gamerack.models.Game;

/**
 * Created by deekshith on 25-11-2017.
 */

public class UpdateExpansionMessageEvent {
    private Expansion expansion;

    public UpdateExpansionMessageEvent(Expansion expansion){
        this.expansion = expansion;
    }

    public Expansion getExpansion(){
        return expansion;
    }
}

package com.ikkerens.worldedit.model.events;

import com.mbserver.api.game.Player;

public abstract class SelectionCommandEvent extends WorldEditEvent {

    public SelectionCommandEvent( final Player player ) {
        super( player );
    }

}

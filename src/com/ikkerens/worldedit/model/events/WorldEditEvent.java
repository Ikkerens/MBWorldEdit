package com.ikkerens.worldedit.model.events;

import com.mbserver.api.events.CancellableEvent;
import com.mbserver.api.game.Player;

public class WorldEditEvent extends CancellableEvent {
    private final Player player;

    public WorldEditEvent( final Player player ) {
        this.player = player;
    }

    public Player getPlayer() {
        return this.player;
    }
}

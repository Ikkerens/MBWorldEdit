package com.ikkerens.worldedit.model.events;

import com.ikkerens.worldedit.model.pattern.SetBlockType;

import com.mbserver.api.game.Location;
import com.mbserver.api.game.Player;

public abstract class SinglePointActionEvent extends WorldEditActionEvent {
    private final Location point;

    public SinglePointActionEvent( final Player player, final SetBlockType type, final Location point ) {
        super( player, type );
        this.point = point;
    }

    public Location getSelectionPoint() {
        return this.point;
    }
}

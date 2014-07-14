package com.ikkerens.worldedit.model.events;

import com.ikkerens.worldedit.model.pattern.SetBlockType;

import com.mbserver.api.game.Player;

public abstract class WorldEditActionEvent extends WorldEditEvent {
    private final SetBlockType type;

    public WorldEditActionEvent( final Player player, final SetBlockType type ) {
        super( player );
        this.type = type;
    }

    /**
     * The SetBlockType instance used, will be null if it's a break-based command.
     */
    public SetBlockType getBlockType() {
        return this.type;
    }
}

package com.ikkerens.worldedit.model.events;

import com.ikkerens.worldedit.WorldEditPlugin;
import com.ikkerens.worldedit.model.Selection;
import com.ikkerens.worldedit.model.pattern.SetBlockType;

import com.mbserver.api.game.Player;

public abstract class CuboidActionEvent extends WorldEditActionEvent {

    public CuboidActionEvent( final Player player, final SetBlockType type ) {
        super( player, type );
    }

    public Selection getSelection() {
        return WorldEditPlugin.getSession( this.getPlayer() ).getSelection();
    }

}

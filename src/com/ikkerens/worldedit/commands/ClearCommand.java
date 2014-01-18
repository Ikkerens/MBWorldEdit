package com.ikkerens.worldedit.commands;

import com.ikkerens.worldedit.WorldEditPlugin;
import com.ikkerens.worldedit.handlers.AbstractCommand;

import com.mbserver.api.game.Player;

public class ClearCommand extends AbstractCommand< WorldEditPlugin > {

    public ClearCommand( final WorldEditPlugin plugin ) {
        super( plugin );
    }

    @Override
    protected void execute( final String label, final Player player, final String[] args ) {
        this.getSession( player ).clearSelection();
        player.clearLines();
        player.sendMessage( "Cleared your selection." );
    }

}

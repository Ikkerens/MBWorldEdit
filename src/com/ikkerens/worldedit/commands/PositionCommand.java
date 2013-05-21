package com.ikkerens.worldedit.commands;

import com.ikkerens.worldedit.WorldEditPlugin;
import com.ikkerens.worldedit.handlers.AbstractCommand;
import com.ikkerens.worldedit.model.Selection;

import com.mbserver.api.game.Player;

public class PositionCommand extends AbstractCommand< WorldEditPlugin > {

    public PositionCommand( final WorldEditPlugin plugin ) {
        super( plugin );
    }

    @Override
    protected void execute( final String label, final Player player, final String[] args ) {
        final Selection sel = this.getSession( player ).getSelection();
        if ( label.equalsIgnoreCase( "/pos1" ) )
            sel.setPosition1( player.getLocation() );
        else
            sel.setPosition2( player.getLocation() );
    }

}

package com.ikkerens.worldedit.commands;

import com.ikkerens.worldedit.WorldEditPlugin;
import com.ikkerens.worldedit.handlers.AbstractCommand;
import com.mbserver.api.game.Player;

public class PositionCommand extends AbstractCommand {

    public PositionCommand( WorldEditPlugin plugin ) {
        super( plugin );
    }

    @Override
    protected void execute( String label, Player player, String[] args ) {
        if ( label.equalsIgnoreCase( "/pos1" ) )
            this.getSelection( player ).setPosition1( player.getLocation() );
        else
            this.getSelection( player ).setPosition2( player.getLocation() );
    }

}

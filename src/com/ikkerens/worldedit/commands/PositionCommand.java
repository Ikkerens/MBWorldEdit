package com.ikkerens.worldedit.commands;

import com.ikkerens.worldedit.WorldEditPlugin;
import com.ikkerens.worldedit.handlers.AbstractCommand;
import com.ikkerens.worldedit.model.Selection;
import com.mbserver.api.game.Player;

public class PositionCommand extends AbstractCommand {

    public PositionCommand( WorldEditPlugin plugin ) {
        super( plugin );
    }

    @Override
    protected void execute( String label, Player player, String[] args ) {
        Selection sel = this.getSession( player ).getSelection();
        if ( label.equalsIgnoreCase( "/pos1" ) )
            sel.setPosition1( player.getLocation() );
        else
            sel.setPosition2( player.getLocation() );
    }

}

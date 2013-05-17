package com.ikkerens.worldedit.commands;

import com.ikkerens.worldedit.WorldEditPlugin;
import com.ikkerens.worldedit.handlers.ActionCommand;
import com.mbserver.api.game.Player;

public class UndoCommand extends ActionCommand<WorldEditPlugin> {

    public UndoCommand( WorldEditPlugin plugin ) {
        super( plugin );
    }

    @Override
    protected void execute( String label, Player player, String[] args ) {
        if ( this.getSession( player ).undoLast() )
            player.sendMessage( "Undone last action." );
        else
            player.sendMessage( "Action history is empty." );
    }

}

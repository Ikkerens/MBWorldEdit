package com.ikkerens.worldedit.commands;

import com.ikkerens.worldedit.WorldEditPlugin;
import com.ikkerens.worldedit.handlers.ActionCommand;

import com.mbserver.api.game.Player;

public class UndoCommand extends ActionCommand< WorldEditPlugin > {

    public UndoCommand( final WorldEditPlugin plugin ) {
        super( plugin );
    }

    @Override
    protected void execute( final String label, final Player player, final String[] args ) {
        int repeat = 1;
        if ( args.length > 0 ) {
            try {
                repeat = Integer.parseInt( args[ 1 ] );
            } catch ( NumberFormatException e ) {
                player.sendMessage( String.format( "%s is not a valid number.", args[ 1 ] ) );
            }
        }

        for ( int i = 0; i < repeat; i++ ) {
            if ( this.getSession( player ).undoLast() )
                player.sendMessage( "Undone last action." );
            else {
                player.sendMessage( "Action history is empty." );
                break;
            }
        }
    }

}

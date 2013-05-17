package com.ikkerens.worldedit.commands;

import com.ikkerens.worldedit.Config;
import com.ikkerens.worldedit.WorldEditPlugin;
import com.ikkerens.worldedit.handlers.AbstractCommand;
import com.mbserver.api.game.Player;

public class LimitCommand extends AbstractCommand<WorldEditPlugin> {

    public LimitCommand( WorldEditPlugin plugin ) {
        super( plugin );
    }

    @Override
    protected void execute( String label, Player player, String[] args ) {
        if ( args.length != 1 ) {
            player.sendMessage( "Usage: /" + label + " <limit>" );
            return;
        }

        try {
            int limit = Integer.parseInt( args[ 0 ] );
            this.getSession( player ).setLimit( limit );
            player.sendMessage( String.format( "Limit set to %s", limit ) );

            Config config = this.getPlugin().getConfig();
            if ( limit == -1 || limit > config.getUndoTreshold() )
                player.sendMessage( String.format( "You can NOT undo actions bigger than %s blocks!", config.getUndoTreshold() ) );
        } catch ( NumberFormatException e ) {
            player.sendMessage( args[ 0 ] + " is not a valid number." );
        }
    }
}

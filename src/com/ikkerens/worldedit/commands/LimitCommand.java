package com.ikkerens.worldedit.commands;

import com.ikkerens.worldedit.Config;
import com.ikkerens.worldedit.WorldEditPlugin;
import com.ikkerens.worldedit.handlers.AbstractCommand;
import com.ikkerens.worldedit.model.events.WorldEditEvent;

import com.mbserver.api.game.Player;

public class LimitCommand extends AbstractCommand< WorldEditPlugin > {

    public LimitCommand( final WorldEditPlugin plugin ) {
        super( plugin );
    }

    @Override
    protected void execute( final String label, final Player player, final String[] args ) {
        if ( args.length != 1 ) {
            player.sendMessage( "Usage: /" + label + " <limit>" );
            return;
        }

        try {
            final int limit = Integer.parseInt( args[ 0 ] );

            final SetEditLimitEvent event = new SetEditLimitEvent( player, limit );
            this.getPlugin().getPluginManager().triggerEvent( event );
            if ( !event.isCancelled() ) {
                this.getPlugin();
                WorldEditPlugin.getSession( player ).setLimit( limit );
                player.sendMessage( String.format( "Limit set to %s", limit ) );

                final Config config = this.getPlugin().getConfig();
                if ( ( limit == -1 ) || ( limit > config.getUndoTreshold() ) )
                    player.sendMessage( String.format( "You can NOT undo actions bigger than %s blocks!", config.getUndoTreshold() ) );
            }
        } catch ( final NumberFormatException e ) {
            player.sendMessage( args[ 0 ] + " is not a valid number." );
        }
    }

    public static class SetEditLimitEvent extends WorldEditEvent {

        public SetEditLimitEvent( final Player player, final int limit ) {
            super( player );
        }

    }
}

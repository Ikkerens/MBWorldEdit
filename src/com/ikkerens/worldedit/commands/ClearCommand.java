package com.ikkerens.worldedit.commands;

import com.ikkerens.worldedit.WorldEditPlugin;
import com.ikkerens.worldedit.handlers.AbstractCommand;
import com.ikkerens.worldedit.model.events.SelectionCommandEvent;

import com.mbserver.api.game.Player;

public class ClearCommand extends AbstractCommand< WorldEditPlugin > {

    public ClearCommand( final WorldEditPlugin plugin ) {
        super( plugin );
    }

    @Override
    protected void execute( final String label, final Player player, final String[] args ) {
        final ClearSelectionCommandEvent event = new ClearSelectionCommandEvent( player );
        this.getPlugin().getPluginManager().triggerEvent( event );

        if ( !event.isCancelled() ) {
            this.getPlugin();
            WorldEditPlugin.getSession( player ).clearSelection();
            player.clearLines();
            player.sendMessage( "Cleared your selection." );
        }
    }

    public static class ClearSelectionCommandEvent extends SelectionCommandEvent {

        public ClearSelectionCommandEvent( final Player player ) {
            super( player );
        }

    }

}

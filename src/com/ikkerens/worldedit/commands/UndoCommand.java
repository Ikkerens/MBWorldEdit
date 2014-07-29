package com.ikkerens.worldedit.commands;

import com.ikkerens.worldedit.Config;
import com.ikkerens.worldedit.WorldEditPlugin;
import com.ikkerens.worldedit.handlers.ActionCommand;
import com.ikkerens.worldedit.model.events.WorldEditEvent;

import com.mbserver.api.game.Player;

public class UndoCommand extends ActionCommand< WorldEditPlugin > {

    public UndoCommand( final WorldEditPlugin plugin ) {
        super( plugin );
    }

    @Override
    protected void execute( final String label, final Player player, final String[] args ) {
        int repeat = 1;
        if ( args.length > 0 )
            try {
                repeat = Integer.parseInt( args[ 0 ] );
            } catch ( final NumberFormatException e ) {
                player.sendMessage( String.format( "%s is not a valid number.", args[ 1 ] ) );
            }

        final int maxHistory = this.getPlugin().< Config > getConfig().getUndoHistoryCount();
        if ( repeat > maxHistory )
            repeat = maxHistory;

        final UndoActionEvent event = new UndoActionEvent( player, repeat );
        this.getPlugin().getPluginManager().triggerEvent( event );

        if ( !event.isCancelled() )
            for ( int i = 0; i < repeat; i++ ) {
                this.getPlugin();
                if ( WorldEditPlugin.getSession( player ).undoLast() )
                    player.sendMessage( "Undone last action." );
                else {
                    player.sendMessage( "Action history is empty." );
                    break;
                }
            }
    }

    public static class UndoActionEvent extends WorldEditEvent {
        private final int repeat;

        public UndoActionEvent( final Player player, final int repeat ) {
            super( player );
            this.repeat = repeat;
        }

        public int getRepeatCount() {
            return this.repeat;
        }

    }

}

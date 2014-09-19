package com.ikkerens.worldedit.commands;

import java.io.IOException;

import com.ikkerens.worldedit.WorldEditPlugin;
import com.ikkerens.worldedit.handlers.ActionCommand;
import com.ikkerens.worldedit.model.events.ClipboardActionEvent;

import com.mbserver.api.game.MBSchematic;
import com.mbserver.api.game.Player;

public class LoadCommand extends ActionCommand< WorldEditPlugin > {

    public LoadCommand( final WorldEditPlugin plugin ) {
        super( plugin );
    }

    @Override
    protected void execute( final String label, final Player player, final String[] args ) {
        if ( args.length != 1 ) {
            player.sendMessage( "Usage: /" + label + " <filename>" );
            return;
        }

        final ClipboardLoadEvent event = new ClipboardLoadEvent( player, args[ 0 ] );
        this.getPlugin().getPluginManager().triggerEvent( event );
        if ( !event.isCancelled() )
            try {
                final MBSchematic clb = MBSchematic.loadFromFile( String.format( "plugins/MBWorldEdit/schematics/%s.mbschem", args[ 0 ] ) );
                this.getPlugin();
                WorldEditPlugin.getSession( player ).setClipboard( clb );
                player.sendMessage( "Clipboard loaded." );
            } catch ( final IOException e ) {
                player.sendMessage( "Loading clipboard failed!" );
            }
    }

    public static class ClipboardLoadEvent extends ClipboardActionEvent {

        public ClipboardLoadEvent( final Player player, final String filename ) {
            super( player, filename );

        }

    }

}

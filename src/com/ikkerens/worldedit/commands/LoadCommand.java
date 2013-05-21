package com.ikkerens.worldedit.commands;

import com.ikkerens.worldedit.WorldEditPlugin;
import com.ikkerens.worldedit.handlers.ActionCommand;
import com.ikkerens.worldedit.model.Clipboard;

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

        final Clipboard clb = Clipboard.loadFromFile( args[ 0 ] );

        if ( clb != null ) {
            this.getSession( player ).setClipboard( clb );
            player.sendMessage( "Clipboard loaded." );
        } else
            player.sendMessage( "Loading clipboard failed!" );
    }

}

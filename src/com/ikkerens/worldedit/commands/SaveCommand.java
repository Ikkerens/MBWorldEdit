package com.ikkerens.worldedit.commands;

import java.io.IOException;

import com.ikkerens.worldedit.WorldEditPlugin;
import com.ikkerens.worldedit.handlers.ActionCommand;

import com.mbserver.api.game.MBSchematic;
import com.mbserver.api.game.Player;

public class SaveCommand extends ActionCommand< WorldEditPlugin > {

    public SaveCommand( final WorldEditPlugin plugin ) {
        super( plugin );
    }

    @Override
    protected void execute( final String label, final Player player, final String[] args ) {
        if ( args.length != 1 ) {
            player.sendMessage( "Usage: /" + label + " <filename>" );
            return;
        }

        final MBSchematic clb = this.getSession( player ).getClipboard();
        if ( clb == null ) {
            player.sendMessage( "Your clipboard is empty!" );
            return;
        }

        try {
            clb.saveTo( String.format( "plugins/MBWorldEdit/%s.mbschem", args[ 0 ] ) );
            player.sendMessage( String.format( "Clipboard saved to \"plugins/MBWorldEdit/%s.mbschem\".", args[ 0 ] ) );
        } catch ( final IOException e ) {
            player.sendMessage( "Saving clipboard failed!" );
        }
    }

}

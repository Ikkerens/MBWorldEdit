package com.ikkerens.worldedit.commands;

import com.ikkerens.worldedit.WorldEditPlugin;
import com.ikkerens.worldedit.exceptions.BlockLimitException;
import com.ikkerens.worldedit.handlers.ActionCommand;
import com.ikkerens.worldedit.model.Clipboard;
import com.ikkerens.worldedit.model.Session;
import com.ikkerens.worldedit.model.WEAction;

import com.mbserver.api.game.Location;
import com.mbserver.api.game.Player;
import com.mbserver.api.game.World;

public class PasteCommand extends ActionCommand< WorldEditPlugin > {

    public PasteCommand( final WorldEditPlugin plugin ) {
        super( plugin );
    }

    @Override
    protected void execute( final String label, final Player player, final String[] args ) {
        final Session session = this.getSession( player );
        final Clipboard clipboard = session.getClipboard();
        if ( clipboard != null ) {
            final Location pLoc = player.getLocation();
            final World world = pLoc.getWorld();
            final int oX = pLoc.getBlockX() + clipboard.getRelativeX();
            final int oY = pLoc.getBlockY() + clipboard.getRelativeY();
            final int oZ = pLoc.getBlockZ() + clipboard.getRelativeZ();
            final short[][][] blocks = clipboard.getBlocks();

            final WEAction wea = session.newAction( world, blocks.length * blocks[ 0 ].length * blocks[ 0 ][ 0 ].length );
            final boolean skipAir = ( args.length == 1 ) && args[ 0 ].equalsIgnoreCase( "-a" );

            final long start = System.currentTimeMillis();

            try {
                for ( int x = 0; x < blocks.length; x++ )
                    for ( int y = 0; y < blocks[ x ].length; y++ )
                        for ( int z = 0; z < blocks[ x ][ y ].length; z++ )
                            if ( !skipAir || ( blocks[ x ][ y ][ z ] != 0 ) )
                                wea.setBlock( x + oX, y + oY, z + oZ, blocks[ x ][ y ][ z ] );
                wea.finish();
            } catch ( final BlockLimitException e ) {
                player.sendMessage( String.format( FINISHED_LIMIT, wea.getAffected(), ( System.currentTimeMillis() - start ) / 1000f ) );
                return;
            }

            player.sendMessage( String.format( FINISHED_DONE, wea.getAffected(), ( System.currentTimeMillis() - start ) / 1000f ) );
        } else
            player.sendMessage( "Your clipboard is empty." );
    }

}

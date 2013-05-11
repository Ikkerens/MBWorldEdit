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

public class PasteCommand extends ActionCommand {

    public PasteCommand( WorldEditPlugin plugin ) {
        super( plugin );
    }

    @Override
    protected void execute( String label, Player player, String[] args ) {
        Session session = this.getSession( player );
        Clipboard clipboard = session.getClipboard();
        if ( clipboard != null ) {
            Location pLoc = player.getLocation();
            World world = pLoc.getWorld();
            int oX = pLoc.getBlockX() + clipboard.getRelativeX();
            int oY = pLoc.getBlockY() + clipboard.getRelativeY();
            int oZ = pLoc.getBlockZ() + clipboard.getRelativeZ();
            short[][][] blocks = clipboard.getBlocks();

            long start = System.currentTimeMillis();

            WEAction wea = session.newAction( world, 
                                                     blocks.length *
                                                     blocks[ 0 ].length * 
                                                     blocks[ 0 ][ 0 ].length );

            try {
                for ( int x = 0; x < blocks.length; x++ )
                    for ( int y = 0; y < blocks[ x ].length; y++ )
                        for ( int z = 0; z < blocks[ x ][ y ].length; z++ )
                            wea.setBlock( x + oX, y + oY, z + oZ, blocks[ x ][ y ][ z ] );
                wea.finish();
            } catch ( BlockLimitException e ) {
                player.sendMessage( String.format( FINISHED_LIMIT, wea.getAffected(), ( System.currentTimeMillis() - start ) / 1000f ) );
                return;
            }

            player.sendMessage( String.format( FINISHED_DONE, wea.getAffected(), ( System.currentTimeMillis() - start ) / 1000f ) );
        } else
            player.sendMessage( "Your clipboard is empty." );
    }

}

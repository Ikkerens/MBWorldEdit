package com.ikkerens.worldedit.commands;

import com.ikkerens.worldedit.WorldEditPlugin;
import com.ikkerens.worldedit.exceptions.BlockLimitException;
import com.ikkerens.worldedit.handlers.ActionCommand;
import com.ikkerens.worldedit.model.Session;
import com.ikkerens.worldedit.model.WEAction;
import com.ikkerens.worldedit.model.events.ClipboardActionEvent;

import com.mbserver.api.game.Location;
import com.mbserver.api.game.MBSchematic;
import com.mbserver.api.game.Player;
import com.mbserver.api.game.World;

public class PasteCommand extends ActionCommand< WorldEditPlugin > {

    public PasteCommand( final WorldEditPlugin plugin ) {
        super( plugin );
    }

    @Override
    protected void execute( final String label, final Player player, final String[] args ) {
        final Session session = this.getPlugin().getSession( player );
        final MBSchematic clipboard = session.getClipboard();
        if ( clipboard != null ) {
            final Location pLoc = player.getLocation();
            final World world = pLoc.getWorld();
            final int oX = pLoc.getBlockX() + clipboard.getRelativeX();
            final int oY = pLoc.getBlockY() + clipboard.getRelativeY();
            final int oZ = pLoc.getBlockZ() + clipboard.getRelativeZ();
            final int[] sizes = clipboard.getSizes();

            final boolean skipAir = ( args.length == 1 ) && args[ 0 ].equalsIgnoreCase( "-a" );
            final PasteCommandEvent event = new PasteCommandEvent( player, oX, oY, oZ, skipAir );
            this.getPlugin().getPluginManager().triggerEvent( event );

            if ( !event.isCancelled() ) {
                final WEAction wea = session.newAction( world, sizes[ 0 ] * sizes[ 1 ] * sizes[ 2 ] );

                final long start = System.currentTimeMillis();

                try {
                    for ( int x = 0; x < sizes[ 0 ]; x++ )
                        for ( int y = 0; y < sizes[ 1 ]; y++ )
                            for ( int z = 0; z < sizes[ 2 ]; z++ ) {
                                final short block = clipboard.getBlock( x, y, z );
                                if ( !skipAir || ( block != 0 ) )
                                    wea.setBlock( x + oX, y + oY, z + oZ, block );
                            }
                    wea.finish();
                } catch ( final BlockLimitException e ) {
                    player.sendMessage( String.format( FINISHED_LIMIT, wea.getAffected(), ( System.currentTimeMillis() - start ) / 1000f ) );
                    return;
                }

                player.sendMessage( String.format( FINISHED_DONE, wea.getAffected(), ( System.currentTimeMillis() - start ) / 1000f ) );
            }
        } else
            player.sendMessage( "Your clipboard is empty." );
    }

    public static class PasteCommandEvent extends ClipboardActionEvent {
        private final int     oX, oY, oZ;
        private final boolean skipAir;

        public PasteCommandEvent( final Player player, final int oX, final int oY, final int oZ, final boolean skipAir ) {
            super( player, null );
            this.oX = oX;
            this.oY = oY;
            this.oZ = oZ;
            this.skipAir = skipAir;
        }

        public int getOriginX() {
            return this.oX;
        }

        public int getOriginY() {
            return this.oY;
        }

        public int getOriginZ() {
            return this.oZ;
        }

        public boolean isSkippingAir() {
            return this.skipAir;
        }
    }
}

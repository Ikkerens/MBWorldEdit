package com.ikkerens.worldedit.wand;

import com.ikkerens.worldedit.WorldEditPlugin;
import com.ikkerens.worldedit.commands.CopyCommand;
import com.ikkerens.worldedit.handlers.AbstractListener;

import com.mbserver.api.events.BlockBreakEvent;
import com.mbserver.api.events.BlockEvent;
import com.mbserver.api.events.EventHandler;
import com.mbserver.api.game.Location;
import com.mbserver.api.game.Player;

public class WandListener extends AbstractListener< WorldEditPlugin > {

    public WandListener( final WorldEditPlugin plugin ) {
        super( plugin );
    }

    @EventHandler
    public void onWandAction( final BlockEvent e ) {
        final Player player = e.getPlayer();

        final short compare;
        if ( e instanceof BlockBreakEvent )
            compare = e.getPlayer().getHandItem();
        else {
            compare = e.getBlock().getBlockID();
            try {
                Thread.sleep( 100 );
            } catch ( final InterruptedException e1 ) {
            }
        }

        if ( compare == Wand.LEFT.getId() || compare == Wand.RIGHT.getId() ) {
            final Location originLoc = player.getMetaData( CopyCommand.COPY_LOCATION_KEY, null );
            if ( originLoc != null ) {
                final Location tLoc = e.getLocation();
                final int rX = originLoc.getBlockX() - tLoc.getBlockX();
                final int rY = originLoc.getBlockY() - tLoc.getBlockY();
                final int rZ = originLoc.getBlockZ() - tLoc.getBlockZ();
                this.getSession( player ).getClipboard().setRelative( rX, rY, rZ );

                player.removeMetaData( CopyCommand.COPY_LOCATION_KEY );
                e.setCancelled( true );
                player.sendMessage( String.format( "Clipboard origin updated to %s", String.format( "(%s,%s,%s)", tLoc.getBlockX(), tLoc.getBlockY(), tLoc.getBlockZ() ) ) );
                return;
            }

            if ( compare == Wand.LEFT.getId() ) {
                this.getSession( e.getPlayer() ).getSelection().setPosition1( e.getLocation() );
                e.setCancelled( true );
            } else {
                this.getSession( e.getPlayer() ).getSelection().setPosition2( e.getLocation() );
                e.setCancelled( true );
            }
        }
    }
}

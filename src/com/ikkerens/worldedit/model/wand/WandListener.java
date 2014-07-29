package com.ikkerens.worldedit.model.wand;

import com.ikkerens.worldedit.WorldEditPlugin;
import com.ikkerens.worldedit.commands.CopyCommand;
import com.ikkerens.worldedit.handlers.AbstractListener;
import com.ikkerens.worldedit.model.events.WandPlaceEvent;

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
            compare = player.getHandItem();
        else {
            compare = e.getBlock().getBlockID();
            try {
                Thread.sleep( 100 );
            } catch ( final InterruptedException e1 ) {
            }
        }

        if ( ( compare == Wand.LEFT.getId() ) || ( compare == Wand.RIGHT.getId() ) ) {
            final Location location = e.getLocation();
            final boolean left = compare == Wand.LEFT.getId();

            final WandPlaceEvent event = new WandPlaceEvent( player, location, left );
            this.getPlugin().getPluginManager().triggerEvent( event );

            if ( !event.isCancelled() )
                if ( left ) {
                    this.getPlugin();
                    this.getPlugin();
                    WorldEditPlugin.getSession( player ).getSelection().setPosition1( location );
                } else
                    WorldEditPlugin.getSession( player ).getSelection().setPosition2( location );
            if ( event.shouldRevertBlock() )
                e.setCancelled( true );
        }
    }

    @EventHandler
    public void onOriginWand( final WandPlaceEvent e ) {
        final Player player = e.getPlayer();

        final Location originLoc = player.getMetaData( CopyCommand.COPY_LOCATION_KEY, null );
        if ( originLoc != null ) {
            final Location tLoc = e.getLocation();
            final int rX = originLoc.getBlockX() - tLoc.getBlockX();
            final int rY = originLoc.getBlockY() - tLoc.getBlockY();
            final int rZ = originLoc.getBlockZ() - tLoc.getBlockZ();
            this.getPlugin();
            WorldEditPlugin.getSession( player ).getClipboard().setRelative( rX, rY, rZ );

            player.removeMetaData( CopyCommand.COPY_LOCATION_KEY );
            player.sendMessage( String.format( "Clipboard origin updated to %s", String.format( "(%s,%s,%s)", tLoc.getBlockX(), tLoc.getBlockY(), tLoc.getBlockZ() ) ) );
            e.setCancelled( true );
            e.setRevertBlock( true );
        }
    }
}

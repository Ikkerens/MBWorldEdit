package com.ikkerens.worldedit.commands;

import com.ikkerens.worldedit.WorldEditPlugin;
import com.ikkerens.worldedit.handlers.ActionCommand;
import com.ikkerens.worldedit.model.Selection;
import com.ikkerens.worldedit.model.Session;
import com.ikkerens.worldedit.model.events.SelectionCommandEvent;

import com.mbserver.api.game.Location;
import com.mbserver.api.game.MBSchematic;
import com.mbserver.api.game.Player;
import com.mbserver.api.game.World;

public class CopyCommand extends ActionCommand< WorldEditPlugin > {
    public static final String COPY_LOCATION_KEY = "worldedit.copy.origin";

    public CopyCommand( final WorldEditPlugin plugin ) {
        super( plugin );
    }

    @Override
    protected void execute( final String label, final Player player, final String[] args ) {
        final Session session = this.getPlugin().getSession( player );
        final Selection sel = session.getSelection();
        if ( sel.isValid() ) {
            final Location lowest = sel.getMinimumPosition();
            final Location highest = sel.getMaximumPosition();
            final World world = lowest.getWorld();

            final CopyCommandEvent event = new CopyCommandEvent( player );
            this.getPlugin().getPluginManager().triggerEvent( event );

            if ( !event.isCancelled() ) {
                final short[][][] clp = new short[ ( highest.getBlockX() - lowest.getBlockX() ) + 1 ][ ( highest.getBlockY() - lowest.getBlockY() ) + 1 ][ ( highest.getBlockZ() - lowest.getBlockZ() ) + 1 ];

                for ( int x = lowest.getBlockX(); x <= highest.getBlockX(); x++ )
                    for ( int y = lowest.getBlockY(); y <= highest.getBlockY(); y++ )
                        for ( int z = lowest.getBlockZ(); z <= highest.getBlockZ(); z++ )
                            clp[ x - lowest.getBlockX() ][ y - lowest.getBlockY() ][ z - lowest.getBlockZ() ] = world.getFlaggedBlockID( x, y, z );
                final Location pLoc = player.getLocation();
                final int x = lowest.getBlockX() - pLoc.getBlockX();
                final int y = lowest.getBlockY() - pLoc.getBlockY();
                final int z = lowest.getBlockZ() - pLoc.getBlockZ();

                final MBSchematic clipboard = new MBSchematic( x, y, z, clp );
                session.setClipboard( clipboard );

                if ( ( args.length > 0 ) && args[ 0 ].equalsIgnoreCase( "-o" ) ) {
                    player.setMetaData( COPY_LOCATION_KEY, lowest );
                    player.sendMessage( "Blocks copied. You can now specify a location to use as origin using your wand." );
                } else
                    player.sendMessage( "Blocks copied" );
            }
        } else
            player.sendMessage( NEED_SELECTION );

    }

    public static class CopyCommandEvent extends SelectionCommandEvent {
        public CopyCommandEvent( final Player player ) {
            super( player );
        }

    }
}

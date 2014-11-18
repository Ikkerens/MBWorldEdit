package com.ikkerens.worldedit.commands;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import com.ikkerens.worldedit.WorldEditPlugin;
import com.ikkerens.worldedit.exceptions.BlockNotFoundException;
import com.ikkerens.worldedit.handlers.AbstractCommand;
import com.ikkerens.worldedit.model.Selection;
import com.ikkerens.worldedit.model.events.SelectionCommandEvent;
import com.ikkerens.worldedit.model.pattern.MatchBlockType;

import com.mbserver.api.game.Location;
import com.mbserver.api.game.Material;
import com.mbserver.api.game.Player;
import com.mbserver.api.game.World;

public class CountCommand extends AbstractCommand< WorldEditPlugin > {

    public CountCommand( final WorldEditPlugin plugin ) {
        super( plugin );
    }

    @Override
    protected void execute( final String label, final Player player, final String[] args ) {
        if ( args.length > 1 ) {
            player.sendMessage( "Usage: /" + label + " <block>" );
            return;
        }

        final Selection sel = WorldEditPlugin.getSession( player ).getSelection();
        if ( sel.isValid() ) {
            final Location lowest = sel.getMinimumPosition();
            final Location highest = sel.getMaximumPosition();
            final World world = lowest.getWorld();

            MatchBlockType match = null;
            if ( args.length == 1 )
                try {
                    match = new MatchBlockType( args[ 0 ] );
                } catch ( final BlockNotFoundException e ) {
                    player.sendMessage( e.getMessage() );
                    return;
                }

            final CountCommandEvent event = new CountCommandEvent( player, match );
            this.getPlugin().getPluginManager().triggerEvent( event );

            if ( !event.isCancelled() ) {
                final Map< Short, Integer > counts = new HashMap< Short, Integer >();

                if ( match != null )
                    for ( final short id : match.getBlockIDs() )
                        counts.put( id, 0 );

                for ( int x = lowest.getBlockX(); x <= highest.getBlockX(); x++ )
                    for ( int z = lowest.getBlockZ(); z <= highest.getBlockZ(); z++ )
                        for ( int y = lowest.getBlockY(); y <= highest.getBlockY(); y++ ) {
                            final short id = world.getBlockID( x, y, z );
                            if ( ( match == null ) || match.matches( id ) )
                                if ( counts.containsKey( id ) )
                                    counts.put( id, counts.get( id ) + 1 );
                                else
                                    counts.put( id, 1 );
                        }

                String foundBlocks = "Counted blocks: ";
                int count = 0;

                for ( final Entry< Short, Integer > entry : counts.entrySet() ) {
                    final short id = entry.getKey();
                    final Material mat = Material.getMaterialByID( id );
                    foundBlocks += count != 0 ? ", " : "";
                    foundBlocks += String.format( "%dx %s",
                        entry.getValue(),
                        mat != null ?
                            String.format( "%s (%d)", mat.name().toLowerCase(), id )
                            : id );
                    if ( ++count == 5 ) {
                        player.sendMessage( foundBlocks );
                        foundBlocks = "Counted blocks: ";
                        count = 0;
                    }
                }

                if ( !foundBlocks.equals( "Counted blocks: " ) )
                    player.sendMessage( foundBlocks );
            }
        } else
            player.sendMessage( NEED_SELECTION );
    }

    public static class CountCommandEvent extends SelectionCommandEvent {
        private final MatchBlockType type;

        public CountCommandEvent( final Player player, final MatchBlockType type ) {
            super( player );
            this.type = type;
        }

        public MatchBlockType getBlockType() {
            return this.type;
        }
    }
}

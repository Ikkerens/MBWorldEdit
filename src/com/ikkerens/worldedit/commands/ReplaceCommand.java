package com.ikkerens.worldedit.commands;

import com.ikkerens.worldedit.WorldEditPlugin;
import com.ikkerens.worldedit.exceptions.BlockLimitException;
import com.ikkerens.worldedit.exceptions.BlockNotFoundException;
import com.ikkerens.worldedit.handlers.ActionCommand;
import com.ikkerens.worldedit.model.Selection;
import com.ikkerens.worldedit.model.Session;
import com.ikkerens.worldedit.model.WEAction;
import com.ikkerens.worldedit.model.events.CuboidActionEvent;
import com.ikkerens.worldedit.model.pattern.MatchBlockType;
import com.ikkerens.worldedit.model.pattern.SetBlockType;

import com.mbserver.api.dynamic.BlockManager;
import com.mbserver.api.game.Location;
import com.mbserver.api.game.Player;
import com.mbserver.api.game.World;

public class ReplaceCommand extends ActionCommand< WorldEditPlugin > {

    public ReplaceCommand( final WorldEditPlugin plugin ) {
        super( plugin );
    }

    @Override
    protected void execute( final String label, final Player player, final String[] args ) {
        if ( ( ( args.length != 2 ) && ( args.length != 3 ) ) || ( ( args.length == 3 ) && !args[ 0 ].equals( "-r" ) ) ) {
            player.sendMessage( "Usage: /" + label + " [-r] <match> <replacement>" );
            return;
        }

        int it = -1;
        if ( args.length == 3 )
            it++;

        this.getPlugin();
        final Session session = WorldEditPlugin.getSession( player );
        final Selection sel = session.getSelection();
        if ( sel.isValid() ) {
            final Location lowest = sel.getMinimumPosition();
            final Location highest = sel.getMaximumPosition();
            final World world = lowest.getWorld();

            SetBlockType type;
            MatchBlockType matcher;
            try {
                matcher = new MatchBlockType( args[ ++it ] );
                type = SetBlockType.from( session, args[ ++it ] );
            } catch ( final BlockNotFoundException e ) {
                player.sendMessage( e.getMessage() );
                return;
            }

            final ReplaceCommandEvent event = new ReplaceCommandEvent( player, matcher, type );
            this.getPlugin().getPluginManager().triggerEvent( event );

            if ( !event.isCancelled() ) {
                final long start = System.currentTimeMillis();
                final WEAction wea = session.newAction( world, sel.getCount() );

                try {
                    final BlockManager mgr = this.getPlugin().getServer().getBlockManager();
                    for ( int x = lowest.getBlockX(); x <= highest.getBlockX(); x++ )
                        for ( int z = lowest.getBlockZ(); z <= highest.getBlockZ(); z++ )
                            for ( int y = lowest.getBlockY(); y <= highest.getBlockY(); y++ ) {
                                final short matchBlock = world.getFlaggedBlockID( x, y, z );

                                if ( !matcher.matches( matchBlock ) )
                                    continue;

                                short nextBlock = type.getNextBlock( x, y, z );

                                if ( ( args.length == 3 ) && ( mgr.getBlockType( matchBlock & 0x00FF ).getRotatability() == mgr.getBlockType( nextBlock ).getRotatability() ) )
                                    nextBlock = (short) ( nextBlock | ( matchBlock & 0xFF00 ) );

                                wea.setBlock( x, y, z, nextBlock );
                            }
                    wea.finish();
                } catch ( final BlockLimitException e ) {
                    player.sendMessage( String.format( FINISHED_LIMIT, wea.getAffected(), ( System.currentTimeMillis() - start ) / 1000f ) );
                    return;
                }

                player.sendMessage( String.format( FINISHED_DONE, wea.getAffected(), ( System.currentTimeMillis() - start ) / 1000f ) );
            }
        } else
            player.sendMessage( NEED_SELECTION );
    }

    public static class ReplaceCommandEvent extends CuboidActionEvent {
        private final MatchBlockType match;

        public ReplaceCommandEvent( final Player player, final MatchBlockType match, final SetBlockType replacement ) {
            super( player, replacement );
            this.match = match;
        }

        public MatchBlockType getMatcher() {
            return this.match;
        }
    }
}

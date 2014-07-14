package com.ikkerens.worldedit.commands;

import com.ikkerens.worldedit.WorldEditPlugin;
import com.ikkerens.worldedit.exceptions.BlockLimitException;
import com.ikkerens.worldedit.handlers.ActionCommand;
import com.ikkerens.worldedit.model.Selection;
import com.ikkerens.worldedit.model.Session;
import com.ikkerens.worldedit.model.WEAction;
import com.ikkerens.worldedit.model.events.CuboidActionEvent;

import com.mbserver.api.game.Location;
import com.mbserver.api.game.Player;
import com.mbserver.api.game.World;

public class BreakCommand extends ActionCommand< WorldEditPlugin > {

    public BreakCommand( final WorldEditPlugin plugin ) {
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

            final BreakActionEvent event = new BreakActionEvent( player );
            this.getPlugin().getPluginManager().triggerEvent( event );

            if ( !event.isCancelled() ) {
                final long start = System.currentTimeMillis();
                final WEAction wea = session.newAction( world, sel.getCount() );

                try {
                    for ( int x = lowest.getBlockX(); x <= highest.getBlockX(); x++ )
                        for ( int z = lowest.getBlockZ(); z <= highest.getBlockZ(); z++ )
                            for ( int y = lowest.getBlockY(); y <= highest.getBlockY(); y++ )
                                wea.destroy( x, y, z, ( ( args.length == 1 ) && args[ 0 ].equals( "-n" ) ) ? 0 : player.getHandItem() );
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

    public static class BreakActionEvent extends CuboidActionEvent {

        public BreakActionEvent( final Player player ) {
            super( player, null );
        }

    }
}

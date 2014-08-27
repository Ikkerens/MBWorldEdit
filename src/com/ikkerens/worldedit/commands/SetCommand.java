package com.ikkerens.worldedit.commands;

import com.ikkerens.worldedit.WorldEditPlugin;
import com.ikkerens.worldedit.exceptions.BlockLimitException;
import com.ikkerens.worldedit.exceptions.BlockNotFoundException;
import com.ikkerens.worldedit.handlers.ActionCommand;
import com.ikkerens.worldedit.model.Selection;
import com.ikkerens.worldedit.model.Session;
import com.ikkerens.worldedit.model.WEAction;
import com.ikkerens.worldedit.model.events.CuboidActionEvent;
import com.ikkerens.worldedit.model.pattern.SetBlockType;

import com.mbserver.api.game.Location;
import com.mbserver.api.game.Player;
import com.mbserver.api.game.World;

public class SetCommand extends ActionCommand< WorldEditPlugin > {

    public SetCommand( final WorldEditPlugin plugin ) {
        super( plugin );
    }

    @Override
    protected void execute( final String label, final Player player, final String[] args ) {
        if ( args.length != 1 ) {
            player.sendMessage( "Usage: /" + label + " <blocktype>" );
            return;
        }

        final Session session = WorldEditPlugin.getSession( player );
        final Selection sel = session.getSelection();
        if ( sel.isValid() ) {
            final Location lowest = sel.getMinimumPosition();
            final Location highest = sel.getMaximumPosition();
            final World world = lowest.getWorld();

            SetBlockType type;
            try {
                type = SetBlockType.from( session, args[ 0 ] );
            } catch ( final BlockNotFoundException e ) {
                player.sendMessage( e.getMessage() );
                return;
            }

            final SetActionEvent event = new SetActionEvent( player, type );
            this.getPlugin().getPluginManager().triggerEvent( event );

            if ( !event.isCancelled() ) {
                final long start = System.currentTimeMillis();
                final WEAction wea = session.newAction( world, sel.getCount() );

                try {
                    for ( int x = lowest.getBlockX(); x <= highest.getBlockX(); x++ )
                        for ( int z = lowest.getBlockZ(); z <= highest.getBlockZ(); z++ )
                            for ( int y = lowest.getBlockY(); y <= highest.getBlockY(); y++ )
                                wea.setBlock( x, y, z, type );
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

    public static class SetActionEvent extends CuboidActionEvent {

        public SetActionEvent( final Player player, final SetBlockType type ) {
            super( player, type );
        }

    }
}

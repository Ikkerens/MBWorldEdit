package com.ikkerens.worldedit.commands;

import com.ikkerens.worldedit.WorldEditPlugin;
import com.ikkerens.worldedit.handlers.AbstractCommand;
import com.ikkerens.worldedit.model.Selection;
import com.ikkerens.worldedit.wand.Direction;

import com.mbserver.api.Constructors;
import com.mbserver.api.game.Location;
import com.mbserver.api.game.Player;

public class ExpandCommand extends AbstractCommand< WorldEditPlugin > {

    public ExpandCommand( final WorldEditPlugin plugin ) {
        super( plugin );
    }

    @Override
    protected void execute( final String label, final Player player, final String[] args ) {
        if ( args.length != 2 ) {
            player.sendMessage( "Usage: /" + label + " <amount> <direction>" );
            return;
        }

        final Selection sel = this.getSession( player ).getSelection();
        if ( sel.isValid() ) {
            Location lowest, highest;
            if ( args[ 0 ].equalsIgnoreCase( "vert" ) ) {
                lowest = sel.getMinimumPosition();
                highest = sel.getMaximumPosition();

                sel.setPositions( Constructors.newLocation( sel.getWorld(), lowest.getX(), 0, highest.getZ() ), Constructors.newLocation( sel.getWorld(), highest.getX(), 127, highest.getZ() ) );
                sel.inform();
            } else {
                int amount;
                try {
                    amount = Integer.parseInt( args[ 0 ] );
                } catch ( final NumberFormatException e ) {
                    player.sendMessage( "That amount is invalid." );
                    return;
                }

                Direction dir;
                try {
                    dir = Direction.valueOf( args[ 1 ].toUpperCase() );
                } catch ( final IllegalArgumentException e ) {
                    player.sendMessage( "That direction is invalid." );
                    return;
                }

                lowest = sel.getMinimumPosition();
                highest = sel.getMaximumPosition();

                switch( dir ) {
                    case UP:
                    case NORTH:
                    case EAST:
                        lowest = dir.addToLocation( highest, amount );
                        break;

                    case DOWN:
                    case SOUTH:
                    case WEST:
                        highest = dir.addToLocation( lowest, amount );
                        break;
                }
            }

            sel.setPositions( lowest, highest );
            sel.inform();
        } else
            player.sendMessage( NEED_SELECTION );
    }

}

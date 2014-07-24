package com.ikkerens.worldedit.commands;

import com.ikkerens.worldedit.WorldEditPlugin;
import com.ikkerens.worldedit.handlers.AbstractCommand;
import com.ikkerens.worldedit.model.Selection;
import com.ikkerens.worldedit.model.events.SelectionCommandEvent;
import com.ikkerens.worldedit.model.wand.Direction;

import com.mbserver.api.game.Location;
import com.mbserver.api.game.Player;

public class ShiftCommand extends AbstractCommand< WorldEditPlugin > {

    public ShiftCommand( final WorldEditPlugin plugin ) {
        super( plugin );
    }

    @Override
    protected void execute( final String label, final Player player, final String[] args ) {
        if ( args.length != 2 ) {
            player.sendMessage( "Usage: /" + label + " <amount> <direction>" );
            return;
        }

        final Selection sel = this.getPlugin().getSession( player ).getSelection();
        if ( sel.isValid() ) {
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

            final ShiftSelectionCommandEvent event = new ShiftSelectionCommandEvent( player, dir, amount );
            this.getPlugin().getPluginManager().triggerEvent( event );

            if ( !event.isCancelled() ) {
                final Location lowest = sel.getMinimumPosition();
                final Location highest = sel.getMaximumPosition();

                sel.setPositions( dir.addToLocation( lowest, amount ), dir.addToLocation( highest, amount ) );
                sel.inform();
            }
        } else
            player.sendMessage( NEED_SELECTION );
    }

    public static class ShiftSelectionCommandEvent extends SelectionCommandEvent {
        private final Direction direction;
        private final int       amount;

        public ShiftSelectionCommandEvent( final Player player, final Direction direction, final int amount ) {
            super( player );
            this.direction = direction;
            this.amount = amount;
        }

        public Direction getDirection() {
            return this.direction;
        }

        public int getAmount() {
            return this.amount;
        }
    }

}

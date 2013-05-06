package com.ikkerens.worldedit.commands;

import com.ikkerens.worldedit.WorldEditPlugin;
import com.ikkerens.worldedit.exceptions.BlockLimitException;
import com.ikkerens.worldedit.exceptions.BlockNotFoundException;
import com.ikkerens.worldedit.handlers.AbstractCommand;
import com.ikkerens.worldedit.model.Selection;
import com.ikkerens.worldedit.model.SetBlockType;
import com.mbserver.api.game.Location;
import com.mbserver.api.game.Player;
import com.mbserver.api.game.World;

public class WallsCommand extends AbstractCommand {

    public WallsCommand( WorldEditPlugin plugin ) {
        super( plugin );
    }

    @Override
    protected void execute( String label, Player player, String[] args ) {
        if ( args.length != 1 ) {
            player.sendMessage( "Usage: /" + label + " <blocktype>" );
            return;
        }

        Selection sel = this.getSelection( player );
        if ( sel.isValid() ) {
            Location lowest = sel.getMinimumPosition();
            Location highest = sel.getMaximumPosition();
            World world = lowest.getWorld();

            SetBlockType type;
            try {
                type = new SetBlockType( args[ 0 ] );
            } catch ( BlockNotFoundException e ) {
                player.sendMessage( e.getMessage() );
                return;
            }

            long start = System.currentTimeMillis();
            int affected = 0;

            try {
                for ( int y = lowest.getBlockY(); y <= highest.getBlockY(); y++ ) {
                    for ( int z = lowest.getBlockZ(); z <= highest.getBlockZ(); z++ ) {
                        affected += this.setBlock( affected, world, lowest.getBlockX(), y, z, type );
                        affected += this.setBlock( affected, world, highest.getBlockX(), y, z, type );
                    }

                    for ( int x = lowest.getBlockX(); x <= highest.getBlockX(); x++ ) {
                        affected += this.setBlock( affected, world, x, y, lowest.getBlockZ(), type );
                        affected += this.setBlock( affected, world, x, y, highest.getBlockZ(), type );
                    }
                }
            } catch ( BlockLimitException e ) {
                player.sendMessage( String.format( "Hit limit of %s blocks after %s seconds.", affected, ( System.currentTimeMillis() - start ) / 1000f ) );
                return;
            }

            player.sendMessage( String.format( "Action of %s blocks completed in %s seconds.", affected, ( System.currentTimeMillis() - start ) / 1000f ) );
        } else
            player.sendMessage( "You need a valid selection to do this." );
    }
}

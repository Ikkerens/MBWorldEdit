package com.ikkerens.worldedit.commands;

import com.ikkerens.worldedit.WorldEditPlugin;
import com.ikkerens.worldedit.exceptions.BlockLimitException;
import com.ikkerens.worldedit.exceptions.BlockNotFoundException;
import com.ikkerens.worldedit.handlers.ActionCommand;
import com.ikkerens.worldedit.model.Selection;
import com.ikkerens.worldedit.model.Session;
import com.ikkerens.worldedit.model.SetBlockType;
import com.ikkerens.worldedit.model.WEAction;
import com.mbserver.api.game.Location;
import com.mbserver.api.game.Player;
import com.mbserver.api.game.World;

public class OutlineCommand extends ActionCommand {

    public OutlineCommand( WorldEditPlugin plugin ) {
        super( plugin );
    }

    @Override
    protected void execute( String label, Player player, String[] args ) {
        if ( args.length != 1 ) {
            player.sendMessage( "Usage: /" + label + " <blocktype>" );
            return;
        }

        Session session = this.getSession( player );
        Selection sel = session.getSelection();
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
            WEAction wea = session.newAction( world, sel.getCount() );

            try {
                for ( int y = lowest.getBlockY(); y <= highest.getBlockY(); y++ ) {
                    for ( int z = lowest.getBlockZ(); z <= highest.getBlockZ(); z++ ) {
                        wea.setBlock( lowest.getBlockX(), y, z, type );
                        wea.setBlock( highest.getBlockX(), y, z, type );
                    }

                    for ( int x = lowest.getBlockX(); x <= highest.getBlockX(); x++ ) {
                        wea.setBlock( x, y, lowest.getBlockZ(), type );
                        wea.setBlock( x, y, highest.getBlockZ(), type );
                    }
                }

                if ( label.equalsIgnoreCase( "/outline" ) )
                    for ( int x = lowest.getBlockX(); x <= highest.getBlockX(); x++ )
                        for ( int z = lowest.getBlockZ(); z <= highest.getBlockZ(); z++ ) {
                            wea.setBlock( x, lowest.getBlockY(), z, type );
                            wea.setBlock( x, highest.getBlockY(), z, type );
                        }

                wea.finish();
            } catch ( BlockLimitException e ) {
                player.sendMessage( String.format( FINISHED_LIMIT, wea.getAffected(), ( System.currentTimeMillis() - start ) / 1000f ) );
                return;
            }

            player.sendMessage( String.format( FINISHED_DONE, wea.getAffected(), ( System.currentTimeMillis() - start ) / 1000f ) );
        } else
            player.sendMessage( NEED_SELECTION );
    }
}
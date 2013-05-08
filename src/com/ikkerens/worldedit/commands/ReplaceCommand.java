package com.ikkerens.worldedit.commands;

import com.ikkerens.worldedit.WorldEditPlugin;
import com.ikkerens.worldedit.exceptions.BlockLimitException;
import com.ikkerens.worldedit.exceptions.BlockNotFoundException;
import com.ikkerens.worldedit.handlers.ActionCommand;
import com.ikkerens.worldedit.model.MatchBlockType;
import com.ikkerens.worldedit.model.Selection;
import com.ikkerens.worldedit.model.Session;
import com.ikkerens.worldedit.model.SetBlockType;
import com.ikkerens.worldedit.model.WEAction;
import com.mbserver.api.game.Location;
import com.mbserver.api.game.Player;
import com.mbserver.api.game.World;

public class ReplaceCommand extends ActionCommand {

    public ReplaceCommand( WorldEditPlugin plugin ) {
        super( plugin );
    }

    @Override
    protected void execute( String label, Player player, String[] args ) {
        if ( args.length != 2 ) {
            player.sendMessage( "Usage: /" + label + " <match> <replacement>" );
            return;
        }

        Session session = this.getSession( player );
        Selection sel = session.getSelection();
        if ( sel.isValid() ) {
            Location lowest = sel.getMinimumPosition();
            Location highest = sel.getMaximumPosition();
            World world = lowest.getWorld();

            SetBlockType type;
            MatchBlockType match;
            try {
                type = new SetBlockType( args[ 1 ] );
                match = new MatchBlockType( args[ 0 ] );
            } catch ( BlockNotFoundException e ) {
                player.sendMessage( e.getMessage() );
                return;
            }

            long start = System.currentTimeMillis();
            WEAction wea = session.newAction( world, sel.getCount() );

            try {
                for ( int x = lowest.getBlockX(); x <= highest.getBlockX(); x++ )
                    for ( int z = lowest.getBlockZ(); z <= highest.getBlockZ(); z++ )
                        for ( int y = lowest.getBlockY(); y <= highest.getBlockY(); y++ )
                            if ( match.matches( world.getBlockID( x, y, z ) ) )
                                wea.setBlock( x, y, z, type );
            } catch ( BlockLimitException e ) {
                player.sendMessage( String.format( "Hit limit of %s blocks after %s seconds.", wea.getAffected(), ( System.currentTimeMillis() - start ) / 1000f ) );
                return;
            }

            player.sendMessage( String.format( "Action of %s blocks completed in %s seconds.", wea.getAffected(), ( System.currentTimeMillis() - start ) / 1000f ) );
        } else
            player.sendMessage( "You need a valid selection to do this." );
    }

}

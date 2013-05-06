package com.ikkerens.worldedit.commands;

import com.ikkerens.worldedit.WorldEditPlugin;
import com.ikkerens.worldedit.exceptions.BlockNotFoundException;
import com.ikkerens.worldedit.handlers.AbstractCommand;
import com.ikkerens.worldedit.model.MatchBlockType;
import com.ikkerens.worldedit.model.Selection;
import com.ikkerens.worldedit.model.SettingBlockType;
import com.mbserver.api.dynamic.BlockManager;
import com.mbserver.api.game.Location;
import com.mbserver.api.game.Player;
import com.mbserver.api.game.World;

public class ReplaceCommand extends AbstractCommand {

    public ReplaceCommand( WorldEditPlugin plugin ) {
        super( plugin );
    }

    @Override
    protected void execute( String label, Player player, String[] args ) {
        if ( args.length != 2 ) {
            player.sendMessage( "Usage: /" + label + " <match> <replacement>" );
            return;
        }

        Selection sel = this.getSelection( player );
        if ( sel.isValid() ) {
            Location lowest = sel.getMinimumPosition();
            Location highest = sel.getMaximumPosition();

            World world = lowest.getWorld();
            SettingBlockType type;
            MatchBlockType match;
            try {
                type = new SettingBlockType( args[ 1 ] );
                match = new MatchBlockType( args[ 0 ] );
            } catch ( BlockNotFoundException e ) {
                player.sendMessage( e.getMessage() );
                return;
            }

            long start = System.currentTimeMillis();

            BlockManager bm = this.getPlugin().getServer().getBlockManager();
            for ( int z = lowest.getBlockZ(); z <= highest.getBlockZ(); z++ )
                for ( int y = lowest.getBlockY(); y <= highest.getBlockY(); y++ )
                    for ( int x = lowest.getBlockX(); x <= highest.getBlockX(); x++ )
                        if ( match.matches( world.getBlockID( x, y, z ) ) ) {
                            short tS = type.getNextBlock();
                            if ( bm.getBlockType( tS ).isTransparent() )
                                world.setBlock( x, y, z, tS );
                            else
                                world.setBlockWithoutUpdate( x, y, z, tS );
                        }

            player.sendMessage( String.format( "Action completed in %s seconds.", ( System.currentTimeMillis() - start ) / 1000f ) );
        } else
            player.sendMessage( "You need a valid selection to do this." );
    }

}
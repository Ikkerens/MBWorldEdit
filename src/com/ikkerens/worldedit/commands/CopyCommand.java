package com.ikkerens.worldedit.commands;

import com.ikkerens.worldedit.WorldEditPlugin;
import com.ikkerens.worldedit.handlers.ActionCommand;
import com.ikkerens.worldedit.model.Clipboard;
import com.ikkerens.worldedit.model.Selection;
import com.ikkerens.worldedit.model.Session;
import com.mbserver.api.game.Location;
import com.mbserver.api.game.Player;
import com.mbserver.api.game.World;

public class CopyCommand extends ActionCommand<WorldEditPlugin> {

    public CopyCommand( WorldEditPlugin plugin ) {
        super( plugin );
    }

    @Override
    protected void execute( String label, Player player, String[] args ) {
        Session session = this.getSession( player );
        Selection sel = session.getSelection();
        if ( sel.isValid() ) {
            Location lowest = sel.getMinimumPosition();
            Location highest = sel.getMaximumPosition();
            World world = lowest.getWorld();

            short[][][] clp = new short[ highest.getBlockX() - lowest.getBlockX() + 1 ][ highest.getBlockY() - lowest.getBlockY() + 1 ][ highest.getBlockZ() - lowest.getBlockZ() + 1 ];

            for ( int x = lowest.getBlockX(); x <= highest.getBlockX(); x++ )
                for ( int y = lowest.getBlockY(); y <= highest.getBlockY(); y++ )
                    for ( int z = lowest.getBlockZ(); z <= highest.getBlockZ(); z++ )
                        clp[ x - lowest.getBlockX() ][ y - lowest.getBlockY() ][ z - lowest.getBlockZ() ] = world.getBlockID( x, y, z );
            Location pLoc = player.getLocation();
            int x = lowest.getBlockX() - pLoc.getBlockX();
            int y = lowest.getBlockY() - pLoc.getBlockY();
            int z = lowest.getBlockZ() - pLoc.getBlockZ();

            Clipboard clipboard = new Clipboard( x, y, z, clp );
            session.setClipboard( clipboard );

            player.sendMessage( "Blocks copied" );
        } else
            player.sendMessage( NEED_SELECTION );

    }
}

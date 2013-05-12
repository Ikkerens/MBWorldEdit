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

public class PyramidCommand extends ActionCommand {

    public PyramidCommand( WorldEditPlugin plugin ) {
        super( plugin );
    }

    @Override
    protected void execute( String label, Player player, String[] args ) {
        if ( args.length != 2 ) {
            player.sendMessage( "Usage: /" + label + " <block> <height>" );
            return;
        }

        SetBlockType type;
        try {
            type = new SetBlockType( args[ 0 ] );
        } catch ( BlockNotFoundException e ) {
            player.sendMessage( e.getMessage() );
            return;
        }

        int height;
        try {
            height = Integer.parseInt( args[ 1 ] );
        } catch ( NumberFormatException e ) {
            player.sendMessage( "Invalid number: " + args[ 1 ] );
            return;
        }

        Session session = this.getSession( player );
        Selection sel = session.getSelection();

        Location center = sel.getPosition1() != null ? sel.getPosition1() : ( sel.getPosition2() != null ? sel.getPosition2() : null );
        if ( center == null ) {
            player.sendMessage( "You need to mark a center with position 1 to create a sphere." );
            return;
        }

        Selection cSel = new Selection( null );
        cSel.setPositions( center, center.add( height, height, height, true ) );

        long start = System.currentTimeMillis();
        WEAction wea = session.newAction( center.getWorld(), cSel.getCount() );

        try {
            this.generatePyramid( wea, center, type, height, label.equalsIgnoreCase( "/pyramid" ) );
            wea.finish();
        } catch ( BlockLimitException e ) {
            player.sendMessage( String.format( FINISHED_LIMIT, wea.getAffected(), ( System.currentTimeMillis() - start ) / 1000f ) );
            return;
        }

        player.sendMessage( String.format( FINISHED_DONE, wea.getAffected(), ( System.currentTimeMillis() - start ) / 1000f ) );
    }

    private void generatePyramid( WEAction wea, Location center, SetBlockType type, int size, boolean filled ) throws BlockLimitException {
        int height = size;

        int cX = center.getBlockX();
        int cY = center.getBlockY();
        int cZ = center.getBlockZ();

        for ( int y = 0; y <= height; ++y ) {
            size--;
            for ( int x = 0; x <= size; ++x )
                for ( int z = 0; z <= size; ++z )
                    if ( ( filled && z <= size && x <= size ) || z == size || x == size ) {
                        wea.setBlock( cX + x, cY + y, cZ + z, type );
                        wea.setBlock( cX - x, cY + y, cZ + z, type );
                        wea.setBlock( cX + x, cY + y, cZ - z, type );
                        wea.setBlock( cX - x, cY + y, cZ - z, type );
                    }
        }
    }
}

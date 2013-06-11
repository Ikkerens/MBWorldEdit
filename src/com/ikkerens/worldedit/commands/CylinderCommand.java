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

public class CylinderCommand extends ActionCommand< WorldEditPlugin > {

    public CylinderCommand( final WorldEditPlugin plugin ) {
        super( plugin );
    }

    @Override
    protected void execute( final String label, final Player player, final String[] args ) {
        if ( ( args.length != 3 ) && ( args.length != 4 ) ) {
            player.sendMessage( "Usage: /" + label + " <block> <height> <radiusX> [radiusZ]" );
            return;
        }

        SetBlockType type;
        try {
            type = new SetBlockType( args[ 0 ] );
        } catch ( final BlockNotFoundException e ) {
            player.sendMessage( e.getMessage() );
            return;
        }

        int height, rX, rZ;
        try {
            height = Integer.parseInt( args[ 1 ] );
            rX = Integer.parseInt( args[ 2 ] );

            if ( args.length == 4 )
                rZ = Integer.parseInt( args[ 3 ] );
            else
                rZ = rX;
        } catch ( final NumberFormatException e ) {
            player.sendMessage( "Invalid number" );
            return;
        }

        final Session session = this.getSession( player );
        final Selection sel = session.getSelection();

        final Location center = sel.getPosition1() != null ? sel.getPosition1() : ( sel.getPosition2() != null ? sel.getPosition2() : null );
        if ( center == null ) {
            player.sendMessage( "You need to mark a center with position 1 to create a sphere." );
            return;
        }

        final Selection cSel = new Selection( null );
        cSel.setPositions( center, center.add( rX, height, rZ, true ) );

        final long start = System.currentTimeMillis();
        final WEAction wea = session.newAction( center.getWorld(), cSel.getCount() );

        try {
            this.generateCylinder( wea, center, type, height, rX, rZ, label.equalsIgnoreCase( "/cylinder" ) );
            wea.finish();
        } catch ( final BlockLimitException e ) {
            player.sendMessage( String.format( FINISHED_LIMIT, wea.getAffected(), ( System.currentTimeMillis() - start ) / 1000f ) );
            return;
        }

        player.sendMessage( String.format( FINISHED_DONE, wea.getAffected(), ( System.currentTimeMillis() - start ) / 1000f ) );
    }

    private void generateCylinder( final WEAction wea, Location center, final SetBlockType type, int height, int rX, int rZ, final boolean filled ) throws BlockLimitException {
        rX += 0.5;
        rZ += 0.5;

        if ( height == 0 )
            return;
        else if ( height < 0 ) {
            height = -height;
            center = center.add( 0, -height, 0, true );
        }

        if ( center.getBlockY() < 0 )
            center.setY( 0 );
        else if ( ( ( center.getBlockY() + height ) - 1 ) > 127 )
            height = 128 - center.getBlockY();

        final int cX = center.getBlockX();
        final int cY = center.getBlockY();
        final int cZ = center.getBlockZ();

        final double invRadiusX = 1 / rX;
        final double invRadiusZ = 1 / rZ;

        final int ceilRadiusX = (int) Math.ceil( rX );
        final int ceilRadiusZ = (int) Math.ceil( rZ );

        double nextXn = 0;
        forX: for ( int x = 0; x <= ceilRadiusX; ++x ) {
            final double xn = nextXn;
            nextXn = ( x + 1 ) * invRadiusX;
            double nextZn = 0;
            forZ: for ( int z = 0; z <= ceilRadiusZ; ++z ) {
                final double zn = nextZn;
                nextZn = ( z + 1 ) * invRadiusZ;

                final double distanceSq = this.distanceCalc( xn, zn );
                if ( distanceSq > 1 ) {
                    if ( z == 0 )
                        break forX;
                    break forZ;
                }

                if ( !filled )
                    if ( ( this.distanceCalc( nextXn, zn ) <= 1 ) && ( this.distanceCalc( xn, nextZn ) <= 1 ) )
                        continue;

                for ( int y = 0; y < height; ++y ) {
                    wea.setBlock( cX + x, cY + y, cZ + z, type );
                    wea.setBlock( cX - x, cY + y, cZ + z, type );
                    wea.setBlock( cX + x, cY + y, cZ - z, type );
                    wea.setBlock( cX - x, cY + y, cZ - z, type );
                }
            }
        }
    }

    private double distanceCalc( final double x, final double z ) {
        return ( x * x ) + ( z * z );
    }
}

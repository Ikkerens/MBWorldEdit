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

public class SphereCommand extends ActionCommand< WorldEditPlugin > {

    public SphereCommand( final WorldEditPlugin plugin ) {
        super( plugin );
    }

    @Override
    protected void execute( final String label, final Player player, final String[] args ) {
        if ( ( args.length != 2 ) && ( args.length != 4 ) ) {
            player.sendMessage( "Usage: /" + label + " <block> <radius> [radiusY radiusZ]" );
            return;
        }

        final Session session = this.getSession( player );

        SetBlockType type;
        try {
            type = SetBlockType.from( session, args[ 0 ] );
        } catch ( final BlockNotFoundException e ) {
            player.sendMessage( e.getMessage() );
            return;
        }

        int rX, rY, rZ;
        try {
            rX = Integer.parseInt( args[ 1 ] );

            if ( args.length == 4 ) {
                rY = Integer.parseInt( args[ 2 ] );
                rZ = Integer.parseInt( args[ 3 ] );
            } else {
                rY = rX;
                rZ = rX;
            }
        } catch ( final NumberFormatException e ) {
            player.sendMessage( "Invalid number" );
            return;
        }

        final Selection sel = session.getSelection();

        final Location center = sel.getPosition1() != null ? sel.getPosition1() : ( sel.getPosition2() != null ? sel.getPosition2() : null );
        if ( center == null ) {
            player.sendMessage( "You need to mark a center with position 1 to create a sphere." );
            return;
        }

        final Selection cSel = new Selection( null );
        cSel.setPositions( center, center.add( rX, rY, rZ, true ) );

        final long start = System.currentTimeMillis();
        final WEAction wea = session.newAction( center.getWorld(), cSel.getCount() );

        try {
            this.generateSphere( wea, center, type, rX, rY, rZ, label.equalsIgnoreCase( "/sphere" ) );
            wea.finish();
        } catch ( final BlockLimitException e ) {
            player.sendMessage( String.format( FINISHED_LIMIT, wea.getAffected(), ( System.currentTimeMillis() - start ) / 1000f ) );
            return;
        }

        player.sendMessage( String.format( FINISHED_DONE, wea.getAffected(), ( System.currentTimeMillis() - start ) / 1000f ) );
    }

    private void generateSphere( final WEAction wea, final Location center, final SetBlockType type, double rX, double rY, double rZ, final boolean filled ) throws BlockLimitException {
        rX += 0.5;
        rY += 0.5;
        rZ += 0.5;

        final int cX = center.getBlockX();
        final int cY = center.getBlockY();
        final int cZ = center.getBlockZ();

        final double invertedRX = 1 / rX;
        final double invertedRY = 1 / rY;
        final double invertedRZ = 1 / rZ;

        final int ceilRX = (int) Math.ceil( rX );
        final int ceilRY = (int) Math.ceil( rY );
        final int ceilRZ = (int) Math.ceil( rZ );

        double nextX = 0;
        forX: for ( int ix = 0; ix <= ceilRX; ix++ ) {
            final double ax = nextX;
            nextX = ( ix + 1 ) * invertedRX;

            double nextY = 0;
            forY: for ( int iy = 0; iy <= ceilRY; iy++ ) {
                final double ay = nextY;
                nextY = ( iy + 1 ) * invertedRY;

                double nextZ = 0;
                forZ: for ( int iz = 0; iz <= ceilRZ; iz++ ) {
                    final double az = nextZ;
                    nextZ = ( iz + 1 ) * invertedRZ;

                    final double distance = this.distanceCalc( ax, ay, az );

                    if ( distance > 1 ) {
                        if ( iz == 0 ) {
                            if ( iy == 0 )
                                break forX;
                            break forY;
                        }
                        break forZ;
                    }

                    if ( !filled && ( this.distanceCalc( nextX, ay, az ) <= 1 ) && ( this.distanceCalc( ax, nextY, az ) <= 1 ) && ( this.distanceCalc( ax, ay, nextZ ) <= 1 ) )
                        continue;

                    wea.setBlock( cX + ix, cY + iy, cZ + iz, type );
                    wea.setBlock( cX - ix, cY - iy, cZ - iz, type );

                    wea.setBlock( cX - ix, cY + iy, cZ + iz, type );
                    wea.setBlock( cX + ix, cY - iy, cZ + iz, type );
                    wea.setBlock( cX + ix, cY + iy, cZ - iz, type );

                    wea.setBlock( cX - ix, cY - iy, cZ + iz, type );
                    wea.setBlock( cX + ix, cY - iy, cZ - iz, type );
                    wea.setBlock( cX - ix, cY + iy, cZ - iz, type );
                }
            }
        }
    }

    private double distanceCalc( final double x, final double y, final double z ) {
        return ( x * x ) + ( y * y ) + ( z * z );
    }
}

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

public class SphereCommand extends ActionCommand {

    public SphereCommand( WorldEditPlugin plugin ) {
        super( plugin );
    }

    @Override
    protected void execute( String label, Player player, String[] args ) {
        if ( args.length != 2 && args.length != 4 ) {
            player.sendMessage( "Usage: /" + label + " <block> <radius> [radiusY radiusZ]" );
            return;
        }

        SetBlockType type;
        try {
            type = new SetBlockType( args[ 0 ] );
        } catch ( BlockNotFoundException e ) {
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
        } catch ( NumberFormatException e ) {
            player.sendMessage( "Invalid number" );
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
        cSel.setPositions( center, center.add( rX, rY, rZ, true ) );

        long start = System.currentTimeMillis();
        WEAction wea = session.newAction( center.getWorld(), cSel.getCount() );

        try {
            this.generateSphere( wea, center, type, rX, rY, rZ, label.equalsIgnoreCase( "/sphere" ) );
            wea.finish();
        } catch ( BlockLimitException e ) {
            player.sendMessage( String.format( FINISHED_LIMIT, wea.getAffected(), ( System.currentTimeMillis() - start ) / 1000f ) );
            return;
        }

        player.sendMessage( String.format( FINISHED_DONE, wea.getAffected(), ( System.currentTimeMillis() - start ) / 1000f ) );
    }

    private void generateSphere( WEAction wea, Location center, SetBlockType type, double rX, double rY, double rZ, boolean filled ) throws BlockLimitException {
        rX += 0.5;
        rY += 0.5;
        rZ += 0.5;

        int cX = center.getBlockX();
        int cY = center.getBlockY();
        int cZ = center.getBlockZ();

        double invertedRX = 1 / rX;
        double invertedRY = 1 / rY;
        double invertedRZ = 1 / rZ;

        int ceilRX = (int) Math.ceil( rX );
        int ceilRY = (int) Math.ceil( rY );
        int ceilRZ = (int) Math.ceil( rZ );

        double nextX = 0;
        forX: for ( int ix = 0; ix <= ceilRX; ix++ ) {
            double ax = nextX;
            nextX = ( ix + 1 ) * invertedRX;

            double nextY = 0;
            forY: for ( int iy = 0; iy <= ceilRY; iy++ ) {
                double ay = nextY;
                nextY = ( iy + 1 ) * invertedRY;

                double nextZ = 0;
                forZ: for ( int iz = 0; iz <= ceilRZ; iz++ ) {
                    double az = nextZ;
                    nextZ = ( iz + 1 ) * invertedRZ;

                    double distance = distanceCalc( ax, ay, az );

                    if ( distance > 1 ) {
                        if ( iz == 0 ) {
                            if ( iy == 0 )
                                break forX;
                            break forY;
                        }
                        break forZ;
                    }

                    if ( !filled && distanceCalc( nextX, ay, az ) <= 1 && distanceCalc( ax, nextY, az ) <= 1 && distanceCalc( ax, ay, nextZ ) <= 1 )
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

    private double distanceCalc( double x, double y, double z ) {
        return ( x * x ) + ( y * y ) + ( z * z );
    }
}

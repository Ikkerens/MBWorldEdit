package com.ikkerens.worldedit.commands;

import com.ikkerens.worldedit.WorldEditPlugin;
import com.ikkerens.worldedit.exceptions.BlockLimitException;
import com.ikkerens.worldedit.exceptions.BlockNotFoundException;
import com.ikkerens.worldedit.handlers.ActionCommand;
import com.ikkerens.worldedit.model.Selection;
import com.ikkerens.worldedit.model.Session;
import com.ikkerens.worldedit.model.WEAction;
import com.ikkerens.worldedit.model.events.SinglePointActionEvent;
import com.ikkerens.worldedit.model.pattern.SetBlockType;

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

        final Session session = this.getPlugin().getSession( player );

        SetBlockType type;
        try {
            type = SetBlockType.from( session, args[ 0 ] );
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

        final Selection sel = session.getSelection();

        final Location center = sel.getPosition1() != null ? sel.getPosition1() : ( sel.getPosition2() != null ? sel.getPosition2() : null );
        if ( center == null ) {
            player.sendMessage( "You need to mark a center with position 1 to create a cylinder." );
            return;
        }

        final CylinderActionEvent event = new CylinderActionEvent( player, type, center, height, rX, rZ, label.equalsIgnoreCase( "/cylinder" ) );
        this.getPlugin().getPluginManager().triggerEvent( event );

        if ( !event.isCancelled() ) {
            final Selection cSel = new Selection( null );
            cSel.setPositions( center, center.add( rX, height, rZ ) );

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
    }

    private void generateCylinder( final WEAction wea, Location center, final SetBlockType type, int height, double radiusX, double radiusZ, final boolean filled ) throws BlockLimitException {
        radiusX += 0.5;
        radiusZ += 0.5;

        if ( height == 0 )
            return;
        else if ( height < 0 ) {
            height = -height;
            center = center.add( 0, -height, 0 );
        }

        if ( center.getBlockY() < 0 )
            center = center.add( 0, -center.getY(), 0 );
        else if ( ( ( center.getBlockY() + height ) - 1 ) > 127 )
            height = 128 - center.getBlockY();

        final int cX = center.getBlockX();
        final int cY = center.getBlockY();
        final int cZ = center.getBlockZ();

        final double invRadiusX = 1 / radiusX;
        final double invRadiusZ = 1 / radiusZ;

        final int ceilRadiusX = (int) Math.ceil( radiusX );
        final int ceilRadiusZ = (int) Math.ceil( radiusZ );

        double nextXn = 0;
        forX : for ( int x = 0; x <= ceilRadiusX; ++x ) {
            final double xn = nextXn;
            nextXn = ( x + 1 ) * invRadiusX;

            double nextZn = 0;
            forZ : for ( int z = 0; z <= ceilRadiusZ; ++z ) {
                final double zn = nextZn;
                nextZn = ( z + 1 ) * invRadiusZ;

                final double distance = this.distanceCalc( xn, zn );
                if ( distance > 1 ) {
                    if ( z == 0 )
                        break forX;
                    break forZ;
                }

                if ( !filled )
                    if ( ( this.distanceCalc( nextXn, zn ) <= 1 ) && ( this.distanceCalc( xn, nextZn ) <= 1 ) )
                        continue;

                for ( int y = 0; y < height; y++ ) {
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

    public static class CylinderActionEvent extends SinglePointActionEvent {
        private final int     height;
        private final double  radiusX, radiusZ;
        private final boolean filled;

        public CylinderActionEvent( final Player player, final SetBlockType type, final Location point, final int height, final double radiusX, final double radiusZ, final boolean filled ) {
            super( player, type, point );
            this.height = height;
            this.radiusX = radiusX;
            this.radiusZ = radiusZ;
            this.filled = filled;
        }

        public int getHeight() {
            return this.height;
        }

        public double getRadiusX() {
            return this.radiusX;
        }

        public double getRadiusZ() {
            return this.radiusZ;
        }

        public boolean isFilled() {
            return this.filled;
        }

    }
}

package com.ikkerens.worldedit;

import com.mbserver.api.Constructors;
import com.mbserver.api.game.Location;
import com.mbserver.api.game.World;

public class Util {
    private Util() {
        throw new UnsupportedOperationException();
    }

    public static double distanceCalc( final double x, final double z ) {
        return ( x * x ) + ( z * z );
    }

    public static double distanceCalc( final double x, final double y, final double z ) {
        return ( x * x ) + ( y * y ) + ( z * z );
    }

    public static Location newLocation( final World world, final float x, float y, final float z ) {
        if ( y > 127 )
            y = 127;
        else if ( y < 0 )
            y = 0;
        return Constructors.newLocation( world, x, y, z );
    }
}

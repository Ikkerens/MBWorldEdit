package com.ikkerens.worldedit.wand;

import com.mbserver.api.game.Location;

public enum Direction {
    UP ( 0, 1, 0 ),
    DOWN ( 0, -1, 0 ),
    EAST ( 1, 0, 0 ),
    WEST ( -1, 0, 0 ),
    NORTH ( 0, 0, 1 ),
    SOUTH ( 0, 0, -1 );

    private int x, y, z;

    private Direction( final int x, final int y, final int z ) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public Location addToLocation( final Location location, final int amount ) {
        return location.add( this.x * amount, this.y * amount, this.z * amount, false );
    }
}

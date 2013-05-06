package com.ikkerens.worldedit.model;

import com.mbserver.api.Constructors;
import com.mbserver.api.game.Location;

public class Selection {
    private Session session;
    private Location pos1, pos2;

    public Selection( Session session ) {
        this.session = session;
        this.pos1 = null;
        this.pos2 = null;
    }

    public void setPosition1( Location pos ) {
        if ( !pos.equals( this.pos1 ) ) {
            this.pos1 = pos;

            if ( this.pos2 != null && this.pos1.getWorld() != this.pos2.getWorld() )
                this.pos2 = null;

            this.inform();
        }
    }

    public void setPosition2( Location pos ) {
        if ( !pos.equals( this.pos2 ) ) {
            this.pos2 = pos;

            if ( this.pos1 != null && this.pos2.getWorld() != this.pos1.getWorld() )
                this.pos1 = null;

            this.inform();
        }
    }

    public void setPositions( Location pos1, Location pos2 ) {
        this.pos1 = pos1;
        this.pos2 = pos2;
    }

    public boolean isValid() {
        return this.pos1 != null && this.pos2 != null;
    }

    public int getCount() {
        int count;

        if ( this.pos1 != null && this.pos2 != null ) {
            Location pos1 = this.getMinimumPosition();
            Location pos2 = this.getMaximumPosition();
            count = ( pos2.getBlockX() - pos1.getBlockX() + 1 ) * ( pos2.getBlockY() - pos1.getBlockY() + 1 ) * ( pos2.getBlockZ() - pos1.getBlockZ() + 1 );
        } else
            count = 0;

        return count;
    }

    public Location getMinimumPosition() {
        return Constructors.newLocation( this.pos1.getWorld(), Math.min( this.pos1.getX(), this.pos2.getX() ), Math.min( this.pos1.getY(), this.pos2.getY() ), Math.min( this.pos1.getZ(), this.pos2.getZ() ) );
    }

    public Location getMaximumPosition() {
        return Constructors.newLocation( this.pos1.getWorld(), Math.max( this.pos1.getX(), this.pos2.getX() ), Math.max( this.pos1.getY(), this.pos2.getY() ), Math.max( this.pos1.getZ(), this.pos2.getZ() ) );
    }

    private void inform() {
        String pos1text, pos2text;

        if ( this.pos1 != null )
            pos1text = String.format( "(%s,%s,%s)", this.pos1.getBlockX(), this.pos1.getBlockY(), this.pos1.getBlockZ() );
        else
            pos1text = "(Not set)";

        if ( this.pos2 != null )
            pos2text = String.format( "(%s,%s,%s)", this.pos2.getBlockX(), this.pos2.getBlockY(), this.pos2.getBlockZ() );
        else
            pos2text = "(Not set)";

        this.session.getPlayer().sendMessage( String.format( "Selection: %s to %s (Count: %s)", pos1text, pos2text, this.getCount() ) );
    }
}

package com.ikkerens.worldedit.model;

import com.mbserver.api.Constructors;
import com.mbserver.api.dynamic.UILine;
import com.mbserver.api.game.Location;
import com.mbserver.api.game.Player;
import com.mbserver.api.game.World;

public class Selection {
    private final Session session;
    private Location      pos1, pos2;

    public Selection( final Session session ) {
        this.session = session;
        this.pos1 = null;
        this.pos2 = null;
    }

    public void setPosition1( final Location pos ) {
        if ( !pos.equals( this.pos1 ) ) {
            this.pos1 = pos;

            if ( ( this.pos2 != null ) && ( this.pos1.getWorld() != this.pos2.getWorld() ) )
                this.pos2 = null;

            this.inform();
        }
    }

    public void setPosition2( final Location pos ) {
        if ( !pos.equals( this.pos2 ) ) {
            this.pos2 = pos;

            if ( ( this.pos1 != null ) && ( this.pos2.getWorld() != this.pos1.getWorld() ) )
                this.pos1 = null;

            this.inform();
        }
    }

    public void setPositions( final Location pos1, final Location pos2 ) {
        this.pos1 = pos1;
        this.pos2 = pos2;
    }

    public boolean isValid() {
        return ( this.pos1 != null ) && ( this.pos2 != null );
    }

    public int getCount() {
        int count;

        if ( ( this.pos1 != null ) && ( this.pos2 != null ) ) {
            final Location pos1 = this.getMinimumPosition();
            final Location pos2 = this.getMaximumPosition();
            count = ( ( pos2.getBlockX() - pos1.getBlockX() ) + 1 ) * ( ( pos2.getBlockY() - pos1.getBlockY() ) + 1 ) * ( ( pos2.getBlockZ() - pos1.getBlockZ() ) + 1 );
        } else
            count = 0;

        return count;
    }

    public World getWorld() {
        return this.pos1.getWorld();
    }

    public Location getPosition1() {
        return this.pos1;
    }

    public Location getPosition2() {
        return this.pos2;
    }

    public Location getMinimumPosition() {
        return Constructors.newLocation( this.pos1.getWorld(), Math.min( this.pos1.getX(), this.pos2.getX() ), Math.min( this.pos1.getY(), this.pos2.getY() ), Math.min( this.pos1.getZ(), this.pos2.getZ() ) );
    }

    public Location getMaximumPosition() {
        return Constructors.newLocation( this.pos1.getWorld(), Math.max( this.pos1.getX(), this.pos2.getX() ), Math.max( this.pos1.getY(), this.pos2.getY() ), Math.max( this.pos1.getZ(), this.pos2.getZ() ) );
    }

    public void inform() {
        final String pos1text = this.posText( this.pos1 );
        final String pos2text = this.posText( this.pos2 );

        this.session.getPlayer().sendMessage( String.format( "Selection: %s to %s (Count: %s)", pos1text, pos2text, this.getCount() ) );

        // Build lines
        this.session.getPlayer().clearLines();
        if ( this.isValid() ) {
            final Location pos1 = this.getMinimumPosition();
            final Location pos2 = this.getMaximumPosition().add( 1, 1, 1 );
            final World world = pos1.getWorld();
            final Player player = this.session.getPlayer();

            final Location pos3, pos4, pos5, pos6, pos7, pos8;
            // Bottom positions
            pos3 = Constructors.newLocation( world, pos2.getBlockX(), pos1.getBlockY(), pos1.getBlockZ() );
            pos4 = Constructors.newLocation( world, pos1.getBlockX(), pos1.getBlockY(), pos2.getBlockZ() );
            pos5 = Constructors.newLocation( world, pos2.getBlockX(), pos1.getBlockY(), pos2.getBlockZ() );

            // Upper positions
            pos6 = Constructors.newLocation( world, pos1.getBlockX(), pos2.getBlockY(), pos2.getBlockZ() );
            pos7 = Constructors.newLocation( world, pos2.getBlockX(), pos2.getBlockY(), pos1.getBlockZ() );
            pos8 = Constructors.newLocation( world, pos1.getBlockX(), pos2.getBlockY(), pos1.getBlockZ() );

            // Bottom lines
            player.drawLine( new UILine( pos1, pos3 ), false );
            player.drawLine( new UILine( pos1, pos4 ), false );
            player.drawLine( new UILine( pos3, pos5 ), false );
            player.drawLine( new UILine( pos4, pos5 ), false );

            // Upper lines
            player.drawLine( new UILine( pos2, pos6 ), false );
            player.drawLine( new UILine( pos2, pos7 ), false );
            player.drawLine( new UILine( pos6, pos8 ), false );
            player.drawLine( new UILine( pos7, pos8 ), false );

            // Side lines
            player.drawLine( new UILine( pos1, pos8 ), false );
            player.drawLine( new UILine( pos3, pos7 ), false );
            player.drawLine( new UILine( pos4, pos6 ), false );
            player.drawLine( new UILine( pos5, pos2 ), true );
        }
    }

    private String posText( final Location pos ) {
        return pos != null ? String.format( "(%s,%s,%s)", pos.getBlockX(), pos.getBlockY(), pos.getBlockZ() ) : "(Not set)";
    }
}

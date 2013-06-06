package com.ikkerens.worldedit.handlers;

import com.ikkerens.worldedit.exceptions.BlockNotFoundException;

import com.mbserver.api.game.Material;

public abstract class Parser {
    protected short getItemID( final String arg ) throws BlockNotFoundException {
        try {
            return Short.parseShort( arg );
        } catch ( final NumberFormatException e ) {
            try {
                final Material m = Material.valueOf( arg.toUpperCase() );
                if ( m.getBlockType() == null )
                    throw new BlockNotFoundException( String.format( "%s is not a block.", arg ) );
                return m.getID();
            } catch ( final IllegalArgumentException ignored ) {
            }
        }

        throw new BlockNotFoundException( String.format( "Block %s not found.", arg ) );
    }
}

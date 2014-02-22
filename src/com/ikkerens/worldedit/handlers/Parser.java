package com.ikkerens.worldedit.handlers;

import com.ikkerens.worldedit.exceptions.BlockNotFoundException;

import com.mbserver.api.game.Material;

public abstract class Parser {
    protected short getItemID( final String arg ) throws BlockNotFoundException {
        try {
            return Short.parseShort( arg );
        } catch ( final NumberFormatException e ) {
            Material found = null;
            final String upperName = arg.toUpperCase();
            int delta = Integer.MAX_VALUE;

            for ( final Material material : Material.values() )
                if ( material.name().startsWith( upperName ) ) {
                    final int curDelta = material.name().length() - upperName.length();
                    if ( curDelta < delta ) {
                        found = material;
                        delta = curDelta;
                    }

                    if ( curDelta == 0 )
                        break;
                }

            if ( found == null )
                throw new BlockNotFoundException( String.format( "%s is not a block.", arg ) );

            return found.getID();
        }
    }
}

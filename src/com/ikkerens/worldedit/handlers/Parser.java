package com.ikkerens.worldedit.handlers;

import com.ikkerens.worldedit.exceptions.BlockNotFoundException;
import com.mbserver.api.game.Material;

public abstract class Parser {
    protected short getItemID( String arg ) throws BlockNotFoundException {
        try {
            return Short.parseShort( arg );
        } catch ( NumberFormatException e ) {
            try {
                return Material.valueOf( arg.toUpperCase() ).getID();
            } catch ( IllegalArgumentException ignored ) {
            }
        }

        throw new BlockNotFoundException( String.format( "Block %s not found.", arg ) );
    }
}

package com.ikkerens.worldedit.model.pattern;

import java.util.HashSet;

import com.ikkerens.worldedit.exceptions.BlockNotFoundException;
import com.ikkerens.worldedit.handlers.Parser;

public class MatchBlockType extends Parser {
    private final HashSet< Short > ids;

    public MatchBlockType( final String rawBlocks ) throws BlockNotFoundException {
        this.ids = new HashSet< Short >();
        final String[] blocks = rawBlocks.split( "," );

        for ( final String block : blocks )
            this.ids.add( this.getItemID( block ) );
    }

    public boolean matches( final short id ) {
        return this.ids.contains( id );
    }
}

package com.ikkerens.worldedit.model.pattern;

import java.util.HashSet;
import java.util.Iterator;

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

    public short[] getBlockIDs() {
        final short[] returnVal = new short[ this.ids.size() ];

        int i = 0;
        final Iterator< Short > it = this.ids.iterator();
        while ( it.hasNext() )
            returnVal[ i++ ] = it.next();

        return returnVal;
    }
}

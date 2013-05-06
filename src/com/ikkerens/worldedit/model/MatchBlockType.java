package com.ikkerens.worldedit.model;

import java.util.HashSet;

import com.ikkerens.worldedit.handlers.Parser;
import com.ikkerens.worldedit.wand.BlockNotFoundException;

public class MatchBlockType extends Parser {
    private HashSet< Short > ids;

    public MatchBlockType( String rawBlocks ) throws BlockNotFoundException {
        this.ids = new HashSet< Short >();
        String[] blocks = rawBlocks.split( "," );

        for ( String block : blocks ) {
            this.ids.add( this.getItemID( block ) );
        }
    }

    public boolean matches( short id ) {
        return this.ids.contains( id );
    }
}

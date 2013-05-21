package com.ikkerens.worldedit.model;

import java.util.Random;
import java.util.TreeMap;

import com.ikkerens.worldedit.exceptions.BlockNotFoundException;
import com.ikkerens.worldedit.handlers.Parser;

public class SetBlockType extends Parser {
    private final Random                  random;
    private final TreeMap< Float, Short > validBlocks;
    private float                         total;

    public SetBlockType( final String rawBlocks ) throws BlockNotFoundException {
        this.random = new Random();
        this.validBlocks = new TreeMap< Float, Short >();
        final String[] blocks = rawBlocks.split( "," );

        for ( final String block : blocks ) {
            final String[] perc = block.split( "%" );

            float chance;
            String blk;

            if ( perc.length >= 2 ) {
                chance = Float.parseFloat( perc[ 0 ] );
                blk = perc[ 1 ];
            } else {
                chance = 100;
                blk = perc[ 0 ];
            }

            final short id = this.getItemID( blk );
            this.validBlocks.put( this.total, id );
            this.total += chance;
        }
    }

    public short getNextBlock() {
        return this.validBlocks.floorEntry( this.random.nextFloat() * this.total ).getValue();
    }
}

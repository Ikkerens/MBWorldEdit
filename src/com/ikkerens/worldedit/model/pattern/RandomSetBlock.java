package com.ikkerens.worldedit.model.pattern;

import java.util.Collection;
import java.util.Iterator;
import java.util.Random;
import java.util.TreeMap;

import com.ikkerens.worldedit.exceptions.BlockNotFoundException;

class RandomSetBlock extends SetBlockType {
    private final Random                  random;
    private final TreeMap< Float, Short > validBlocks;
    private float                         total;

    RandomSetBlock( final String rawBlocks ) throws BlockNotFoundException {
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

    @Override
    public short getNextBlock( final int x, final int y, final int z ) {
        return this.validBlocks.floorEntry( this.random.nextFloat() * this.total ).getValue();
    }

    @Override
    public short[] getBlockIDs() {
        final Collection< Short > values = this.validBlocks.values();
        final short[] returnVal = new short[ values.size() ];

        int i = 0;
        final Iterator< Short > it = values.iterator();
        while ( it.hasNext() )
            returnVal[ i++ ] = it.next();

        return returnVal;
    }
}

package com.ikkerens.worldedit.model;

import java.util.Random;
import java.util.TreeMap;

import com.ikkerens.worldedit.handlers.Parser;
import com.ikkerens.worldedit.wand.BlockNotFoundException;

public class SettingBlockType extends Parser {
    private Random                  random;
    private TreeMap< Float, Short > validBlocks;
    private float                   total;

    public SettingBlockType( String rawBlocks ) throws BlockNotFoundException {
        this.random = new Random();
        this.validBlocks = new TreeMap< Float, Short >();
        String[] blocks = rawBlocks.split( "," );

        for ( String block : blocks ) {
            String[] perc = block.split( "%" );

            float chance;
            String blk;

            if ( perc.length >= 2 ) {
                chance = Float.parseFloat( perc[ 0 ] );
                blk = perc[ 1 ];
            } else {
                chance = 100;
                blk = perc[ 0 ];
            }

            short id = this.getItemID( blk );
            this.validBlocks.put( total, id );
            this.total += chance;
        }
    }

    public short getNextBlock() {
        return this.validBlocks.floorEntry( this.random.nextFloat() * total ).getValue();
    }
}

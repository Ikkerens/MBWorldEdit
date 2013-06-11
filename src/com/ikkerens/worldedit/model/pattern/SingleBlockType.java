package com.ikkerens.worldedit.model.pattern;

import com.ikkerens.worldedit.exceptions.BlockNotFoundException;

public class SingleBlockType extends SetBlockType {
    private final short block;

    public SingleBlockType( final String arg ) throws BlockNotFoundException {
        this.block = this.getItemID( arg );
    }

    @Override
    public short getNextBlock( final int x, final int y, final int z ) {
        return this.block;
    }

}

package com.ikkerens.worldedit.model;

import java.util.AbstractMap.SimpleEntry;
import java.util.ArrayList;
import java.util.HashSet;

import com.ikkerens.worldedit.exceptions.BlockLimitException;
import com.mbserver.api.game.Chunk;
import com.mbserver.api.game.World;

public class WEAction {
    private World                                        world;
    private boolean                                      recordAction;
    private int                                          limit;
    private int                                          affected;

    private HashSet< Chunk >                             chunks;
    private ArrayList< SimpleEntry< Integer[], Short > > undoList;

    WEAction( World world, boolean recordAction, int limit ) {
        this.world = world;
        this.recordAction = recordAction;
        this.limit = limit;

        if ( this.recordAction )
            this.undoList = new ArrayList< SimpleEntry< Integer[], Short > >();
    }

    public void setBlock( int x, int y, int z, short blockID ) throws BlockLimitException {
        short current = this.world.getBlockID( x, y, z );

        if ( current != blockID ) {
            if ( this.limit != -1 && this.affected >= this.limit )
                throw new BlockLimitException();

            if ( this.recordAction )
                this.undoList.add( new SimpleEntry< Integer[], Short >( new Integer[] { x, y, z }, current ) );

            this.world.setBlockWithoutUpdate( x, y, z, blockID );

            this.chunks.add( this.world.getChunk( x, y, z, false ) );

            this.affected++;
        }
    }

    public void setBlock( int x, int y, int z, SetBlockType type ) throws BlockLimitException {
        this.setBlock( x, y, z, type.getNextBlock() );
    }

    public void undo() {
        for ( SimpleEntry< Integer[], Short > entry : this.undoList ) {
            Integer[] keys = entry.getKey();
            this.world.setBlockWithoutUpdate( keys[ 0 ], keys[ 1 ], keys[ 2 ], entry.getValue() );
        }
    }

    public void finish() {
        for ( Chunk ch : this.chunks )
            ch.calculateLight();
        this.chunks.clear();
    }

    public int getAffected() {
        return this.affected;
    }
}

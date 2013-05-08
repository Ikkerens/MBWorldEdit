package com.ikkerens.worldedit.model;

import java.util.AbstractMap.SimpleEntry;
import java.util.ArrayList;

import com.ikkerens.worldedit.exceptions.BlockLimitException;
import com.mbserver.api.dynamic.BlockManager;
import com.mbserver.api.game.World;

public class WEAction {
    private final BlockManager                           mgr;

    private World                                        world;
    private boolean                                      recordAction;
    private int                                          limit;
    private int                                          affected;

    private ArrayList< SimpleEntry< Integer[], Short > > undoList;

    WEAction( BlockManager mgr, World world, boolean recordAction, int limit ) {
        this.mgr = mgr;
        this.world = world;
        this.recordAction = recordAction;
        this.limit = limit;

        if ( this.recordAction )
            this.undoList = new ArrayList< SimpleEntry< Integer[], Short > >();
    }

    public void setBlock( int x, int y, int z, SetBlockType type ) throws BlockLimitException {
        short current = this.world.getBlockID( x, y, z );
        short next = type.getNextBlock();

        if ( current != next ) {
            if ( this.recordAction )
                this.undoList.add( new SimpleEntry< Integer[], Short >( new Integer[] { x, y, z }, current ) );

            if ( this.mgr.getBlockType( next ).isTransparent() )
                this.world.setBlock( x, y, z, next );
            else
                this.world.setBlockWithoutUpdate( x, y, z, next );

            if ( this.limit != -1 && this.affected >= this.limit )
                throw new BlockLimitException();

            this.affected++;
        }
    }

    public void undo() {
        for ( SimpleEntry< Integer[], Short > entry : this.undoList ) {
            Integer[] keys = entry.getKey();
            this.world.setBlockWithoutUpdate( keys[ 0 ], keys[ 1 ], keys[ 2 ], entry.getValue() );
        }
    }

    public int getAffected() {
        return this.affected;
    }
}

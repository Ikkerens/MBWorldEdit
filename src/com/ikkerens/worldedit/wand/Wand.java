package com.ikkerens.worldedit.wand;

import com.mbserver.api.Constructors;
import com.mbserver.api.dynamic.BlockFace;
import com.mbserver.api.dynamic.BlockManager;
import com.mbserver.api.dynamic.BlockType;
import com.mbserver.api.exceptions.BlockTypeAlreadyRegisteredException;

public class Wand {
    public static final Wand LEFT  = new Wand( 150, 14, 0 );
    public static final Wand RIGHT = new Wand( 151, 14, 1 );

    private final BlockType  type;

    public Wand( final int id, final int x, final int y ) {
        this.type = Constructors.newBlockType( id );

        this.type.addTexture( BlockFace.DEFAULT, x, y );
        // this.type.setCreative( false );
    }

    public void register( final BlockManager mgr ) {
        try {
            mgr.registerBlockType( this.type );
        } catch ( final BlockTypeAlreadyRegisteredException e ) {
            e.fixIt();
        }
    }

    public short getId() {
        return this.type.getID();
    }
}

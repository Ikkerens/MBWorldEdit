package com.ikkerens.worldedit.wand;

import com.mbserver.api.Constructors;
import com.mbserver.api.dynamic.BlockFace;
import com.mbserver.api.dynamic.BlockManager;
import com.mbserver.api.dynamic.BlockType;
import com.mbserver.api.exceptions.BlockTypeAlreadyRegisteredException;

public class Wand {
    public static final Wand LEFT  = new Wand( "Wand Pos 1", "paint6" );
    public static final Wand RIGHT = new Wand( "Wand Pos 2", "paint7" );

    private final BlockType  type;

    public Wand( final String name, final String texture ) {
        this.type = Constructors.newBlockType( 150 );
        this.type.setName( name );

        this.type.addTexture( BlockFace.DEFAULT, texture );
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

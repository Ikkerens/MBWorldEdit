package com.ikkerens.worldedit.wand;

public class BlockNotFoundException extends Exception {
    private static final long serialVersionUID = -7404101035380905175L;

    public BlockNotFoundException( String msg ) {
        super( msg );
    }
}

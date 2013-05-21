package com.ikkerens.worldedit.exceptions;

public class BlockNotFoundException extends Exception {
    private static final long serialVersionUID = -7404101035380905175L;

    public BlockNotFoundException( final String msg ) {
        super( msg );
    }
}

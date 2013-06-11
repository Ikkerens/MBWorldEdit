package com.ikkerens.worldedit.model;

import com.ikkerens.worldedit.exceptions.BlockNotFoundException;
import com.ikkerens.worldedit.handlers.Parser;

public abstract class SetBlockType extends Parser {
    public static SetBlockType from( final Session session, final String arg ) throws BlockNotFoundException {
        if ( arg.equalsIgnoreCase( "#clipboard" ) )
            return new ClipboardSetter( session.getClipboard() );
        else
            return new RandomSetBlock( arg );
    }

    public abstract short getNextBlock( int x, int y, int z );
}

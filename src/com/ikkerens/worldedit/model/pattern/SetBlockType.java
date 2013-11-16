package com.ikkerens.worldedit.model.pattern;

import com.ikkerens.worldedit.exceptions.BlockNotFoundException;
import com.ikkerens.worldedit.handlers.Parser;
import com.ikkerens.worldedit.model.Session;

public abstract class SetBlockType extends Parser {
    public static SetBlockType from( final Session session, final String arg ) throws BlockNotFoundException {
        if ( arg.equalsIgnoreCase( "#clipboard" ) )
            if ( session.getClipboard() != null )
                return new ClipboardSetter( session.getClipboard() );
            else
                throw new BlockNotFoundException( "Your clipboard is empty!" );
        else if ( arg.contains( "," ) )
            return new RandomSetBlock( arg );
        else
            return new SingleBlockType( arg );
    }

    public abstract short getNextBlock( int x, int y, int z );
}
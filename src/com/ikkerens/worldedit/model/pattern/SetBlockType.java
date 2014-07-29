package com.ikkerens.worldedit.model.pattern;

import com.ikkerens.worldedit.exceptions.BlockNotFoundException;
import com.ikkerens.worldedit.handlers.Parser;
import com.ikkerens.worldedit.model.Session;

import com.mbserver.api.game.MBSchematic;

public abstract class SetBlockType extends Parser {
    public static SetBlockType from( final Session session, final String arg ) throws BlockNotFoundException {
        if ( arg.equalsIgnoreCase( "#clipboard" ) )
            if ( session.getClipboard() != null )
                return new ClipboardSetter( session );
            else
                throw new BlockNotFoundException( "Your clipboard is empty!" );
        else if ( arg.contains( "," ) )
            return new RandomSetBlock( arg );
        else
            return new SingleBlockType( arg );
    }

    /**
     * Warning: Clipboard setter will return null because it is simply too heavy for lookups.
     * 
     * @return Array of block IDs used
     * @see MBSchematic#getBlock
     */
    public abstract short[] getBlockIDs();

    public abstract short getNextBlock( int x, int y, int z );
}

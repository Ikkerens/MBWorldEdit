package com.ikkerens.worldedit.model.pattern;

import com.ikkerens.worldedit.model.Session;

import com.mbserver.api.game.Location;
import com.mbserver.api.game.MBSchematic;

class ClipboardSetter extends SetBlockType {
    private final Location    minimumPosition;
    private final int[]       clbSize;
    private final MBSchematic clipboard;

    ClipboardSetter( final Session session ) {
        this.minimumPosition = session.getSelection().getMinimumPosition();
        this.clipboard = session.getClipboard();
        this.clbSize = this.clipboard.getSizes();
    }

    @Override
    public short getNextBlock( final int x, final int y, final int z ) {
        final int xp = Math.abs( x - this.minimumPosition.getBlockX() ) % clbSize[0];
        final int yp = Math.abs( y - this.minimumPosition.getBlockY() ) % clbSize[1];
        final int zp = Math.abs( z - this.minimumPosition.getBlockZ() ) % clbSize[2];

        return this.clipboard.getBlock( xp, yp, zp );
    }

}

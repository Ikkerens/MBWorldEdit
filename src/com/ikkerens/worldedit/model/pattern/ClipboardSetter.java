package com.ikkerens.worldedit.model.pattern;

import com.ikkerens.worldedit.model.Session;

import com.mbserver.api.game.Location;

class ClipboardSetter extends SetBlockType {
    private final Location    minimumPosition;
    private final short[][][] clipboard;

    ClipboardSetter( final Session session ) {
        this.minimumPosition = session.getSelection().getMinimumPosition();
        this.clipboard = session.getClipboard().getBlocks();
    }

    @Override
    public short getNextBlock( final int x, final int y, final int z ) {
        final int xp = Math.abs( x - this.minimumPosition.getBlockX() ) % this.clipboard.length;
        final int yp = Math.abs( y - this.minimumPosition.getBlockY() ) % this.clipboard[ 0 ].length;
        final int zp = Math.abs( z - this.minimumPosition.getBlockZ() ) % this.clipboard[ 0 ][ 0 ].length;

        return this.clipboard[ xp ][ yp ][ zp ];
    }

}

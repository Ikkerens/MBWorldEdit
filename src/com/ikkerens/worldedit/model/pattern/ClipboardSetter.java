package com.ikkerens.worldedit.model.pattern;

import com.ikkerens.worldedit.model.Clipboard;

class ClipboardSetter extends SetBlockType {
    private final short[][][] clipboard;

    ClipboardSetter( final Clipboard clipboard ) {
        this.clipboard = clipboard.getBlocks();
    }

    @Override
    public short getNextBlock( final int x, final int y, final int z ) {
        final int xp = Math.abs( x ) % this.clipboard.length;
        final int yp = Math.abs( y ) % this.clipboard[ 0 ].length;
        final int zp = Math.abs( z ) % this.clipboard[ 0 ][ 0 ].length;

        return this.clipboard[ xp ][ yp ][ zp ];
    }

}

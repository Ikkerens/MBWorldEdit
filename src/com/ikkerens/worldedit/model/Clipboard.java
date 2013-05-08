package com.ikkerens.worldedit.model;

public class Clipboard {
    // Relative towards player
    private int         rX, rY, rZ;
    private short[][][] blocks;

    public Clipboard( int rX, int rY, int rZ, short[][][] blocks ) {
        this.rX = rX;
        this.rY = rY;
        this.rZ = rZ;
        this.blocks = blocks;
    }

    public int getRelativeX() {
        return this.rX;
    }

    public int getRelativeY() {
        return this.rY;
    }

    public int getRelativeZ() {
        return this.rZ;
    }

    public short[][][] getBlocks() {
        return blocks;
    }
}

package com.ikkerens.worldedit.model;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.ShortBuffer;
import java.util.zip.DataFormatException;
import java.util.zip.Deflater;
import java.util.zip.Inflater;

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

    public static Clipboard loadFromFile( String name ) {
        RandomAccessFile file = null;

        try {
            File schemFile = new File( String.format( "plugins/MBWorldEdit/schematics/%s.mbschem", name ) );
            schemFile.getParentFile().mkdirs();

            if ( !schemFile.exists() )
                return null;

            file = new RandomAccessFile( schemFile.getAbsolutePath(), "r" );
            int oSize = file.readInt();
            int cSize = file.readInt();
            byte[] compressed = new byte[ cSize ];
            byte[] decompressed = new byte[ oSize ];

            int loaded = 0;
            while ( loaded != cSize )
                loaded += file.read( compressed, loaded, cSize - loaded );

            Inflater decompressor = new Inflater();
            decompressor.setInput( compressed );
            decompressor.inflate( decompressed );
            decompressor.end();

            ByteBuffer schem = ByteBuffer.wrap( decompressed );

            int rX = schem.getInt();
            int rY = schem.getInt();
            int rZ = schem.getInt();
            short[][][] clp = new short[ schem.getInt() ][ schem.getInt() ][ schem.getInt() ];

            ShortBuffer sBuf = schem.asShortBuffer();

            for ( int x = 0; x < clp.length; x++ )
                for ( int y = 0; y < clp[ 0 ].length; y++ )
                    sBuf.get( clp[ x ][ y ], 0, clp[ x ][ y ].length );

            return new Clipboard( rX, rY, rZ, clp );
        } catch ( IOException e ) {
        } catch ( DataFormatException e ) {
        } finally {
            if ( file != null )
                try {
                    file.close();
                } catch ( IOException e ) {
                }
        }

        return null;
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

    public boolean save( String name ) {
        RandomAccessFile file = null;

        try {
            File schemFile = new File( String.format( "plugins/MBWorldEdit/schematics/%s.mbschem", name ) );
            schemFile.getParentFile().mkdirs();

            if ( schemFile.exists() )
                schemFile.delete();

            ByteBuffer byteBuf = ByteBuffer.allocate( 24 + this.blocks.length * this.blocks[ 0 ].length * this.blocks[ 0 ][ 0 ].length * 2 );

            byteBuf.putInt( this.rX );
            byteBuf.putInt( this.rY );
            byteBuf.putInt( this.rZ );
            byteBuf.putInt( this.blocks.length );
            byteBuf.putInt( this.blocks[ 0 ].length );
            byteBuf.putInt( this.blocks[ 0 ][ 0 ].length );

            ShortBuffer sBuf = byteBuf.asShortBuffer();
            for ( int x = 0; x < this.blocks.length; x++ )
                for ( int y = 0; y < this.blocks[ x ].length; y++ )
                    sBuf.put( this.blocks[ x ][ y ] );

            byte[] before = byteBuf.array();
            byte[] result = new byte[ before.length ];

            Deflater compressor = new Deflater( Deflater.BEST_COMPRESSION );
            compressor.setInput( before );
            compressor.finish();
            int cSize = compressor.deflate( result );
            compressor.end();

            file = new RandomAccessFile( schemFile.getAbsolutePath(), "rw" );
            file.writeInt( before.length );
            file.writeInt( cSize );
            file.write( result, 0, cSize );

            return true;
        } catch ( IOException e ) {
        } finally {
            if ( file != null )
                try {
                    file.close();
                } catch ( IOException e ) {
                }
        }

        return false;
    }
}

package com.ikkerens.worldedit.model;

import java.util.LinkedList;

import com.ikkerens.worldedit.Config;

import com.mbserver.api.MBServerPlugin;
import com.mbserver.api.game.MBSchematic;
import com.mbserver.api.game.Player;
import com.mbserver.api.game.World;

public class Session {
    private final Player                 player;
    private Selection                    selection;
    private MBSchematic                  clipboard;
    private final LinkedList< WEAction > history;

    private final int                    undoTreshold;
    private final int                    undoLimit;
    private int                          limit;

    public Session( final MBServerPlugin plugin, final Player player ) {
        this.player = player;
        this.selection = new Selection( this );
        this.history = new LinkedList< WEAction >();

        final Config config = plugin.getConfig();
        this.undoLimit = config.getUndoHistoryCount();
        this.undoTreshold = config.getUndoTreshold();
        this.limit = config.getLimit();
    }

    public void setLimit( final int limit ) {
        this.limit = limit;
    }

    public Player getPlayer() {
        return this.player;
    }

    public Selection getSelection() {
        return this.selection;
    }

    public void clearSelection() {
        this.selection = new Selection( this );
    }

    public MBSchematic getClipboard() {
        return this.clipboard;
    }

    public void setClipboard( final MBSchematic clipboard ) {
        this.clipboard = clipboard;
    }

    public WEAction newAction( final World world, final int count ) {
        final boolean recordHistory = count <= this.undoTreshold;
        final WEAction act = new WEAction( world, recordHistory, this.limit );

        if ( recordHistory ) {
            this.history.push( act );
            if ( this.history.size() > this.undoLimit )
                this.history.removeLast();
        }

        return act;
    }

    public boolean undoLast() {
        final WEAction last = this.history.poll();

        if ( last != null ) {
            last.undo();
            return true;
        }

        return false;
    }
}

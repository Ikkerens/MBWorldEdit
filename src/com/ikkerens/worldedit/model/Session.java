package com.ikkerens.worldedit.model;

import java.util.LinkedList;

import com.ikkerens.worldedit.Config;
import com.ikkerens.worldedit.WorldEditPlugin;
import com.mbserver.api.dynamic.BlockManager;
import com.mbserver.api.game.Player;
import com.mbserver.api.game.World;

public class Session {
    private BlockManager           mgr;
    private Player                 player;
    private Selection              selection;
    private LinkedList< WEAction > history;

    private int                    undoTreshold;
    private int                    undoLimit;
    private int                    limit;

    public Session( WorldEditPlugin plugin, Player player ) {
        this.mgr = plugin.getBlockManager();
        this.player = player;
        this.selection = new Selection( this );
        this.history = new LinkedList< WEAction >();

        Config config = plugin.getConfig();
        this.undoLimit = config.getUndoHistoryCount();
        this.undoTreshold = config.getUndoTreshold();
        this.limit = config.getLimit();
    }

    public void setLimit( int limit ) {
        this.limit = limit;
    }

    public Player getPlayer() {
        return this.player;
    }

    public Selection getSelection() {
        return this.selection;
    }

    public WEAction newAction( World world, int count ) {
        boolean recordHistory = count > this.undoTreshold;
        WEAction act = new WEAction( this.mgr, world, recordHistory, this.limit );

        if ( recordHistory ) {
            this.history.push( act );
            if ( this.history.size() > undoLimit )
                this.history.removeLast();
        }

        return act;
    }

    public boolean undoLast() {
        WEAction last = this.history.poll();

        if ( last != null ) {
            last.undo();
            return true;
        }

        return false;
    }
}

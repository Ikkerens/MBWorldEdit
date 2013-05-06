package com.ikkerens.worldedit.handlers;

import com.ikkerens.worldedit.WorldEditPlugin;
import com.ikkerens.worldedit.model.Selection;
import com.mbserver.api.game.Player;

public class AbstractHandler {
    private WorldEditPlugin plugin;

    public AbstractHandler( WorldEditPlugin plugin ) {
        this.plugin = plugin;
    }

    protected final WorldEditPlugin getPlugin() {
        return this.plugin;
    }

    protected final Selection getSelection( Player player ) {
        Selection sel = player.getMetaData( "worldedit.selection", null );
        if ( sel == null ) {
            sel = new Selection( player );
            player.setMetaData( "worldedit.selection", sel );
        }

        return sel;
    }
}

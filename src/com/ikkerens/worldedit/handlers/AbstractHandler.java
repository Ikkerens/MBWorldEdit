package com.ikkerens.worldedit.handlers;

import com.ikkerens.worldedit.WorldEditPlugin;
import com.ikkerens.worldedit.model.Session;
import com.mbserver.api.game.Player;

public class AbstractHandler {
    private WorldEditPlugin plugin;

    public AbstractHandler( WorldEditPlugin plugin ) {
        this.plugin = plugin;
    }

    protected final WorldEditPlugin getPlugin() {
        return this.plugin;
    }

    protected final Session getSession( Player player ) {
        Session session = player.getMetaData( "worldedit.session", null );
        if ( session == null ) {
            session = new Session( this.plugin, player );
            player.setMetaData( "worldedit.session", session );
        }

        return session;
    }
}

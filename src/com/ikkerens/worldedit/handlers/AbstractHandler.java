package com.ikkerens.worldedit.handlers;

import com.ikkerens.worldedit.model.Session;
import com.mbserver.api.MBServerPlugin;
import com.mbserver.api.game.Player;

public class AbstractHandler<P extends MBServerPlugin> {
    private P plugin;

    public AbstractHandler( P plugin ) {
        this.plugin = plugin;
    }

    protected final P getPlugin() {
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

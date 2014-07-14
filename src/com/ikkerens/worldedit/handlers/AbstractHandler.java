package com.ikkerens.worldedit.handlers;

import com.mbserver.api.MBServerPlugin;

public class AbstractHandler< P extends MBServerPlugin > {
    private final P plugin;

    public AbstractHandler( final P plugin ) {
        this.plugin = plugin;
    }

    protected final P getPlugin() {
        return this.plugin;
    }
}

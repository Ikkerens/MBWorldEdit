package com.ikkerens.worldedit.handlers;

import com.mbserver.api.MBServerPlugin;
import com.mbserver.api.events.Listener;

public abstract class AbstractListener< P extends MBServerPlugin > extends AbstractHandler< P > implements Listener {

    public AbstractListener( final P plugin ) {
        super( plugin );
    }

}

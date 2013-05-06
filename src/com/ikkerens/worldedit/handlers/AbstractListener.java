package com.ikkerens.worldedit.handlers;

import com.ikkerens.worldedit.WorldEditPlugin;
import com.mbserver.api.events.Listener;

public abstract class AbstractListener extends AbstractHandler implements Listener {

    public AbstractListener( WorldEditPlugin plugin ) {
        super( plugin );
    }

}

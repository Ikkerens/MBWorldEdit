package com.ikkerens.worldedit.wand;

import com.ikkerens.worldedit.WorldEditPlugin;
import com.ikkerens.worldedit.handlers.AbstractListener;
import com.mbserver.api.events.BlockPlaceEvent;
import com.mbserver.api.events.EventHandler;

public class WandListener extends AbstractListener<WorldEditPlugin> {

    public WandListener( WorldEditPlugin plugin ) {
        super( plugin );
    }

    @EventHandler
    public void wandPlace( BlockPlaceEvent e ) {
        if ( e.getBlock().getBlockID() == Wand.LEFT.getId() ) {
            this.getSession( e.getPlayer() ).getSelection().setPosition1( e.getLocation() );
            e.setCancelled( true );
        } else if ( e.getBlock().getBlockID() == Wand.RIGHT.getId() ) {
            this.getSession( e.getPlayer() ).getSelection().setPosition2( e.getLocation() );
            e.setCancelled( true );
        }
    }
}

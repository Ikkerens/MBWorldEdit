package com.ikkerens.worldedit.commands;

import com.ikkerens.worldedit.WorldEditPlugin;
import com.ikkerens.worldedit.handlers.AbstractCommand;
import com.ikkerens.worldedit.wand.Wand;

import com.mbserver.api.game.Player;

public class WandCommand extends AbstractCommand< WorldEditPlugin > {

    public WandCommand( final WorldEditPlugin plugin ) {
        super( plugin );
    }

    @Override
    protected void execute( final String label, final Player player, final String[] args ) {
        player.giveItem( Wand.LEFT.getId(), 1, false );
        player.giveItem( Wand.RIGHT.getId(), 1, true );
        player.sendMessage( "You have been given the 2 wand blocks." );
    }

}

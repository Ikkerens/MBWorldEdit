package com.ikkerens.worldedit.commands;

import com.ikkerens.worldedit.WorldEditPlugin;
import com.ikkerens.worldedit.handlers.AbstractCommand;
import com.ikkerens.worldedit.wand.Wand;

import com.mbserver.api.Constructors;
import com.mbserver.api.game.GameMode;
import com.mbserver.api.game.Player;

public class WandCommand extends AbstractCommand< WorldEditPlugin > {

    public WandCommand( final WorldEditPlugin plugin ) {
        super( plugin );
    }

    @Override
    protected void execute( final String label, final Player player, final String[] args ) {
        if ( player.getGameMode() == GameMode.CREATIVE ) {
            player.setItemSlot( 0, Constructors.newItemStack( Wand.LEFT.getId(), 1 ), false );
            player.setItemSlot( 1, Constructors.newItemStack( Wand.RIGHT.getId(), 2 ), true );
        } else {
            player.giveItem( Wand.LEFT.getId(), 1, false );
            player.giveItem( Wand.RIGHT.getId(), 1, true );
        }
        player.sendMessage( "You have been given the 2 wand blocks." );
    }

}

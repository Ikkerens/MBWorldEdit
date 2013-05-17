package com.ikkerens.worldedit.commands;

import com.ikkerens.worldedit.WorldEditPlugin;
import com.ikkerens.worldedit.handlers.AbstractCommand;
import com.ikkerens.worldedit.wand.Wand;
import com.mbserver.api.game.Player;

public class WandCommand extends AbstractCommand<WorldEditPlugin> {

    public WandCommand( WorldEditPlugin plugin ) {
        super( plugin );
    }

    @Override
    protected void execute( String label, Player player, String[] args ) {
        player.giveItem( Wand.LEFT.getId(), 1, false );
        player.giveItem( Wand.RIGHT.getId(), 1, true );
    }

}

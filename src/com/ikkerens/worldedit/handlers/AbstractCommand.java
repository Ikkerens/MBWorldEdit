package com.ikkerens.worldedit.handlers;

import com.ikkerens.worldedit.WorldEditPlugin;
import com.mbserver.api.CommandExecutor;
import com.mbserver.api.CommandSender;
import com.mbserver.api.game.Player;

public abstract class AbstractCommand extends AbstractHandler implements CommandExecutor {
    public AbstractCommand( WorldEditPlugin plugin ) {
        super( plugin );
    }

    public void execute( String command, final CommandSender sender, final String[] args, final String label ) {
        if ( !( sender instanceof Player ) ) {
            sender.sendMessage( "WorldEdit can only be used by players." );
            return;
        }

        if ( !sender.hasPermission( String.format( "ikkerens.worldedit.%s", command.replaceFirst( "/", "" ) ) ) ) {
            // sender.sendMessage( "You do not have permission to use /" + label );
            // return;
        }

        Thread cmdThread = new Thread( new Runnable() {
            public void run() {
                AbstractCommand.this.execute( label, (Player) sender, args );
            }
        } );
        cmdThread.start();
    }

    protected abstract void execute( String label, Player player, String[] args );
}

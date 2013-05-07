package com.ikkerens.worldedit.handlers;

import com.ikkerens.worldedit.WorldEditPlugin;
import com.mbserver.api.CommandSender;

public abstract class ActionCommand extends AbstractCommand {

    public ActionCommand( WorldEditPlugin plugin ) {
        super( plugin );
    }

    @Override
    public void execute( final String command, final CommandSender sender, final String[] args, final String label ) {
        new Thread() {
            @Override
            public void run() {
                ActionCommand.super.execute( command, sender, args, label );
            }
        }.start();
    }

}

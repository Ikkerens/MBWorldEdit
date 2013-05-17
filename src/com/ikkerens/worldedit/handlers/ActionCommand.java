package com.ikkerens.worldedit.handlers;

import com.mbserver.api.CommandSender;
import com.mbserver.api.MBServerPlugin;

public abstract class ActionCommand< P extends MBServerPlugin > extends AbstractCommand< P > {

    public ActionCommand( P plugin ) {
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

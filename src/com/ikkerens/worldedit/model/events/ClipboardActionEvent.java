package com.ikkerens.worldedit.model.events;

import com.mbserver.api.game.Player;

public class ClipboardActionEvent extends WorldEditEvent {
    private final String filename;

    public ClipboardActionEvent( final Player player, final String filename ) {
        super( player );
        this.filename = filename;
    }
    
    public String getFilename() {
        return this.filename;
    }

}

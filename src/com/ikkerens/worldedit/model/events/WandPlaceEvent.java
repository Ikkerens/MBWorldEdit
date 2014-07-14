package com.ikkerens.worldedit.model.events;

import com.mbserver.api.game.Location;
import com.mbserver.api.game.Player;

public class WandPlaceEvent extends WorldEditEvent {
    private final Location location;
    private final boolean  pos1;

    private boolean        revertBlock;

    public WandPlaceEvent( final Player player, final Location location, final boolean pos1 ) {
        super( player );
        this.location = location;
        this.pos1 = pos1;
        this.revertBlock = true;
    }

    public Location getLocation() {
        return this.location;
    }

    public boolean isPos1() {
        return this.pos1;
    }

    public boolean isPos2() {
        return !this.pos1;
    }

    public void setRevertBlock( final boolean revertBlock ) {
        this.revertBlock = revertBlock;
    }

    public boolean shouldRevertBlock() {
        return this.revertBlock;
    }
}

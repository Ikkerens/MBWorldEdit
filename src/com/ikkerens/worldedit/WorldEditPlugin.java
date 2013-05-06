package com.ikkerens.worldedit;

import com.ikkerens.worldedit.commands.ExpandCommand;
import com.ikkerens.worldedit.commands.PositionCommand;
import com.ikkerens.worldedit.commands.ReplaceCommand;
import com.ikkerens.worldedit.commands.SetCommand;
import com.ikkerens.worldedit.commands.ShiftCommand;
import com.ikkerens.worldedit.commands.WallsCommand;
import com.ikkerens.worldedit.commands.WandCommand;
import com.ikkerens.worldedit.wand.Wand;
import com.ikkerens.worldedit.wand.WandListener;
import com.mbserver.api.MBServerPlugin;
import com.mbserver.api.Manifest;
import com.mbserver.api.PluginManager;

@Manifest( name = "MBWorldEdit", authors = "Ikkerens", config = Config.class )
public class WorldEditPlugin extends MBServerPlugin {
    @Override
    public void onEnable() {
        Wand.LEFT.register( this.getBlockManager() );
        Wand.RIGHT.register( this.getBlockManager() );

        PluginManager pm = this.getPluginManager();
        // Plugin commands
        pm.registerCommand( "/wand", new WandCommand( this ) );

        // Actions
        pm.registerCommand( "/set", new SetCommand( this ) );
        pm.registerCommand( "/replace", new String[] { "/repl" }, new ReplaceCommand( this ) );
        pm.registerCommand( "/walls", new WallsCommand( this ) );

        // Selection
        pm.registerCommand( "/pos1", new String[] { "/pos2" }, new PositionCommand( this ) );
        pm.registerCommand( "/shift", new ShiftCommand( this ) );
        pm.registerCommand( "/expand", new ExpandCommand( this ) );

        pm.registerEventHandler( new WandListener( this ) );
    }

    @Override
    public void onDisable() {
        this.saveConfig();
    }
}

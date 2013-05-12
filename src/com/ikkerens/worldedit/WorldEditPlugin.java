package com.ikkerens.worldedit;

import com.ikkerens.worldedit.commands.CopyCommand;
import com.ikkerens.worldedit.commands.CountCommand;
import com.ikkerens.worldedit.commands.ExpandCommand;
import com.ikkerens.worldedit.commands.InOutSetCommand;
import com.ikkerens.worldedit.commands.LimitCommand;
import com.ikkerens.worldedit.commands.LoadCommand;
import com.ikkerens.worldedit.commands.OutlineCommand;
import com.ikkerens.worldedit.commands.PasteCommand;
import com.ikkerens.worldedit.commands.PositionCommand;
import com.ikkerens.worldedit.commands.PyramidCommand;
import com.ikkerens.worldedit.commands.ReplaceCommand;
import com.ikkerens.worldedit.commands.SaveCommand;
import com.ikkerens.worldedit.commands.SetCommand;
import com.ikkerens.worldedit.commands.ShiftCommand;
import com.ikkerens.worldedit.commands.SphereCommand;
import com.ikkerens.worldedit.commands.UndoCommand;
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
        // Save config, for editing
        this.getConfig();
        this.saveConfig();

        PluginManager pm = this.getPluginManager();

        // Set up wand
        Wand.LEFT.register( this.getBlockManager() );
        Wand.RIGHT.register( this.getBlockManager() );
        pm.registerEventHandler( new WandListener( this ) );

        // Plugin commands
        pm.registerCommand( "/wand", new WandCommand( this ) );
        pm.registerCommand( "/limit", new LimitCommand( this ) );
        pm.registerCommand( "/count", new CountCommand( this ) );

        // Actions
        pm.registerCommand( "/set", new SetCommand( this ) );
        pm.registerCommand( "/replace", new String[] { "/repl" }, new ReplaceCommand( this ) );
        pm.registerCommand( "/outline", new String[] { "/walls" }, new OutlineCommand( this ) );
        pm.registerCommand( "/sphere", new String[] { "/hsphere" }, new SphereCommand( this ) );
        pm.registerCommand( "/pyramid", new String[] { "/hpyramid" }, new PyramidCommand( this ) );
        pm.registerCommand( "/undo", new UndoCommand( this ) );

        // Selection
        pm.registerCommand( "/pos1", new String[] { "/pos2" }, new PositionCommand( this ) );
        pm.registerCommand( "/shift", new ShiftCommand( this ) );
        pm.registerCommand( "/expand", new ExpandCommand( this ) );
        pm.registerCommand( "/inset", new String[] { "/outset" }, new InOutSetCommand( this ) );

        // Clipboard
        pm.registerCommand( "/copy", new String[] { "/cp" }, new CopyCommand( this ) );
        pm.registerCommand( "/paste", new PasteCommand( this ) );
        pm.registerCommand( "/load", new LoadCommand( this ) );
        pm.registerCommand( "/save", new SaveCommand( this ) );
    }
}

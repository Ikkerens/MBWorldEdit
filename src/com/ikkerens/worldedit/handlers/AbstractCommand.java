package com.ikkerens.worldedit.handlers;

import com.ikkerens.worldedit.Config;
import com.ikkerens.worldedit.WorldEditPlugin;
import com.ikkerens.worldedit.exceptions.BlockLimitException;
import com.ikkerens.worldedit.model.SetBlockType;
import com.mbserver.api.CommandExecutor;
import com.mbserver.api.CommandSender;
import com.mbserver.api.dynamic.BlockManager;
import com.mbserver.api.game.Player;
import com.mbserver.api.game.World;

public abstract class AbstractCommand extends AbstractHandler implements CommandExecutor {
    private final BlockManager mgr;
    private final Config       config;

    public AbstractCommand( WorldEditPlugin plugin ) {
        super( plugin );
        this.mgr = plugin.getBlockManager();
        this.config = plugin.getConfig();
    }

    public void execute( String command, CommandSender sender, String[] args, String label ) {
        if ( !( sender instanceof Player ) ) {
            sender.sendMessage( "WorldEdit can only be used by players." );
            return;
        }

        if ( !sender.hasPermission( String.format( "ikkerens.worldedit.%s", command.replaceFirst( "/", "" ) ) ) ) {
            // sender.sendMessage( "You do not have permission to use /" + label );
            // return;
        }

        this.execute( label, (Player) sender, args );
    }

    protected final int setBlock( int affected, World world, int x, int y, int z, SetBlockType type ) throws BlockLimitException {
        short tS = type.getNextBlock();
        if ( world.getBlockID( x, y, z ) != tS ) {
            if ( this.mgr.getBlockType( tS ).isTransparent() )
                world.setBlock( x, y, z, tS );
            else
                world.setBlockWithoutUpdate( x, y, z, tS );

            if ( affected >= config.getLimit() )
                throw new BlockLimitException();

            return 1;
        } else
            return 0;
    }

    protected abstract void execute( String label, Player player, String[] args );
}

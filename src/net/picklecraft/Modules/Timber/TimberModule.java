package net.picklecraft.Modules.Timber;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginManager;

import de.diddiz.LogBlock.Consumer;
import de.diddiz.LogBlock.LogBlock;

import net.picklecraft.PickleCraftPlugin;
import net.picklecraft.Modules.IModule;
import org.bukkit.World;

/**
 * Copyright (c) 2011-2014
 *
 * This program is free software: you can redistribute it and/or modify it under
 * the terms of the GNU General Public License as published by the Free Software
 * Foundation, either version 2 of the License, or (at your option) any later
 * version.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU General Public License for more
 * details.
 *
 * You should have received a copy of the GNU General Public License along with
 * this program. If not, see <http://www.gnu.org/licenses/>.
 *
 * @author Pickle
 *
 */

public class TimberModule implements IModule {

    private final PickleCraftPlugin plugin;
    private final TimberBlockListener blockListener;

    private Consumer lbconsumer = null;

    protected static Material treeMaterials[] = {Material.LOG, Material.LOG_2, Material.LEAVES, Material.LEAVES_2};
    
    protected enum CutType {
        SUPER,
        STANDARD
    }
    
    private final int STANDARD_MAX_RANGE;
    private final int STANDARD_MAX_HEIGHT;
    private final int SUPER_MAX_RANGE;
    private final int SUPER_MAX_HEIGHT;
    
    public TimberModule(PickleCraftPlugin plugin) {
        this.plugin = plugin;
        this.blockListener = new TimberBlockListener(this);
        final PluginManager pm = Bukkit.getServer().getPluginManager();
        final Plugin p = pm.getPlugin("LogBlock");
        if (p != null) {
            lbconsumer = ((LogBlock) p).getConsumer();
        }
        pm.registerEvents(blockListener, plugin);

        STANDARD_MAX_RANGE = plugin.getConfig().getInt("timber.standardMaxRange");
        STANDARD_MAX_HEIGHT = plugin.getConfig().getInt("timber.standardMaxHeight");
        SUPER_MAX_RANGE = plugin.getConfig().getInt("timber.superMaxRange");
        SUPER_MAX_HEIGHT = plugin.getConfig().getInt("timber.superMaxHeight");
        
    }

    @Override
    public void onDisable() {

    }

    @Override
    public void onEnable() {
    }

    @Override
    public PickleCraftPlugin getPlugin() {
        return this.plugin;
    }

    @Override
    public String getName() {
        return "Timber";
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command,
            String label, String[] args) {
        return false;
    }

    @Override
    public void sendCommandList(CommandSender sender) {

    }

    private void breakBlock(Player player, int x, int y, int z) {
        Block b = player.getWorld().getBlockAt(x, y, z);
        if (lbconsumer != null) {
            lbconsumer.queueBlockBreak(player.getName(), b.getState());
        }
        
        b.breakNaturally(player.getItemInHand());
        plugin.Damage(player, 1);
    }

    public void CutTree(Player player, Block log) {
        if (player.getItemInHand().getType() == Material.GOLD_AXE) { //superaxe!
            breakChain(player, log, CutType.SUPER, log.getX(), log.getY(), log.getZ());
        }
        else {
            breakChain(player, log, CutType.STANDARD, log.getX(), log.getY(), log.getZ());
        }
 
    }
    
    public Material getMaterialFromBlock(World w, int x, int y, int z) {
        return w.getBlockAt(x,y,z).getType();
    }
    
    public boolean isBeyondRange(CutType cutType, Block referenceBlock, int x, int y, int z) {
        if (cutType == CutType.STANDARD) {
            if (Math.abs(referenceBlock.getX() - x) > STANDARD_MAX_RANGE ||
                    Math.abs(referenceBlock.getZ() - z) > STANDARD_MAX_RANGE ||
                    Math.abs(referenceBlock.getY() - y) > STANDARD_MAX_HEIGHT) {
                return true;
            }
        }
        else if (cutType == CutType.SUPER) {
            if (Math.abs(referenceBlock.getX() - x) > SUPER_MAX_RANGE ||
                    Math.abs(referenceBlock.getZ() - z) > SUPER_MAX_RANGE ||
                    Math.abs(referenceBlock.getY() - y) > SUPER_MAX_HEIGHT) {
                return true;
            }
        }
        return false;
    }
    /*
    * A recurisive method to chop a tree down,
    * Concept borrowed from http://pastebin.com/BCNURhFG
    *
    * Reference Block is the starting point used for as a reference point.
    *
    * @author Pickle, club559
    */
    
    public void breakChain(Player player, Block referenceBlock, CutType cutType, int x, int y, int z) {
        if (isBeyondRange(cutType, referenceBlock, x, y, z)) {
            return;
        }

        /*
        *Ignore this until I figure a workaround unbreaking enchantment not affecting damage rate.
        if (player.getItemInHand().getType() == Material.AIR) {
            return;
        }*/
        
        breakBlock(player, x, y, z);
        
        for (Material mat : TimberModule.treeMaterials) {
            if (getMaterialFromBlock(player.getWorld(), x+1, y, z) == mat) {
                breakChain(player, referenceBlock, cutType, x+1, y, z);
            }
            
            if (getMaterialFromBlock(player.getWorld(), x-1, y, z) == mat) {
                breakChain(player, referenceBlock, cutType, x-1, y, z);
            }
            
            if (getMaterialFromBlock(player.getWorld(), x, y+1, z) == mat) {
                breakChain(player, referenceBlock, cutType, x, y+1, z);
            }
            
            /*if (getMaterialFromBlock(player.getWorld(), x, y-1, z) == mat) {
                breakChain(player, referenceBlock, cutType, x, y-1, z);
            }*/

            if (getMaterialFromBlock(player.getWorld(), x, y, z+1) == mat) {
                breakChain(player, referenceBlock, cutType, x, y, z+1);
            }
            
            if (getMaterialFromBlock(player.getWorld(), x, y, z-1) == mat) {
                breakChain(player, referenceBlock, cutType, x, y, z-1);
            }
        }

    }

}

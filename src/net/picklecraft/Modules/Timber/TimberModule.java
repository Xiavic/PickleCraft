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
/**
 * Copyright (c) 2011-2012
 * 
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 2 of the License, or
 * (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 * 
 * @author Pickle
 * 
 */

public class TimberModule implements IModule {

	private PickleCraftPlugin plugin;
	
	private Consumer lbconsumer = null;
	
	private TimberBlockListener blockListener;
	
	protected static Material treeMaterials[] = { Material.LOG, Material.LEAVES };
	
	public TimberModule(PickleCraftPlugin plugin) {
		this.plugin = plugin;
		this.blockListener = new TimberBlockListener(this);		
		final PluginManager pm = Bukkit.getServer().getPluginManager();
		final Plugin p = pm.getPlugin("LogBlock");
		if (p != null)
			lbconsumer = ((LogBlock)p).getConsumer();
		
		pm.registerEvents(blockListener,plugin);

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
	
	private void breakBlock(Block b, Player player, boolean damage) {
		if (lbconsumer != null) {
			lbconsumer.queueBlockBreak(player.getName(),  b.getState());
		}
		b.breakNaturally();

		if (damage)
			plugin.Damage(player,1);
	}
	
	public void CutTree(Player player, Block log) {
		
		int m = 0;
		//Lets choose our max height for each tree.
		switch(log.getData()) {
		case 0:
			m = 10;
			break;
		case 1:
			m = 15;
			break;
		case 2:
			m = 10;
			break;
		case 3:
			m = 25;
			break;
		default: 
			m = 10;
		}
		int y = log.getY();
		int max = y + m;
		if (max > player.getWorld().getMaxHeight()) { 
			max = player.getWorld().getMaxHeight(); 
		}
		if (player.getItemInHand().getType() == Material.GOLD_AXE) { //superaxe!
			for (; y < max; y++) {
				for (int x = -1; x < 2; x++) {
					for (int z = -1; z < 2; z++) {
						Block b = player.getWorld().getBlockAt(log.getX() + x, y, log.getZ() + z);
						//Help save the rainforest!
						if (b.getType() == treeMaterials[0]) {
							if (b.getData() == 3 && !PickleCraftPlugin.hasPerm(player, "PickleCraft.timber.cut.jungle")) { return; }
							breakBlock(b, player,false);
						}
						else if (b.getType() == treeMaterials[1]) {
							if (b.getData() == 3 && !PickleCraftPlugin.hasPerm(player, "PickleCraft.timber.cut.jungle")) { return; }
							breakBlock(b, player,false);
						}
						else {
							if(b.getType() == Material.AIR) {
								//derp must ran out of tree?
								if (x == 0 && z == 0) { return; }
							}
						}
					}
				}
				plugin.Damage(player,1); // instead per height.
			}
		}
		else {
			for (; y < max; y++) {
				Block b = player.getWorld().getBlockAt(log.getX(), y, log.getZ());
				//Help save the rainforest!
				if (b.getType() == Material.LOG) {
					if (b.getData() == 3 && !PickleCraftPlugin.hasPerm(player, "PickleCraft.timber.cut.jungle")) { return; }
					breakBlock(b, player,true);
				}
				else if (b.getType() == Material.LEAVES) {
					if (b.getData() == 3 && !PickleCraftPlugin.hasPerm(player, "PickleCraft.timber.cut.jungle")) { return; }
					breakBlock(b, player,true);
				}
				else {
					if(b.getType() == Material.AIR) {
						return;
					}
				}
			}
		}
	}

}

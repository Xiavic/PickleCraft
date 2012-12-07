package net.picklecraft.Modules.InvisibilityNerf;

import net.picklecraft.PickleCraftPlugin;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.*;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.Potion;
import org.bukkit.potion.PotionType;

/**
 *
 * Copyright (c) 2012
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
 */
public class PlayerListener implements Listener {
	private InvisibilityNerfModule module;

	public PlayerListener(InvisibilityNerfModule module) {
		this.module = module;
	}
	@EventHandler(priority = EventPriority.NORMAL)
	public void onDropItem(PlayerDropItemEvent event) {
		//if player is holding a watch and drops it....turn invis off.
		if (event.getPlayer().getItemInHand().getType() == Material.WATCH) {
			ItemStack item = event.getItemDrop().getItemStack();
			if (item != null) {
				if (item.getType() == Material.WATCH) {
					SpyPlayer spy = module.getPlayer(event.getPlayer());
					if (spy != null) { //player isn't listed
						spy.setInvisible(false);
					}
				}
			}
		}
	}
	@EventHandler(priority = EventPriority.NORMAL)
	public void onItemHeld(PlayerItemHeldEvent event) {
		SpyPlayer spy = module.getPlayer(event.getPlayer());
		ItemStack item = event.getPlayer().getInventory().getItem(event.getNewSlot());
		if (item != null) {
			if (item.getType() == Material.WATCH) {
				if (spy != null) {
					spy.setInvisible(true);
				}
				//If player is admin, make him vanish
				//Player doesn't need a potion
				else if (PickleCraftPlugin.hasPerm(event.getPlayer(), "PickleCraft.invisiblity.admin")) {
					//Make potion last virtually forever
					spy = module.addPlayer(event.getPlayer(),Integer.MAX_VALUE,Integer.MAX_VALUE);
					spy.setInvisible(true);
				}
			}
			else {
				//if players switches item then turn invis off.
				if (spy != null) {
					spy.setInvisible(false);
				}
			}
		}
		else {
			//if players switches item then turn invis off.
			if (spy != null) {
				spy.setInvisible(false);
			}
		}
	}
	@EventHandler(priority = EventPriority.NORMAL)
	public void onPlayerClick(PlayerInteractEvent event) {
		if (event.getAction() == Action.RIGHT_CLICK_AIR || event.getAction() == Action.RIGHT_CLICK_BLOCK) {
			if (!event.isCancelled() && event.getMaterial() == Material.POTION) {
				Potion p = Potion.fromItemStack(event.getItem());
				if (p.getType() == PotionType.INVISIBILITY) {
					SpyPlayer spy = module.getPlayer(event.getPlayer());
					if (spy == null) { //player isn't listed
						//So doo all this stuff
						spy = module.addPlayer(event.getPlayer());
						spy.setInvisible(false);
						event.getItem().setAmount(event.getItem().getAmount()-1);
						spy.getPlayer().sendMessage(
							module.getPlugin().getStringFromConfig("invisiblenerf.messages.info")
							);
						//event.getPlayer().sendMessage("Heavy is Spy!");
					}
					//cancel the event so not to waste player's potion when under effect
					event.setCancelled(true);
				}
			}
		}
	}
	@EventHandler(priority = EventPriority.NORMAL)
	public void onPlayerJoin(PlayerJoinEvent event) {
		//if player joins with invis effect, prevent them from being invis.
		if (SpyPlayer.hasInvisEffect(event.getPlayer())) {
			SpyPlayer spy = module.getPlayer(event.getPlayer());
			if (spy == null) { //player isn't listed
				spy = module.addPlayer(event.getPlayer());
			}
			spy.setInvisible(false);
		}
	}
	@EventHandler(priority = EventPriority.NORMAL)
	public void onPlayerKick(PlayerKickEvent event) {
		module.removePlayer(event.getPlayer());
	}
	@EventHandler(priority = EventPriority.NORMAL)
	public void onPlayerQuit(PlayerQuitEvent event) {
		module.removePlayer(event.getPlayer());
	}


}

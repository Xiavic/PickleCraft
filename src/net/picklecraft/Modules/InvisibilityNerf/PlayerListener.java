package net.picklecraft.Modules.InvisibilityNerf;

import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.*;

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
	public void onItemHeld(PlayerItemHeldEvent event) {
		SpyPlayer spy = module.getPlayer(event.getPlayer());
		if (event.getPlayer().getInventory().getItem(event.getNewSlot()).getType() == Material.WATCH) {
			if (spy == null) { //player isn't listed
				spy = module.addPlayer(event.getPlayer());
			}
			spy.setInvisible(true);
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
		//if player joins with invis effect, prevent them from being invis.
		if (SpyPlayer.hasInvisEffect(event.getPlayer())) {
			SpyPlayer spy = module.getPlayer(event.getPlayer());
			if (spy == null) { //player isn't listed
				spy = module.addPlayer(event.getPlayer());
			}
			spy.setInvisible(true);
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
			spy.setInvisible(true);
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

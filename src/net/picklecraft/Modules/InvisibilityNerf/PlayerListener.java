package net.picklecraft.Modules.InvisibilityNerf;

import java.util.Collection;
import org.bukkit.Material;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PotionSplashEvent;
import org.bukkit.event.player.PlayerItemHeldEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerKickEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

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
		if (event.getPlayer().getItemInHand().getType() == Material.WATCH) {
			if (spy == null) { //player isn't listedSpyPlayer spy = module.getPlayer(event.getPlayer());
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
	public void onPlayerJoin(PlayerJoinEvent event) {
		//if player joins with invis effect, prevent them from being invis.
		if (SpyPlayer.hasInvisEffect(event.getPlayer())) {
			SpyPlayer spy = module.addPlayer(event.getPlayer());
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

package net.picklecodes.Modules.SignRank;

import net.picklecodes.PickleCraftPlugin;

import org.bukkit.block.BlockState;
import org.bukkit.block.Sign;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.SignChangeEvent;
import org.bukkit.event.player.PlayerInteractEvent;

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
public class SignRankPlayerListener implements Listener  {
	private SignRankModule module;
	public SignRankPlayerListener(SignRankModule module) {
		this.module = module;
	}
	@EventHandler(priority = EventPriority.LOW)
	public void onPlayerInteract(PlayerInteractEvent event) {
		if (event.getAction() == Action.RIGHT_CLICK_BLOCK) {
			BlockState blockstate = event.getClickedBlock().getState();
			if (blockstate instanceof Sign) {
				Sign sign = (Sign)blockstate;
				String[] lines = sign.getLines();
				if (lines[0].equalsIgnoreCase("SignRank")) {
					if (!lines[1].isEmpty()) { module.promote(event.getPlayer(),lines[1]); }
					else if (!lines[2].isEmpty()) { module.promote(event.getPlayer(),lines[2]); }
					else if (!lines[3].isEmpty()) { module.promote(event.getPlayer(),lines[3]); }
				}
			}
		}
	}
	@EventHandler(priority = EventPriority.HIGH)
	public void onSignChange(SignChangeEvent event) {
		String[] lines = event.getLines();
		if (lines[0].equalsIgnoreCase("SignRank")) {
			if (PickleCraftPlugin.hasPerm(event.getPlayer(), "PickleCraft.signrank.build")) {
				event.getPlayer().sendMessage(PickleCraftPlugin.Colorize("&2Created SignRank!"));
			}
			else {
				event.getPlayer().sendMessage(
						module.getPlugin().getStringFromConfig("common.messages.errors.donthaveperm")
						);
				event.getBlock().breakNaturally();
				event.setCancelled(true);
			}
		}
	}
	
}

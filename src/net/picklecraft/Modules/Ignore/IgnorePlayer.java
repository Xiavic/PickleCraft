package net.picklecraft.Modules.Ignore;

import java.util.ArrayList;
import java.util.List;

import net.picklecraft.PickleCraftPlugin;

import org.bukkit.entity.Player;
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
public class IgnorePlayer {
	private boolean allIgnore = false;
	private List<Player> ignoreList = new ArrayList<Player>();
	private Player player;
	private IgnoreModule module;

	public IgnorePlayer(IgnoreModule module, Player player) {
		this.module = module;
		this.player = player;
	}

	public void unignorePlayer(Player player) {
		if (player != null) {
			if (ignoreList.contains(player)) {
				ignoreList.remove(player);
				this.player.sendMessage(
						module.getPlugin().getStringFromConfig("ignorecraft.messages.info.removed"
						, player.getName())
						);
			}
			else {
				this.player.sendMessage(
						module.getPlugin().getStringFromConfig("ignorecraft.messages.errors.isnotignored"
						, player.getName())
						);
			}
		}
	}

	public void ignorePlayer(Player player) {
		if (player != null) {
			if (!ignoreList.contains(player)) {
				this.player.sendMessage(
						module.getPlugin().getStringFromConfig("ignorecraft.messages.info.added"
						, player.getName())
						);
				player.sendMessage(
						module.getPlugin().getStringFromConfig("ignorecraft.messages.info.ignored"
						, this.player.getName())
						);
				ignoreList.add(player);
			}
			else {
				this.player.sendMessage(
						module.getPlugin().getStringFromConfig("ignorecraft.messages.errors.alreadyignored"
						, player.getName())
						);
			}
		}
	}

	public boolean isAllIgnored() { return allIgnore; }
	public void toggleAllIgnore() {
		allIgnore = !allIgnore;
		if (allIgnore) {
			player.sendMessage(
					module.getPlugin().getStringFromConfig("ignorecraft.messages.info.allignore_on")
					);
		}
		else {
			player.sendMessage(
					module.getPlugin().getStringFromConfig("ignorecraft.messages.info.allignore_off")
					);
		}
	}

	public Player getPlayer() {
	    return player;
	}
	public boolean isIgnored(Player player) {
		return ignoreList.contains(player);
	}
	public List<Player> getIgnoreList() {
	    return ignoreList;
	}

}

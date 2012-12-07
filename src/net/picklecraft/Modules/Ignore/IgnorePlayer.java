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
	/*
	 * Converted this from "raw" player objects to String of the player's names.
	 * Due to the fact player objects are null when json is parsed.
	 */
	private List<String> ignoreList = new ArrayList<String>();
	private String player;
	private IgnoreModule module;

	public IgnorePlayer(IgnoreModule module, String player) {
		this.module = module;
		this.player = player;
	}

	public void unignorePlayer(String player, boolean inform) {
		Player currentPlayer = module.getPlugin().getServer().getPlayerExact(this.player);
		if (player != null) {
			if (ignoreList.contains(player)) {
				ignoreList.remove(player);
				if (inform) {
					currentPlayer.sendMessage(
							module.getPlugin().getStringFromConfig("ignorecraft.messages.info.removed"
							, player)
							);
				}
			}
			else if (inform) {
				currentPlayer.sendMessage(
						module.getPlugin().getStringFromConfig("ignorecraft.messages.errors.isnotignored"
						, player)
						);
			}
		}
	}

	public void ignorePlayer(String player, boolean inform) {
		Player currentPlayer = module.getPlugin().getServer().getPlayerExact(this.player);
		Player ignorePlayer = module.getPlugin().getServer().getPlayerExact(player);
		if (player != null) {
			if (!ignoreList.contains(player)) {
				if (inform) {
					currentPlayer.sendMessage(
							module.getPlugin().getStringFromConfig("ignorecraft.messages.info.added"
							, player)
							);
					ignorePlayer.sendMessage(
							module.getPlugin().getStringFromConfig("ignorecraft.messages.info.ignored"
							, this.player)
							);
				}
				ignoreList.add(player);
			}
			else if (inform) {
				currentPlayer.sendMessage(
						module.getPlugin().getStringFromConfig("ignorecraft.messages.errors.alreadyignored"
						, player)
						);
			}
		}
	}

	public boolean isAllIgnored() { return allIgnore; }
	public void toggleAllIgnore(boolean inform) {
		allIgnore = !allIgnore;
		if (inform) {
			Player currentPlayer = module.getPlugin().getServer().getPlayerExact(this.player);
			if (allIgnore) {
				currentPlayer.sendMessage(
						module.getPlugin().getStringFromConfig("ignorecraft.messages.info.allignore_on")
						);
			}
			else {
				currentPlayer.sendMessage(
						module.getPlugin().getStringFromConfig("ignorecraft.messages.info.allignore_off")
						);
			}
		}
	}

	public Player getPlayer() {
	    return module.getPlugin().getServer().getPlayerExact(this.player);
	}
	public String getPlayerName() {
	    return this.player;
	}
	public boolean isIgnored(String player) {
		return ignoreList.contains(player);
	}
	public List<String> getIgnoreList() {
	    return ignoreList;
	}

}

package net.picklecodes.Modules.SignRank;

import net.picklecodes.PickleCraftPlugin;
import net.picklecodes.Modules.IModule;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import ru.tehkode.permissions.PermissionManager;
import ru.tehkode.permissions.PermissionUser;
import ru.tehkode.permissions.bukkit.PermissionsEx;
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
public class SignRankModule implements IModule {
	private PickleCraftPlugin plugin;
	private SignRankPlayerListener srPlayerListener;
	private PermissionManager permEx;
	
	public SignRankModule(PickleCraftPlugin plugin) {
		this.plugin = plugin;
	}
	@Override
	public void onDisable() {

	}

	@Override
	public void onEnable() {
		if(Bukkit.getServer().getPluginManager().isPluginEnabled("PermissionsEx")){
			permEx = PermissionsEx.getPermissionManager();
			srPlayerListener = new SignRankPlayerListener(this);
			plugin.getServer().getPluginManager().registerEvents(srPlayerListener, plugin);
		}
		else {
			plugin.unloadModule(this);
			PickleCraftPlugin.log.info("Could not find pex!");
		}
	}

	@Override
	public PickleCraftPlugin getPlugin() {
		return plugin;
	}

	@Override
	public boolean onCommand(CommandSender sender, Command command,
			String label, String[] args) {
		return false;
	}

	@Override
	public void sendCommandList(CommandSender sender) {

	}
	
	public void promote(Player player, String group) {
		if (permEx.getGroup(group) != null) {
			PermissionUser user = permEx.getUser(player);
			user.setGroups(new String[]{group});
			player.sendMessage(PickleCraftPlugin.Colorize("&2Promoted to &e"+group));
		}
	}

}

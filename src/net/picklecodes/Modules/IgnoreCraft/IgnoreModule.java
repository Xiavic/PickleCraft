package net.picklecodes.Modules.IgnoreCraft;

import java.util.HashMap;
import java.util.Map;

import net.picklecodes.PickleCraftPlugin;
import net.picklecodes.Modules.IModule;
import net.picklecodes.util.Properties;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
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
public class IgnoreModule implements IModule {
	public Map<Player,IgnorePlayer> playerIgnoreList = new HashMap<Player,IgnorePlayer>();
	public Properties ignoreListProp;
	
	private PickleCraftPlugin plugin;
	
	private IgnorePlayerListener igPlayerListener;
	private IgnoreWorldListener igWorldListener;
	
	public IgnoreModule(PickleCraftPlugin plugin) {
		this.plugin = plugin;
	}
	
	@Override
	public PickleCraftPlugin getPlugin() {
		return plugin;
	}
	@Override
	public String getName() {
		return "Ignore";
	}
	@Override
	public void onDisable() {
		Save();
	}

	@Override
	public void onEnable() {
		igPlayerListener = new IgnorePlayerListener(this);
		igWorldListener = new IgnoreWorldListener(this);
		plugin.getServer().getPluginManager().registerEvents(igPlayerListener,plugin);
		plugin.getServer().getPluginManager().registerEvents(igWorldListener,plugin);
		ignoreListProp = new Properties(plugin, "ignoreList.prop");
		ignoreListProp.Parse();
	}
	
	@Override
	public void sendCommandList(CommandSender sender) {
		Player player = null;
		if (sender instanceof Player) {
			player = (Player) sender;
		}
		if (player != null) {
			Command c = plugin.getCommand("ignore");
		     if (PickleCraftPlugin.hasPerm(player, "IgnoreCraft.ignore")) {
				player.sendMessage(
						plugin.getStringFromConfig("ignorecraft.messages.commandhelplist.header")
						);
				player.sendMessage(c.getUsage() +" "+ c.getDescription());
				c = plugin.getCommand("ignoreall");
				player.sendMessage(c.getUsage() +" "+ c.getDescription());
				c = plugin.getCommand("ignorelist");
				player.sendMessage(c.getUsage() +" "+ c.getDescription());			
			 }	
		}
	}

	@Override
	public boolean onCommand(CommandSender sender, Command command,
			String label, String[] args) {
		Player player = null;
		if (sender instanceof Player) {
			player = (Player) sender;
		}
		/*
		 * ignore command.
		 */
		if (command.getName().equalsIgnoreCase("ignore")){
			if (player != null) {
				if (PickleCraftPlugin.hasPerm(player, command.getPermission())) {
					if (args.length >= 1) {
						if (args.length >= 2) {
						/* 
						 * flags 
						 */
							if (args[0].equalsIgnoreCase("-r")) { // remove ignore flag
								Object[] playerAndbool = PickleCraftPlugin.getPlayer(args[1]);
								Player p = (Player) playerAndbool[0]; 
								if (p != null) {
									if ((Boolean)playerAndbool[1] == false) {
										IgnorePlayer igP = playerIgnoreList.get(player);
										if (igP != null) {
											igP.unignorePlayer(p);		
										}
										else {
											player.sendMessage(
													plugin.getStringFromConfig("ignorecraft.messages.errors.isnotignored"
													, p.getName())
													);
										}
									}
									else {
										player.sendMessage(
												plugin.getStringFromConfig("common.messages.errors.tomanyplayers"
												, args[1])
												);
									}
									return true;
								}
								else {
									player.sendMessage(
											plugin.getStringFromConfig("common.messages.errors.playerdontexist"
											, args[1])
											);
								}			
							}
						}
						else {
							Object[] playerAndbool = PickleCraftPlugin.getPlayer(args[0]);
							Player p = (Player) playerAndbool[0]; 
							if (p != null) {
								if ((Boolean)playerAndbool[1] == false) {
									IgnorePlayer igP = playerIgnoreList.get(player);
									if (igP != null) {
										igP.ignorePlayer(p);
									}
									else {
										igP = new IgnorePlayer(this,player);
										igP.ignorePlayer(p);
										playerIgnoreList.put(player, igP);
									}
								}
								else {
									player.sendMessage(
											plugin.getStringFromConfig("common.messages.errors.tomanyplayers"
											, args[0])
											);
								}
							}
							else {
								player.sendMessage(
										plugin.getStringFromConfig("common.messages.errors.playerdontexist"
										, args[0])
										);
							}
							return true;
						}
					}
				}
				else {
					player.sendMessage(
							plugin.getStringFromConfig("common.messages.errors.donthaveperm")
							);
					return true;
				}	
			}
			else {
				sender.sendMessage("This is a player only command.");
				return true;
			}
		}
		/*
		 * ignoreall command
		 */
		else if (command.getName().equalsIgnoreCase("ignoreall")) { //all ignore 
			if (player != null) {
				if (PickleCraftPlugin.hasPerm(player, command.getPermission())) {
					IgnorePlayer igP = playerIgnoreList.get(player);
					if (igP != null) {
						igP.toggleAllIgnore();
					}
					else {
						igP = new IgnorePlayer(this,player);
						igP.toggleAllIgnore();
						playerIgnoreList.put(player, igP);
					}
				}	
				else {
					player.sendMessage(
							plugin.getStringFromConfig("common.messages.errors.donthaveperm")
							);
				}
			}
			else {
				sender.sendMessage("This is a player only command.");
			}
			return true;
		}
		/*
		 * display ignore list
		 */
		else if (command.getName().equalsIgnoreCase("ignorelist")) { //displays ignore list
			if (player != null) {
				IgnorePlayer igP = playerIgnoreList.get(player);
				if (igP != null) {
					String ignoreList = igP.getStringList();
					if (ignoreList == null) {
						player.sendMessage(
								plugin.getStringFromConfig("ignorecraft.messages.errors.noignores")
								);
					}
					else if (ignoreList.isEmpty()) { 
						player.sendMessage(
								plugin.getStringFromConfig("ignorecraft.messages.errors.noignores")
								);
					}
					else {
						player.sendMessage(
								plugin.getStringFromConfig("ignorecraft.messages.info.ignorelist"
								, ignoreList)
								);
					}
				}
				else {
					player.sendMessage(
							plugin.getStringFromConfig("ignorecraft.messages.errors.noignores")
							);
				}
			}	
			else {
				sender.sendMessage("This is a player only command.");
			}
			return true;
		}
		return false;
	}

	public void setPlayerIgnoreList(Player player, String igList) {
		if (player != null) {
			if (igList != null) {
				if (!igList.isEmpty()) {
					IgnorePlayer igPlayer = playerIgnoreList.get(player);
					if (igPlayer != null) {
						igPlayer.setStringList(igList);
					}
					else {
						igPlayer = new IgnorePlayer(this, player);
						if (igPlayer.setStringList(igList)) {
							playerIgnoreList.put(player, igPlayer);
						}
					}
				}
			}
		}
	}
	
	public void Save() {
		for (Player player : playerIgnoreList.keySet()) {
			Save(player);
		}
		ignoreListProp.Save();
	}
	public void Save(Player player) {
		if (player != null) {
			IgnorePlayer ig = playerIgnoreList.get(player);
			if (ig != null) {
				ignoreListProp.addValue(player.getName() +".ignore", ig.getStringList().toLowerCase());
			}
		}
	}
	
	public void Load(Player player) {
		if (player != null) {
			String igList = null;
			 igList = ignoreListProp.getValue(player.getName() +".ignore");	
			 if (igList != null) {
				 setPlayerIgnoreList(player,igList);
			 }
		}
	}

}

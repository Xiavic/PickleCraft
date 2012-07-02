package net.picklecraft;

import com.google.gson.Gson;
import com.sk89q.wepif.PermissionsResolverManager;
import java.util.IllegalFormatException;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import net.picklecraft.Modules.IModule;
import net.picklecraft.Modules.ModuleManager;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.server.PluginDisableEvent;
import org.bukkit.event.server.PluginEnableEvent;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.java.JavaPlugin;

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
public class PickleCraftPlugin extends JavaPlugin implements Listener {
	public static final Logger log = Bukkit.getLogger();
	public static ModuleManager moduleManager = null; 
        public static final Pattern colorPattern = Pattern.compile("&");
	public static final Gson gson = new Gson();
        
	private static boolean worldedit;
	
	/*
	 * returns Player and a Boolean (that defines if multiple players was found).
	 * first value will be Player
	 * second will be Boolean
	 * 
	 * Will return if An exact match was found,
	 *  and the second value shall be false.
	 */
	public static Object[] getPlayer(String name) {
            name = name.toLowerCase();
            Object[] playerAndbool = new Object[2];
            playerAndbool[0] = null;
            playerAndbool[1] = false;
            int playerMatch = 0;
            for (Player p : Bukkit.getServer().getOnlinePlayers()) {
                String name2 = p.getName().toLowerCase();
                if (name2.contains(name)) { 
                    playerAndbool[0] = p;
                    if (name2.equals(name)) {
                        playerAndbool[1] = false;
                        break;
                    }
                    else if (++playerMatch > 1) {
                        playerAndbool[1] = true;
                        break;
                    }
                }
            }
            return playerAndbool;
	}

    public static String Colorize(String string) {
        Matcher m = colorPattern.matcher(string);
        StringBuffer sb = new StringBuffer();
        boolean result = m.find();
        while(result) {
            m.appendReplacement(sb, "\u00A7");
            result = m.find();
        }
        m.appendTail(sb);
        return sb.toString();
    }
    public String getStringFromConfig(String path) {
        return Colorize(getConfig().getString(path));
    }
    public String getStringFromConfig(String path, Object... args) {
    	try {
            return Colorize(String.format(getConfig().getString(path), args));
        }
        catch(IllegalFormatException e) {
            e.printStackTrace();
            return ChatColor.RED +"Check Config: "+ path;
        }
    }
    
	public static boolean hasPerm(Player player, String perm) {
            if (worldedit) {
                return PermissionsResolverManager.getInstance().hasPermission(player, perm);
            }
            return player.hasPermission(perm);
	}
	
	public static boolean hasWorldEdit() { return worldedit; }
	
	public void reload() {
            Bukkit.broadcastMessage(Colorize("&2Derp! reloading the plugin :o"));
            Bukkit.getPluginManager().disablePlugin(this);
            Bukkit.getPluginManager().enablePlugin(this);
            moduleManager.reloadModules();
	}

	@Override
	public void onDisable() {
		getServer().getScheduler().cancelTasks(this);
		moduleManager.unloadModules();
		//saveConfig();
	}

	@Override
	public void onEnable() {
            if (moduleManager == null) {
                moduleManager = new ModuleManager(this);
            }
		if (Bukkit.getServer().getPluginManager().isPluginEnabled("WorldEdit")){
			PermissionsResolverManager.initialize(this); //wepif
			PickleCraftPlugin.worldedit = true;
		}
		getConfig().options().copyDefaults(true);
		saveConfig();
		reloadConfig();
		/* set up new modules */
                moduleManager.loadModules();
		Bukkit.getPluginManager().registerEvents(this, this);

	}
	
	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		Player player = null;
		if (sender instanceof Player) {
                    player = (Player) sender;
		}
		if (command.getName().equalsIgnoreCase("picklecraft")){ 
                    if (args.length > 0) {
                        if (player !=null) {
                            if (hasPerm(player,"PickleCraft.reload")) {
                                if (args[0].equalsIgnoreCase("reload")) {
                                    reload();
                                }
                            }
                        }
                    }
                    else {
				PluginDescriptionFile pdfFile = this.getDescription();
				sender.sendMessage(ChatColor.DARK_GREEN+pdfFile.getName() +" Version "+ pdfFile.getVersion());
				for (IModule module : moduleManager.modules) {
					module.sendCommandList(sender);
				}
			}
			return true;
		}	
		else if (command.getName().equalsIgnoreCase("pony")){ 
			if (player != null) {
				if (args.length >= 1) {
					Pony.Say(player, args[0]);
				}
				else {
					Pony.SayRandom(player);
				}
			}
			else {
				sender.sendMessage("This is a player only command.");
			}
			return true;
		}
		else {
			boolean success = false;
			for (IModule module : moduleManager.modules) {
				if (module.onCommand(sender, command, label, args) == true) { 
					success = true; 
				}
			}
			return success;
		}
	}
	
	public void Damage(Player player,int amount) {
		player.getItemInHand().setDurability((short) (player.getItemInHand().getDurability() + amount));
	}
	
	@EventHandler(priority = EventPriority.LOW)
	public void onPluginDisabled(PluginDisableEvent event) {
		if ("WorldEdit".equals(event.getPlugin().getName())) {
			PickleCraftPlugin.worldedit = false;
		}
	}
	@EventHandler(priority = EventPriority.LOW)
	public void onPluginEnabled(PluginEnableEvent event) {
		if ("WorldEdit".equals(event.getPlugin().getName())) {
			PickleCraftPlugin.worldedit = true;
			PermissionsResolverManager.initialize(this);
		}
	}
}

